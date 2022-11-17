package studio.crud.crudframework.crud.handler;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import studio.crud.crudframework.crud.exception.CrudDeleteException;
import studio.crud.crudframework.crud.hooks.HooksDTO;
import studio.crud.crudframework.crud.hooks.delete.CRUDOnDeleteHook;
import studio.crud.crudframework.crud.hooks.delete.CRUDPostDeleteHook;
import studio.crud.crudframework.crud.hooks.delete.CRUDPreDeleteHook;
import studio.crud.crudframework.crud.hooks.interfaces.DeleteHooks;
import studio.crud.crudframework.crud.model.EntityMetadataDTO;
import studio.crud.crudframework.crud.policy.PolicyRuleType;
import studio.crud.crudframework.exception.WrapException;
import studio.crud.crudframework.model.BaseCrudEntity;
import studio.crud.crudframework.modelfilter.DynamicModelFilter;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@WrapException(CrudDeleteException.class)
public class CrudDeleteHandlerImpl implements CrudDeleteHandler {

	@Autowired
	private CrudHelper crudHelper;

	@Resource(name = "crudDeleteHandler")
	private CrudDeleteHandler crudDeleteHandlerProxy;

	@Autowired
	private CrudSecurityHandler crudSecurityHandler;

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> void deleteInternal(ID id, Class<Entity> clazz,
																							HooksDTO<CRUDPreDeleteHook<ID, Entity>, CRUDOnDeleteHook<ID, Entity>, CRUDPostDeleteHook<ID, Entity>> hooks, boolean applyDefaultPolicies) {
		if (applyDefaultPolicies) {
			crudSecurityHandler.evaluatePreRulesAndThrow(PolicyRuleType.CAN_DELETE, clazz);
		}
		
		crudHelper.checkEntityImmutability(clazz);
		crudHelper.checkEntityDeletability(clazz);

		List<DeleteHooks> deleteHooksList = crudHelper.getHooks(DeleteHooks.class, clazz);

		if(deleteHooksList != null && !deleteHooksList.isEmpty()) {
			for(DeleteHooks<ID, Entity> deleteHooks : deleteHooksList) {
				hooks.getPreHooks().add(0, deleteHooks::preDelete);
				hooks.getOnHooks().add(0, deleteHooks::onDelete);
				hooks.getPostHooks().add(0, deleteHooks::postDelete);
			}
		}

		for(CRUDPreDeleteHook<ID, Entity> preHook : hooks.getPreHooks()) {
			preHook.run(id);
		}

		EntityMetadataDTO metadataDTO = crudHelper.getEntityMetadata(clazz);

		Entity entity;

		if(metadataDTO.getDeleteableType() == EntityMetadataDTO.DeleteableType.Hard) {
			entity = crudDeleteHandlerProxy.deleteHardTransactional(id, clazz, hooks.getOnHooks(), applyDefaultPolicies);
		} else {

			entity = crudDeleteHandlerProxy.deleteSoftTransactional(id, metadataDTO.getDeleteField().getName(), clazz, hooks.getOnHooks(), applyDefaultPolicies);
		}

		crudHelper.evictEntityFromCache(entity);

		for(CRUDPostDeleteHook<ID, Entity> postHook : hooks.getPostHooks()) {
			postHook.run(entity);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity deleteHardTransactional(ID id, Class<Entity> clazz, List<CRUDOnDeleteHook<ID, Entity>> onHooks, boolean applyDefaultPolicies) {
		Entity entity = getEntityForDeletion(id, clazz, applyDefaultPolicies);
		if(crudHelper.isEntityDeleted(entity)) {
			throw new CrudDeleteException("Entity of type [ " + clazz.getSimpleName() + " ] does not exist or cannot be deleted");
		}

		for(CRUDOnDeleteHook<ID, Entity> onHook : onHooks) {
			onHook.run(entity);
		}

		crudHelper.getCrudDaoForEntity(clazz).hardDeleteById(id, clazz);
		return entity;
	}

	@Override
	@Transactional(readOnly = false)
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity deleteSoftTransactional(ID id, String deleteField, Class<Entity> clazz, List<CRUDOnDeleteHook<ID, Entity>> onHooks, boolean applyDefaultPolicies) {
		Entity entity = getEntityForDeletion(id, clazz, applyDefaultPolicies);
		
		if(crudHelper.isEntityDeleted(entity)) {
			throw new CrudDeleteException("Entity of type [ " + clazz.getSimpleName() + " ] does not exist or cannot be deleted");
		}


		for(CRUDOnDeleteHook<ID, Entity> onHook : onHooks) {
			onHook.run(entity);
		}
		crudHelper.getCrudDaoForEntity(clazz).softDeleteById(id, deleteField, clazz);

		return entity;
	}

	@Nullable
	private <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity getEntityForDeletion(ID id, Class<Entity> clazz, boolean applyDefaultPolicies) {
		DynamicModelFilter filter = crudHelper.getIdFilter(id);
		if (applyDefaultPolicies) {
			filter.getFilterFields().addAll(crudSecurityHandler.getFilterFields(clazz));
		}
		Entity entity = crudHelper.getEntities(filter, clazz, false).stream().findFirst().orElse(null);
		if (applyDefaultPolicies && entity != null) {
			crudSecurityHandler.evaluatePostRulesAndThrow(entity, PolicyRuleType.CAN_DELETE, clazz);
		}
		return entity;
	}

}
