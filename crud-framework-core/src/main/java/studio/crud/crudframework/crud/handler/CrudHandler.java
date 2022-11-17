package studio.crud.crudframework.crud.handler;

import studio.crud.crudframework.crud.annotation.DeleteColumn;
import studio.crud.crudframework.crud.annotation.Deleteable;
import studio.crud.crudframework.crud.hooks.create.CRUDOnCreateHook;
import studio.crud.crudframework.crud.hooks.create.CRUDPostCreateHook;
import studio.crud.crudframework.crud.hooks.create.CRUDPreCreateHook;
import studio.crud.crudframework.crud.hooks.create.from.CRUDOnCreateFromHook;
import studio.crud.crudframework.crud.hooks.create.from.CRUDPostCreateFromHook;
import studio.crud.crudframework.crud.hooks.create.from.CRUDPreCreateFromHook;
import studio.crud.crudframework.crud.hooks.delete.CRUDOnDeleteHook;
import studio.crud.crudframework.crud.hooks.delete.CRUDPostDeleteHook;
import studio.crud.crudframework.crud.hooks.delete.CRUDPreDeleteHook;
import studio.crud.crudframework.crud.hooks.index.CRUDOnIndexHook;
import studio.crud.crudframework.crud.hooks.index.CRUDPostIndexHook;
import studio.crud.crudframework.crud.hooks.index.CRUDPreIndexHook;
import studio.crud.crudframework.crud.hooks.show.CRUDOnShowHook;
import studio.crud.crudframework.crud.hooks.show.CRUDPostShowHook;
import studio.crud.crudframework.crud.hooks.show.CRUDPreShowHook;
import studio.crud.crudframework.crud.hooks.show.by.CRUDOnShowByHook;
import studio.crud.crudframework.crud.hooks.show.by.CRUDPostShowByHook;
import studio.crud.crudframework.crud.hooks.show.by.CRUDPreShowByHook;
import studio.crud.crudframework.crud.hooks.update.CRUDOnUpdateHook;
import studio.crud.crudframework.crud.hooks.update.CRUDPostUpdateHook;
import studio.crud.crudframework.crud.hooks.update.CRUDPreUpdateHook;
import studio.crud.crudframework.crud.hooks.update.from.CRUDOnUpdateFromHook;
import studio.crud.crudframework.crud.hooks.update.from.CRUDPostUpdateFromHook;
import studio.crud.crudframework.crud.hooks.update.from.CRUDPreUpdateFromHook;
import studio.crud.crudframework.crud.model.CRUDRequestBuilder;
import studio.crud.crudframework.crud.model.MassUpdateCRUDRequestBuilder;
import studio.crud.crudframework.crud.model.ReadCRUDRequestBuilder;
import studio.crud.crudframework.crud.model.UpdateCRUDRequestBuilder;
import studio.crud.crudframework.model.BaseCrudEntity;
import studio.crud.crudframework.modelfilter.DynamicModelFilter;
import studio.crud.crudframework.ro.PagingDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Used for all CRUD actions
 */
public interface CrudHandler {

    /**
     * Index request, returns list of {@code T} entity according to {@code filter}
     *
     * @param <Entity> {@link BaseCrudEntity} type
     * @param filter   the filter
     * @param clazz    the entity class
     * @return {@link ReadCRUDRequestBuilder} use {@link CRUDRequestBuilder#execute()} to run the request
     */
    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> ReadCRUDRequestBuilder<CRUDPreIndexHook<ID, Entity>, CRUDOnIndexHook<ID, Entity>, CRUDPostIndexHook<ID, Entity>, PagingDTO<Entity>> index(
            DynamicModelFilter filter, Class<Entity> clazz);

    /**
     * Index request, returns list of {@code T}  entity according to {@code filter}
     *
     * @param <Entity> {@link BaseCrudEntity} type
     * @param <RO>     return object type
     * @param filter   the filter
     * @param clazz    the entity class
     * @param toClazz  the return object class
     * @return {@link ReadCRUDRequestBuilder} use {@link CRUDRequestBuilder#execute()} to run the request
     */
    <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> ReadCRUDRequestBuilder<CRUDPreIndexHook<ID, Entity>, CRUDOnIndexHook<ID, Entity>, CRUDPostIndexHook<ID, Entity>, PagingDTO<RO>> index(
            DynamicModelFilter filter, Class<Entity> clazz, Class<RO> toClazz);

