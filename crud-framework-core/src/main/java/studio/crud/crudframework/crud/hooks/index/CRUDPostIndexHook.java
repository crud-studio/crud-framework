package studio.crud.crudframework.crud.hooks.index;

import studio.crud.crudframework.crud.hooks.base.FilterPagingDTOHook;
import studio.crud.crudframework.model.BaseCrudEntity;
import studio.crud.crudframework.modelfilter.DynamicModelFilter;

import java.io.Serializable;

@FunctionalInterface
public interface CRUDPostIndexHook<ID extends Serializable, Entity extends BaseCrudEntity<ID>> extends FilterPagingDTOHook<ID, Entity> {

}
