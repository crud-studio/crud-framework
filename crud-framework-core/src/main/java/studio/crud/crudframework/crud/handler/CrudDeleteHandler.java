package studio.crud.crudframework.crud.handler;

import studio.crud.crudframework.crud.hooks.HooksDTO;
import studio.crud.crudframework.crud.hooks.delete.CRUDOnDeleteHook;
import studio.crud.crudframework.crud.hooks.delete.CRUDPostDeleteHook;
import studio.crud.crudframework.crud.hooks.delete.CRUDPreDeleteHook;
import studio.crud.crudframework.model.BaseCrudEntity;

import java.io.Serializable;
import java.util.List;

public interface CrudDeleteHandler {

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> void deleteInternal(ID id, Class<Entity> clazz,
			HooksDTO<CRUDPreDeleteHook<ID, Entity>, CRUDOnDeleteHook<ID, Entity>, CRUDPostDeleteHook<ID, Entity>> hooks);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity deleteHardTransactional(ID id, Class<Entity> clazz, List<CRUDOnDeleteHook<ID, Entity>> onHooks);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity deleteSoftTransactional(ID id, String deleteField, Class<Entity> clazz, List<CRUDOnDeleteHook<ID, Entity>> onHooks);
}
