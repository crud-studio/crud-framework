package studio.crud.crudframework.crud.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import studio.crud.crudframework.crud.exception.CrudCreateException;
import studio.crud.crudframework.crud.exception.CrudDeleteException;
import studio.crud.crudframework.crud.hooks.HooksDTO;
import studio.crud.crudframework.crud.hooks.create.CRUDOnCreateHook;
import studio.crud.crudframework.crud.hooks.create.CRUDPostCreateHook;
import studio.crud.crudframework.crud.hooks.create.CRUDPreCreateHook;
import studio.crud.crudframework.crud.hooks.create.from.CRUDOnCreateFromHook;
import studio.crud.crudframework.crud.hooks.create.from.CRUDPostCreateFromHook;
import studio.crud.crudframework.crud.hooks.create.from.CRUDPreCreateFromHook;
import studio.crud.crudframework.crud.hooks.interfaces.CreateFromHooks;
import studio.crud.crudframework.crud.hooks.interfaces.CreateHooks;
import studio.crud.crudframework.exception.WrapException;
import studio.crud.crudframework.model.BaseCrudEntity;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@WrapException(CrudCreateException.class)
public class CrudCreateHandlerImpl implements CrudCreateHandler {

	@Autowired
	private CrudHelper crudHelper;

    @Autowired
    private CrudSecurityHandler crudSecurityHandler;

	@Resource(name = "crudCreateHandler")
	private CrudCreateHandler crudCreateHandlerProxy;

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity createInternal(Entity entity, HooksDTO<CRUDPreCreateHook<ID, Entity>, CRUDOnCreateHook<ID, Entity>, CRUDPostCreateHook<ID, Entity>> hooks) {
		Objects.requireNonNull(entity, "Entity cannot be null");
        crudSecurityHandler.matchesCanCreate(entity.getClass());
		List<CreateHooks> createHooksList = crudHelper.getHooks(CreateHooks.class, entity.getClass());

		if(createHooksList != null && !createHooksList.isEmpty()) {
			for(CreateHooks<ID, Entity> createHooks : createHooksList) {
				hooks.getPreHooks().add(0, createHooks::preCreate);
				hooks.getOnHooks().add(0, createHooks::onCreate);
				hooks.getPostHooks().add(0, createHooks::postCreate);
			}
		}

		for(CRUDPreCreateHook<ID, Entity> preHook : hooks.getPreHooks()) {
			preHook.run(entity);
		}

			entity = crudCreateHandlerProxy.createTransactional(entity, hooks.getOnHooks());
		for(CRUDPostCreateHook<ID, Entity> postHook : hooks.getPostHooks()) {
			postHook.run(entity);
		}

		return entity;
	}

	@Override
	@Transactional(readOnly = false)
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity createTransactional(Entity entity, List<CRUDOnCreateHook<ID, Entity>> onHooks) {
		for(CRUDOnCreateHook<ID, Entity> onHook : onHooks) {
			onHook.run(entity);
		}

		crudHelper.validate(entity);

		return crudHelper.getCrudDaoForEntity(entity.getClass()).saveOrUpdate(entity);
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity createFromInternal(Object object, Class<Entity> clazz,
			HooksDTO<CRUDPreCreateFromHook<ID, Entity>, CRUDOnCreateFromHook<ID, Entity>, CRUDPostCreateFromHook<ID, Entity>> hooks) {
		Objects.requireNonNull(object, "Object cannot be null");

		List<CreateFromHooks> createFromHooksList = crudHelper.getHooks(CreateFromHooks.class, clazz);

		if(createFromHooksList != null && !createFromHooksList.isEmpty()) {
			for(CreateFromHooks<ID, Entity> createFromHooks : createFromHooksList) {
				hooks.getPreHooks().add(0, createFromHooks::preCreateFrom);
				hooks.getOnHooks().add(0, createFromHooks::onCreateFrom);
				hooks.getPostHooks().add(0, createFromHooks::postCreateFrom);
			}
		}

		for(CRUDPreCreateFromHook preHook : hooks.getPreHooks()) {
			preHook.run(object);
		}

		crudHelper.validate(object);

		Entity entity = crudCreateHandlerProxy.createFromTransactional(object, clazz, hooks.getOnHooks());
		for(CRUDPostCreateFromHook<ID, Entity> postHook : hooks.getPostHooks()) {
			postHook.run(entity);
		}
		return entity;
	}

	@Override
	@Transactional(readOnly = false)
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity createFromTransactional(Object object, Class<Entity> clazz, List<CRUDOnCreateFromHook<ID, Entity>> onHooks) {
		Entity entity = crudHelper.fill(object, clazz);

		if(entity.exists()) {
			throw new CrudDeleteException("Entity of type [ " + clazz.getSimpleName() + " ] with ID [ " + entity.getId() + " ] already exists and cannot be created");
		}

		for(CRUDOnCreateFromHook<ID, Entity> onHook : onHooks) {
			onHook.run(entity, object);
		}

		crudHelper.validate(entity);

		return crudHelper.getCrudDaoForEntity(clazz).saveOrUpdate(entity);
	}

}
