package studio.crud.crudframework.crud.hooks.base;

import studio.crud.crudframework.model.BaseCrudEntity;

import java.io.Serializable;

public interface EntityHook<ID extends Serializable, Entity extends BaseCrudEntity<ID>> extends CRUDHook {

	void run(Entity entity);

}