    /**
     * Delete request, deletes according to {@link Deleteable} and {@link DeleteColumn} if {@link Deleteable#softDelete()} is used. Throws runtime exception if given entity is not deletable
     *
     * @param <Entity> {@link BaseCrudEntity} type
     * @param id       the entity id
     * @param clazz    the entity class
     * @return {@link UpdateCRUDRequestBuilder} use {@link CRUDRequestBuilder#execute()} to run the request
     */
    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> UpdateCRUDRequestBuilder<CRUDPreDeleteHook<ID, Entity>, CRUDOnDeleteHook<ID, Entity>, CRUDPostDeleteHook<ID, Entity>, Void> delete(ID id,
                                                                                                                                                                                                    Class<Entity> clazz);

    /**
     * Create From request, uses {@code object} to fill and create a new entity of type {@code class}
     *
     * @param <Entity> {@link BaseCrudEntity} type
     * @param object   the object from which the entity is filled
     * @param clazz    the entity class
     * @return {@link UpdateCRUDRequestBuilder} use {@link CRUDRequestBuilder#execute()} to run the request
     */
    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> UpdateCRUDRequestBuilder<CRUDPreCreateFromHook<ID, Entity>, CRUDOnCreateFromHook<ID, Entity>, CRUDPostCreateFromHook<ID, Entity>, Entity> createFrom(
            Object object, Class<Entity> clazz);

    /**
     * Create From request, uses {@code object} to fill and create a new entity of type {@code class}
     *
     * @param <Entity> {@link BaseCrudEntity} type
     * @param <RO>     return object type
     * @param object   the object from which the entity is filled
     * @param clazz    the entity class
     * @param toClazz  the return object class
     * @return {@link UpdateCRUDRequestBuilder} use {@link CRUDRequestBuilder#execute()} to run the request
     */
    <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> UpdateCRUDRequestBuilder<CRUDPreCreateFromHook<ID, Entity>, CRUDOnCreateFromHook<ID, Entity>, CRUDPostCreateFromHook<ID, Entity>, RO> createFrom(
            Object object, Class<Entity> clazz, Class<RO> toClazz);

    /**
     * Create Request, persists the given {@code entity}, throws a runtime exception if the entity already exists
     *
     * @param <Entity> {@link BaseCrudEntity} type
     * @param entity   the entity
     * @return {@link UpdateCRUDRequestBuilder} use {@link CRUDRequestBuilder#execute()} to run the request
     */
    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> UpdateCRUDRequestBuilder<CRUDPreCreateHook<ID, Entity>, CRUDOnCreateHook<ID, Entity>, CRUDPostCreateHook<ID, Entity>, Entity> create(Entity entity);

    /**
     * Create Request, persists the given {@code entity}, throws a runtime exception if the entity already exists
     *
     * @param <Entity> {@link BaseCrudEntity} type
     * @param <RO>     return object type
     * @param entity   the entity
     * @param toClazz  the return object class
     * @return {@link UpdateCRUDRequestBuilder} use {@link CRUDRequestBuilder#execute()} to run the request
     */
    <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> UpdateCRUDRequestBuilder<CRUDPreCreateHook<ID, Entity>, CRUDOnCreateHook<ID, Entity>, CRUDPostCreateHook<ID, Entity>, RO> create(Entity entity,
                                                                                                                                                                                                      Class<RO> toClazz);

    /**
     * Update From request, uses {@code object} to fill and update an existing entity of type {@code class} with {@code id}, throws a runtime exception if the entity does not exist
     *
     * @param <Entity> {@link BaseCrudEntity} type
     * @param id       the entity id
     * @param object   the object from which the entity is filled
     * @param clazz    the entity class
     * @return {@link UpdateCRUDRequestBuilder} use {@link CRUDRequestBuilder#execute()} to run the request
     */
    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> UpdateCRUDRequestBuilder<CRUDPreUpdateFromHook<ID, Entity>, CRUDOnUpdateFromHook<ID, Entity>, CRUDPostUpdateFromHook<ID, Entity>, Entity> updateFrom(ID id,
                                                                                                                                                                                                                      Object object, Class<Entity> clazz);

