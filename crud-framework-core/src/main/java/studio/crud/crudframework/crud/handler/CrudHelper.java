package studio.crud.crudframework.crud.handler;

import studio.crud.crudframework.crud.cache.CrudCache;
import studio.crud.crudframework.crud.dataaccess.DataAccessManager;
import studio.crud.crudframework.crud.dataaccess.model.DataAccessorDTO;
import studio.crud.crudframework.crud.decorator.ObjectDecorator;
import studio.crud.crudframework.crud.hooks.interfaces.CRUDHooks;
import studio.crud.crudframework.crud.model.EntityMetadataDTO;
import studio.crud.crudframework.model.BaseCrudEntity;
import studio.crud.crudframework.modelfilter.DynamicModelFilter;
import studio.crud.crudframework.modelfilter.FilterField;

import java.io.Serializable;
import java.util.List;

public interface CrudHelper {

    <ID extends Serializable, Entity extends BaseCrudEntity<ID>, HooksType extends CRUDHooks> List<HooksType> getHooks(Class<HooksType> crudHooksClazz, Class<Entity> entityClazz);

    <ID extends Serializable, Entity extends BaseCrudEntity<ID>> boolean isEntityDeleted(Entity entity);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> void decorateFilter(DynamicModelFilter filter, Class<Entity> entityClazz, DataAccessorDTO accessorDTO, boolean forUpdate);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> void validateAndFillFilterFieldMetadata(List<FilterField> filterFields, Class<Entity> entityClazz);

	/* transactional */
	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> List<Entity> getEntities(DynamicModelFilter filter, Class<Entity> entityClazz, DataAccessorDTO accessorDTO, Boolean persistCopy, boolean forUpdate);

	/* transactional */
	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> long getEntitiesCount(DynamicModelFilter filter, Class<Entity> entityClazz, DataAccessorDTO accessorDTO, boolean forUpdate);

	/* transactional */
	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity getEntityById(ID entityId, Class<Entity> entityClazz, Boolean persistCopy, DataAccessorDTO accessorDTO, boolean forUpdate);

	/* transactional */
	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> long getEntityCountById(ID entityId, Class<Entity> entityClazz, DataAccessorDTO accessorDTO, boolean forUpdate);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> DataAccessManager getAccessorManager(Class<?> accessorClazz, Class<Entity> entityClazz);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> void checkEntityImmutability(Class<Entity> clazz);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> void checkEntityDeletability(Class<Entity> clazz);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> EntityMetadataDTO getEntityMetadata(Class<Entity> entityClazz);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> void evictEntityFromCache(Entity entity);

	<Entity, E> ObjectDecorator<Entity, E> getObjectDecorator(Class<Entity> fromClass, Class<E> toClass);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> CrudCache getEntityCache(Class<Entity> clazz);

	void validate(Object target);


	<From, To> To fill(From fromObject, Class<To> toClazz);

	<From, To> void fill(From fromObject, To toObject);

	<From, To> List<To> fillMany(List<From> fromObjects, Class<To> toClazz);

	<Entity> void setTotalToPagingCache(Class<Entity> entityClazz, DynamicModelFilter filter, long total);

	<Entity> Long getTotalFromPagingCache(Class<Entity> entityClazz, DynamicModelFilter filter);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> CrudDao getCrudDaoForEntity(Class<Entity> entityClazz);
}
