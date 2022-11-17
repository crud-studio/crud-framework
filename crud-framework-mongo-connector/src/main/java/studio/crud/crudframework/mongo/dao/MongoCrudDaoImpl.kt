package studio.crud.crudframework.mongo.dao

import studio.crud.crudframework.crud.handler.CrudDao
import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.crudframework.modelfilter.DynamicModelFilter
import java.io.Serializable

class MongoCrudDaoImpl : CrudDao, AbstractMongoBaseDao() {
    override fun <ID : Serializable?, Entity : BaseCrudEntity<ID>?, Filter : DynamicModelFilter> index(filter: Filter, clazz: Class<Entity>?): MutableList<Entity> {
        val query = buildQuery(filter)
        setOrder(query, filter.orders)
        setBoundaries(query, filter.start, filter.limit)
        return mongoTemplate.find(query, clazz)
    }

    override fun <ID : Serializable?, Entity : BaseCrudEntity<ID>?, Filter : DynamicModelFilter?> indexCount(filter: Filter, clazz: Class<Entity>?): Long {
        return mongoTemplate.count(buildQuery(filter), clazz)
    }

    override fun <ID : Serializable?, Entity : BaseCrudEntity<ID>?> hardDeleteById(id: ID, clazz: Class<Entity>?) {
        deleteObject(clazz, id)
    }

    override fun <ID : Serializable?, Entity : BaseCrudEntity<ID>?> saveOrUpdate(entity: Entity): Entity {
        mongoTemplate.save(entity)
        return entity
    }
}