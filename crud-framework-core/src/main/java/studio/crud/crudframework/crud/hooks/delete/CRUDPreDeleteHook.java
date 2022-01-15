package studio.crud.crudframework.crud.hooks.delete;

import studio.crud.crudframework.crud.hooks.base.IDHook;
import studio.crud.crudframework.model.BaseCrudEntity;

import java.io.Serializable;

@FunctionalInterface
public interface CRUDPreDeleteHook<ID extends Serializable, Entity extends BaseCrudEntity<ID>> extends IDHook<ID> {

}
