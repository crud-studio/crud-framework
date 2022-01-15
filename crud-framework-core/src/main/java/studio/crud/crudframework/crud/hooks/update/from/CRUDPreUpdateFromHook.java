package studio.crud.crudframework.crud.hooks.update.from;

import studio.crud.crudframework.crud.hooks.base.CRUDHook;
import studio.crud.crudframework.model.BaseCrudEntity;

import java.io.Serializable;

@FunctionalInterface
public interface CRUDPreUpdateFromHook<ID extends Serializable, Entity extends BaseCrudEntity<ID>> extends CRUDHook {

	void run(ID id, Object object);
}
