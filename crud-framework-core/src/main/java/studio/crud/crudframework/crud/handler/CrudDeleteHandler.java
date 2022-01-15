package studio.crud.crudframework.crud.handler;

import studio.crud.crudframework.crud.dataaccess.model.DataAccessorDTO;
import studio.crud.crudframework.crud.hooks.HooksDTO;
import studio.crud.crudframework.crud.hooks.delete.*;
import studio.crud.crudframework.model.BaseCrudEntity;

import java.io.Serializable;
import java.util.List;

public interface CrudDeleteHandler {

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> void deleteInternal(ID id, Class<Entity> clazz,
			HooksDTO<CRUDPreDeleteHook<ID, Entity>, CRUDOnDeleteHook<ID, Entity>, CRUDPostDeleteHook<ID, Entity>> hooks,
			DataAccessorDTO accessorDTO);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity deleteHardTransactional(ID id, Class<Entity> clazz, List<CRUDOnDeleteHook<ID, Entity>> onHooks, DataAccessorDTO accessorDTO);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity deleteSoftTransactional(ID id, String deleteField, Class<Entity> clazz, List<CRUDOnDeleteHook<ID, Entity>> onHooks, DataAccessorDTO accessorDTO);
}
