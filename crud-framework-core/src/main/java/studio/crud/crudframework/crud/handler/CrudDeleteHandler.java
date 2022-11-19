package studio.crud.crudframework.crud.handler;

import studio.crud.crudframework.crud.hooks.HooksDTO;
import studio.crud.crudframework.crud.hooks.delete.CRUDOnDeleteHook;
import studio.crud.crudframework.crud.hooks.delete.CRUDPostDeleteHook;
import studio.crud.crudframework.crud.hooks.delete.CRUDPreDeleteHook;
import studio.crud.crudframework.model.BaseCrudEntity;
import studio.crud.crudframework.modelfilter.DynamicModelFilter;

import java.io.Serializable;
import java.util.List;

public interface CrudDeleteHandler {

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> void deleteInternal(ID id, Class<Entity> clazz,
																					 HooksDTO<CRUDPreDeleteHook<ID, Entity>, CRUDOnDeleteHook<ID, Entity>, CRUDPostDeleteHook<ID, Entity>> hooks, boolean applyPolicies);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity deleteHardTransactional(DynamicModelFilter filter, Class<Entity> clazz, List<CRUDOnDeleteHook<ID, Entity>> onHooks, boolean applyPolicies);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity deleteSoftTransactional(DynamicModelFilter filter, String deleteField, Class<Entity> clazz, List<CRUDOnDeleteHook<ID, Entity>> onHooks, boolean applyPolicies);
}