    /**
     * Update From request, uses {@code object} to fill and update an existing entity of type {@code class} with {@code id}, throws a runtime exception if the entity does not exist
     *
     * @param <Entity> {@link BaseCrudEntity} type
     * @param <RO>     return object type
     * @param id       the entity id
     * @param object   the object from which the entity is filled
     * @param clazz    the entity class
     * @param toClazz  the return object class
     * @return {@link UpdateCRUDRequestBuilder} use {@link CRUDRequestBuilder#execute()} to run the request
     */
    <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> UpdateCRUDRequestBuilder<CRUDPreUpdateFromHook<ID, Entity>, CRUDOnUpdateFromHook<ID, Entity>, CRUDPostUpdateFromHook<ID, Entity>, RO> updateFrom(ID id,
                                                                                                                                                                                                                      Object object, Class<Entity> clazz, Class<RO> toClazz);

    /**
     * Update request, persists an existing entity
     *
     * @param <Entity> {@link BaseCrudEntity} type
     * @param entity   the entity
     * @return {@link UpdateCRUDRequestBuilder} use {@link CRUDRequestBuilder#execute()} to run the request
     */
    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> UpdateCRUDRequestBuilder<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>, Entity> update(Entity entity);

    /**
     * Update request, persists an existing entity
     *
     * @param <Entity> {@link BaseCrudEntity} type
     * @param <RO>     return object type
     * @param entity   the entity
     * @param toClazz  the return object class
     * @return {@link UpdateCRUDRequestBuilder} use {@link CRUDRequestBuilder#execute()} to run the request
     */
    <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> UpdateCRUDRequestBuilder<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>, RO> update(Entity entity,
                                                                                                                                                                                                      Class<RO> toClazz);

    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> MassUpdateCRUDRequestBuilder<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>, List<Entity>> update(
            List<Entity> entities);

    <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> MassUpdateCRUDRequestBuilder<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>, List<RO>> update(
            List<Entity> entities, Class<RO> toClazz);

    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> MassUpdateCRUDRequestBuilder<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>, List<Entity>> updateByFilter(
            DynamicModelFilter filter, Class<Entity> entityClazz);

    <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> MassUpdateCRUDRequestBuilder<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>, List<RO>> updateByFilter(
            DynamicModelFilter filter, Class<Entity> entityClazz, Class<RO> toClazz);

    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> ReadCRUDRequestBuilder<CRUDPreShowByHook<ID, Entity>, CRUDOnShowByHook<ID, Entity>, CRUDPostShowByHook<ID, Entity>, Entity> showBy(
            DynamicModelFilter filter, Class<Entity> clazz);

    <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> ReadCRUDRequestBuilder<CRUDPreShowByHook<ID, Entity>, CRUDOnShowByHook<ID, Entity>, CRUDPostShowByHook<ID, Entity>, RO> showBy(
            DynamicModelFilter filter, Class<Entity> clazz, Class<RO> toClazz);

    /**
     * Show request, returns a single {@code T} entity according with the given {@code id}
     *
     * @param <Entity> {@link BaseCrudEntity} type
     * @param id       the entity id
     * @param clazz    the entity class
     * @return {@link ReadCRUDRequestBuilder} use {@link CRUDRequestBuilder#execute()} to run the request
     */
    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> ReadCRUDRequestBuilder<CRUDPreShowHook<ID, Entity>, CRUDOnShowHook<ID, Entity>, CRUDPostShowHook<ID, Entity>, Entity> show(ID id, Class<Entity> clazz);

    /**
     * Show request, returns a single {@code T} entity according with the given {@code id}
     *
     * @param <Entity> {@link BaseCrudEntity} type
     * @param <RO>     return object type
     * @param id       the entity id
     * @param clazz    the entity class
     * @param toClazz  the return object class
     * @return {@link ReadCRUDRequestBuilder} use {@link CRUDRequestBuilder#execute()} to run the request
     */
    <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> ReadCRUDRequestBuilder<CRUDPreShowHook<ID, Entity>, CRUDOnShowHook<ID, Entity>, CRUDPostShowHook<ID, Entity>, RO> show(ID id, Class<Entity> clazz,
                                                                                                                                                                                            Class<RO> toClazz);


    /**
     * Runs field validation on {@code target}, throwing an exception with violations if the validation failed
     *
     * @param target
     */
    void validate(Object target);

    /**
     * Validate a filter against an entity
     *
     * @param filter the filter
     * @param clazz  the entity class
     */
    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> void validateFilter(DynamicModelFilter filter, Class<Entity> clazz);

    /**
     * Check whether the given filter matches the given entity
     *
     * @param filter the filter to check
     * @param entity the entity to check against
     */
    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> boolean filterMatches(DynamicModelFilter filter, Entity entity);
}
