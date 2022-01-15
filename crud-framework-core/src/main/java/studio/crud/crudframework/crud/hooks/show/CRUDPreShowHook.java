package studio.crud.crudframework.crud.hooks.show;

import studio.crud.crudframework.crud.hooks.base.IDHook;
import studio.crud.crudframework.model.BaseCrudEntity;

import java.io.Serializable;

@FunctionalInterface
public interface CRUDPreShowHook<ID extends Serializable, Entity extends BaseCrudEntity<ID>> extends IDHook<ID> {

}
