package studio.crud.crudframework.crud.handler;

import studio.crud.crudframework.crud.hooks.HooksDTO;
import studio.crud.crudframework.crud.hooks.create.CRUDOnCreateHook;
import studio.crud.crudframework.crud.hooks.create.CRUDPostCreateHook;
import studio.crud.crudframework.crud.hooks.create.CRUDPreCreateHook;
import studio.crud.crudframework.crud.hooks.create.from.CRUDOnCreateFromHook;
import studio.crud.crudframework.crud.hooks.create.from.CRUDPostCreateFromHook;
import studio.crud.crudframework.crud.hooks.create.from.CRUDPreCreateFromHook;
import studio.crud.crudframework.model.BaseCrudEntity;

import java.io.Serializable;
import java.util.List;

public interface CrudCreateHandler {

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity createInternal(Entity entity, HooksDTO<CRUDPreCreateHook<ID, Entity>, CRUDOnCreateHook<ID, Entity>, CRUDPostCreateHook<ID, Entity>> hooks, boolean applyDefaultPolicies);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity createTransactional(Entity entity, List<CRUDOnCreateHook<ID, Entity>> onHooks, boolean applyDefaultPolicies);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity createFromInternal(Object object, Class<Entity> clazz,
                                                                                           HooksDTO<CRUDPreCreateFromHook<ID, Entity>, CRUDOnCreateFromHook<ID, Entity>, CRUDPostCreateFromHook<ID, Entity>> hooks);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity createFromTransactional(Object object, Class<Entity> clazz, List<CRUDOnCreateFromHook<ID, Entity>> onHooks);
}
