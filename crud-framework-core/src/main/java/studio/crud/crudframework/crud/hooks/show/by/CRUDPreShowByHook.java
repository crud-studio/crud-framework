package studio.crud.crudframework.crud.hooks.show.by;

import studio.crud.crudframework.crud.hooks.base.CRUDHook;
import studio.crud.crudframework.model.BaseCrudEntity;
import studio.crud.crudframework.modelfilter.DynamicModelFilter;

import java.io.Serializable;

@FunctionalInterface
public interface CRUDPreShowByHook<ID extends Serializable, Entity extends BaseCrudEntity<ID>> extends CRUDHook {

	void run(DynamicModelFilter filter);

}
