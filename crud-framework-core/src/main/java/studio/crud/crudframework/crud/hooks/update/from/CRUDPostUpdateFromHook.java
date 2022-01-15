package studio.crud.crudframework.crud.hooks.update.from;

import studio.crud.crudframework.crud.hooks.base.EntityHook;
import studio.crud.crudframework.model.BaseCrudEntity;

import java.io.Serializable;

@FunctionalInterface
public interface CRUDPostUpdateFromHook<ID extends Serializable, Entity extends BaseCrudEntity<ID>> extends EntityHook<ID, Entity> {

}
