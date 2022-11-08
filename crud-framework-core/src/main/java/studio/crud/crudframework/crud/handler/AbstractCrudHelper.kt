package studio.crud.crudframework.crud.handler

import studio.crud.crudframework.crud.cache.CrudCache
import studio.crud.crudframework.crud.hooks.interfaces.CRUDHooks
import studio.crud.crudframework.crud.model.EntityMetadataDTO
import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.crudframework.modelfilter.DynamicModelFilter
import studio.crud.crudframework.modelfilter.FilterField
import java.io.Serializable

abstract class AbstractCrudHelper : CrudHelper {
    override fun <ID : Serializable?, Entity : BaseCrudEntity<ID>?, HooksType : CRUDHooks<*, out BaseCrudEntity<*>>?> getHooks(crudHooksClazz: Class<HooksType>?, entityClazz: Class<Entity>?): MutableList<HooksType> {
        throw UnsupportedOperationException()
    }

    override fun <ID : Serializable?, Entity : BaseCrudEntity<ID>?> isEntityDeleted(entity: Entity): Boolean {
        throw UnsupportedOperationException()
    }

    override fun <ID : Serializable?, Entity : BaseCrudEntity<ID>?> decorateFilter(filter: DynamicModelFilter?, entityClazz: Class<Entity>?, forUpdate: Boolean) {
        throw UnsupportedOperationException()
    }

    override fun <ID : Serializable?, Entity : BaseCrudEntity<ID>?> validateAndFillFilterFieldMetadata(
        filterFields: MutableList<FilterField>?,
        entityClazz: Class<Entity>?
    ) {
        throw UnsupportedOperationException()
    }

    override fun <ID : Serializable?, Entity : BaseCrudEntity<ID>?> getEntities(
            filter: DynamicModelFilter?, entityClazz: Class<Entity>?, persistCopy: Boolean?, forUpdate: Boolean): MutableList<Entity> {
        throw UnsupportedOperationException()
    }

    override fun <ID : Serializable?, Entity : BaseCrudEntity<ID>?> getEntitiesCount(filter: DynamicModelFilter?, entityClazz: Class<Entity>?, forUpdate: Boolean): Long {
        throw UnsupportedOperationException()
    }

    override fun <ID : Serializable?, Entity : BaseCrudEntity<ID>?> getEntityById(entityId: ID, entityClazz: Class<Entity>?, persistCopy: Boolean?, forUpdate: Boolean): Entity {
        throw UnsupportedOperationException()
    }

    override fun <ID : Serializable?, Entity : BaseCrudEntity<ID>?> getEntityCountById(entityId: ID, entityClazz: Class<Entity>?, forUpdate: Boolean): Long {
        throw UnsupportedOperationException()
    }

    override fun <ID : Serializable?, Entity : BaseCrudEntity<ID>?> checkEntityImmutability(clazz: Class<Entity>?) {
        throw UnsupportedOperationException()
    }

    override fun <ID : Serializable?, Entity : BaseCrudEntity<ID>?> checkEntityDeletability(clazz: Class<Entity>?) {
        throw UnsupportedOperationException()
    }

    override fun <ID : Serializable?, Entity : BaseCrudEntity<ID>?> getEntityMetadata(entityClazz: Class<Entity>?): EntityMetadataDTO {
        throw UnsupportedOperationException()
    }

    override fun <ID : Serializable?, Entity : BaseCrudEntity<ID>?> evictEntityFromCache(entity: Entity) {
        throw UnsupportedOperationException()
    }

    override fun <ID : Serializable?, Entity : BaseCrudEntity<ID>?> getEntityCache(clazz: Class<Entity>?): CrudCache {
        throw UnsupportedOperationException()
    }

    override fun validate(target: Any?) {
        throw UnsupportedOperationException()
    }

    override fun <From : Any?, To : Any?> fill(fromObject: From, toClazz: Class<To>): To {
        throw UnsupportedOperationException()
    }

    override fun <From : Any?, To : Any?> fill(fromObject: From, toObject: To) {
        throw UnsupportedOperationException()
    }

    override fun <From : Any?, To : Any?> fillMany(fromObjects: MutableList<From>, toClazz: Class<To>): List<To> {
        throw UnsupportedOperationException()
    }

    override fun <Entity : Any?> setTotalToPagingCache(entityClazz: Class<Entity>?, filter: DynamicModelFilter?, total: Long) {
        throw UnsupportedOperationException()
    }

    override fun <Entity : Any?> getTotalFromPagingCache(entityClazz: Class<Entity>?, filter: DynamicModelFilter?): Long {
        throw UnsupportedOperationException()
    }

    override fun <ID : Serializable?, Entity : BaseCrudEntity<ID>?> getCrudDaoForEntity(entityClazz: Class<Entity>?): CrudDao {
        throw UnsupportedOperationException()
    }
}