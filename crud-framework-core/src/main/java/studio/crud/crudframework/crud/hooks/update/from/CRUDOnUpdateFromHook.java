package studio.crud.crudframework.crud.hooks.update.from;

import studio.crud.crudframework.crud.hooks.base.ObjectEntityHook;
import studio.crud.crudframework.model.BaseCrudEntity;

import java.io.Serializable;

@FunctionalInterface
public interface CRUDOnUpdateFromHook<ID extends Serializable, Entity extends BaseCrudEntity<ID>> extends ObjectEntityHook<ID, Entity> {

}
