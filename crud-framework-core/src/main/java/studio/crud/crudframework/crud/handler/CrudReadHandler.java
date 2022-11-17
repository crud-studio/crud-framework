package studio.crud.crudframework.crud.handler;

import studio.crud.crudframework.crud.enums.ShowByMode;
import studio.crud.crudframework.crud.hooks.HooksDTO;
import studio.crud.crudframework.crud.hooks.index.CRUDOnIndexHook;
import studio.crud.crudframework.crud.hooks.index.CRUDPostIndexHook;
import studio.crud.crudframework.crud.hooks.index.CRUDPreIndexHook;
import studio.crud.crudframework.crud.hooks.show.CRUDOnShowHook;
import studio.crud.crudframework.crud.hooks.show.CRUDPostShowHook;
import studio.crud.crudframework.crud.hooks.show.CRUDPreShowHook;
import studio.crud.crudframework.crud.hooks.show.by.CRUDOnShowByHook;
import studio.crud.crudframework.crud.hooks.show.by.CRUDPostShowByHook;
import studio.crud.crudframework.crud.hooks.show.by.CRUDPreShowByHook;
import studio.crud.crudframework.model.BaseCrudEntity;
import studio.crud.crudframework.modelfilter.DynamicModelFilter;
import studio.crud.crudframework.ro.PagingDTO;

import java.io.Serializable;
import java.util.List;

public interface CrudReadHandler {

    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> PagingDTO<Entity> indexInternal(DynamicModelFilter filter, Class<Entity> clazz,
                                                                                                 HooksDTO<CRUDPreIndexHook<ID, Entity>, CRUDOnIndexHook<ID, Entity>, CRUDPostIndexHook<ID, Entity>> hooks,
                                                                                                 boolean fromCache, Boolean persistCopy, boolean applyDefaultPolicies, boolean count);

    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> PagingDTO<Entity> indexTransactional(DynamicModelFilter filter, Class<Entity> clazz,
                                                                                                      List<CRUDOnIndexHook<ID, Entity>> onHooks,
                                                                                                      Boolean persistCopy, boolean applyDefaultPolicies, boolean count);

    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity showByInternal(DynamicModelFilter filter, Class<Entity> clazz,
                                                                                       HooksDTO<CRUDPreShowByHook<ID, Entity>, CRUDOnShowByHook<ID, Entity>, CRUDPostShowByHook<ID, Entity>> hooks, boolean fromCache, Boolean persistCopy, ShowByMode mode, boolean applyDefaultPolicies);

    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity showByTransactional(DynamicModelFilter filter, Class<Entity> clazz, List<CRUDOnShowByHook<ID, Entity>> onHooks,
                                                                                            Boolean persistCopy, ShowByMode mode, boolean applyDefaultPolicies);

    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity showInternal(ID id, Class<Entity> clazz,
                                                                                     HooksDTO<CRUDPreShowHook<ID, Entity>, CRUDOnShowHook<ID, Entity>, CRUDPostShowHook<ID, Entity>> hooks, boolean fromCache, Boolean persistCopy, boolean applyDefaultPolicies);

    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity showTransactional(ID id, Class<Entity> clazz, List<CRUDOnShowHook<ID, Entity>> onHooks, Boolean persistCopy, boolean applyDefaultPolicies);
}
