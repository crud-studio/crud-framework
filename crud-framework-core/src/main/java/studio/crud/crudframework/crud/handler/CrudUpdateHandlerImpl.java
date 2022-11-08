package studio.crud.crudframework.crud.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import studio.crud.crudframework.crud.exception.CrudUpdateException;
import studio.crud.crudframework.crud.hooks.HooksDTO;
import studio.crud.crudframework.crud.hooks.interfaces.UpdateFromHooks;
import studio.crud.crudframework.crud.hooks.interfaces.UpdateHooks;
import studio.crud.crudframework.crud.hooks.update.CRUDOnUpdateHook;
import studio.crud.crudframework.crud.hooks.update.CRUDPostUpdateHook;
import studio.crud.crudframework.crud.hooks.update.CRUDPreUpdateHook;
import studio.crud.crudframework.crud.hooks.update.from.CRUDOnUpdateFromHook;
import studio.crud.crudframework.crud.hooks.update.from.CRUDPostUpdateFromHook;
import studio.crud.crudframework.crud.hooks.update.from.CRUDPreUpdateFromHook;
import studio.crud.crudframework.exception.WrapException;
import studio.crud.crudframework.model.BaseCrudEntity;
import studio.crud.crudframework.modelfilter.DynamicModelFilter;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WrapException(value = CrudUpdateException.class)
public class CrudUpdateHandlerImpl implements CrudUpdateHandler {

	@Autowired
	private CrudHelper crudHelper;

	@Resource(name = "crudUpdateHandler")
	private CrudUpdateHandler crudUpdateHandlerProxy;

	@Override
	@Transactional(readOnly = false)
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> List<Entity> updateManyTransactional(List<Entity> entities,
			HooksDTO<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>> hooks, Boolean persistCopy) {
		List<Entity> finalEntityList = new ArrayList<>();
		for(Entity entity : entities) {
			finalEntityList.add(crudUpdateHandlerProxy.updateInternal(entity, hooks));
		}

		return finalEntityList;
	}

	@Override
	@Transactional(readOnly = false)
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> List<Entity> updateByFilterTransactional(DynamicModelFilter filter, Class<Entity> entityClazz,
			HooksDTO<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>> hooks, Boolean persistCopy) {
		List<Entity> entities = crudHelper.getEntities(filter, entityClazz, persistCopy, true);
		return crudUpdateHandlerProxy.updateManyTransactional(entities, hooks, persistCopy);
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity updateInternal(Entity entity, HooksDTO<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>> hooks) {
		Objects.requireNonNull(entity, "Entity cannot be null");
		crudHelper.checkEntityImmutability(entity.getClass());

		List<UpdateHooks> updateHooksList = crudHelper.getHooks(UpdateHooks.class, entity.getClass());

		if(updateHooksList != null && !updateHooksList.isEmpty()) {
			for(UpdateHooks<ID, Entity> updateHooks : updateHooksList) {
				hooks.getPreHooks().add(0, updateHooks::preUpdate);
				hooks.getOnHooks().add(0, updateHooks::onUpdate);
				hooks.getPostHooks().add(0, updateHooks::postUpdate);
			}
		}

		for(CRUDPreUpdateHook<ID, Entity> preHook : hooks.getPreHooks()) {
			preHook.run(entity);
		}

		entity = crudUpdateHandlerProxy.updateTransactional(entity, hooks.getOnHooks());

		crudHelper.evictEntityFromCache(entity);

		for(CRUDPostUpdateHook<ID, Entity> postHook : hooks.getPostHooks()) {
			postHook.run(entity);
		}

		return entity;
	}

	@Override
	@Transactional(readOnly = false)
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity updateTransactional(Entity entity, List<CRUDOnUpdateHook<ID, Entity>> onHooks) {
		// check id exists and has access to entity
		if(!entity.exists() || crudHelper.getEntityCountById(entity.getId(), entity.getClass(), true) == 0) {
			throw new CrudUpdateException("Entity of type [ " + entity.getClass().getSimpleName() + " ] does not exist or cannot be updated");
		}

		for(CRUDOnUpdateHook<ID, Entity> onHook : onHooks) {
			onHook.run(entity);
		}

		crudHelper.validate(entity);

		return crudHelper.getCrudDaoForEntity(entity.getClass()).saveOrUpdate(entity);
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity updateFromInternal(ID id, Object object, Class<Entity> clazz,
			HooksDTO<CRUDPreUpdateFromHook<ID, Entity>, CRUDOnUpdateFromHook<ID, Entity>, CRUDPostUpdateFromHook<ID, Entity>> hooks) {
		crudHelper.checkEntityImmutability(clazz);

		List<UpdateFromHooks> updateFromHooksList = crudHelper.getHooks(UpdateFromHooks.class, clazz);

		if(updateFromHooksList != null && !updateFromHooksList.isEmpty()) {
			for(UpdateFromHooks<ID, Entity> updateFromHooks : updateFromHooksList) {
				hooks.getPreHooks().add(0, updateFromHooks::preUpdateFrom);
				hooks.getOnHooks().add(0, updateFromHooks::onUpdateFrom);
				hooks.getPostHooks().add(0, updateFromHooks::postUpdateFrom);
			}
		}

		Objects.requireNonNull(object, "Object cannot be null");
		for(CRUDPreUpdateFromHook<ID, Entity> preHook : hooks.getPreHooks()) {
			preHook.run(id, object);
		}

		crudHelper.validate(object);

		Entity entity = crudUpdateHandlerProxy.updateFromTransactional(id, object, clazz, hooks.getOnHooks());

		crudHelper.evictEntityFromCache(entity);

		for(CRUDPostUpdateFromHook<ID, Entity> postHook : hooks.getPostHooks()) {
			postHook.run(entity);
		}

		return entity;
	}

	@Override
	@Transactional(readOnly = false)
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity updateFromTransactional(ID id, Object object, Class<Entity> clazz, List<CRUDOnUpdateFromHook<ID, Entity>> onHooks) {
		Entity entity = crudHelper.getEntityById(id, clazz, null, true);

		if(entity == null) {
			throw new CrudUpdateException("Entity of type [ " + clazz.getSimpleName() + " ] does not exist or cannot be updated");
		}

		crudHelper.fill(object, entity);

		for(CRUDOnUpdateFromHook<ID, Entity> onHook : onHooks) {
			onHook.run(entity, object);
		}

		crudHelper.validate(entity);

		return crudHelper.getCrudDaoForEntity(clazz).saveOrUpdate(entity);
	}


}
