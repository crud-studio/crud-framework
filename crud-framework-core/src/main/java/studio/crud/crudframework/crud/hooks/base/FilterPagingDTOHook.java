package studio.crud.crudframework.crud.hooks.base;

import studio.crud.crudframework.model.BaseCrudEntity;
import studio.crud.crudframework.modelfilter.DynamicModelFilter;
import studio.crud.crudframework.ro.PagingDTO;

import java.io.Serializable;

public interface FilterPagingDTOHook<ID extends Serializable, Entity extends BaseCrudEntity<ID>> extends CRUDHook {

	void run(DynamicModelFilter filter, PagingDTO<Entity> result);
}
