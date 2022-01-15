package studio.crud.crudframework.crud.hooks.create.from;

import studio.crud.crudframework.crud.hooks.base.CRUDHook;
import studio.crud.crudframework.model.BaseCrudEntity;

import java.io.Serializable;

@FunctionalInterface
public interface CRUDPreCreateFromHook<ID extends Serializable, Entity extends BaseCrudEntity<ID>> extends CRUDHook {

	void run(Object object);
}
