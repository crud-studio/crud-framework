package studio.crud.crudframework.mongo.model

import studio.crud.crudframework.fieldmapper.annotation.MappedField
import studio.crud.crudframework.fieldmapper.transformer.DateToLongTransformer
import studio.crud.crudframework.mongo.ro.BaseMongoUpdatableRO
import java.util.*

abstract class BaseMongoUpdateableEntity : BaseMongoEntity() {
    @MappedField(target = BaseMongoUpdatableRO::class, transformer = DateToLongTransformer::class)
    var lastUpdateTime: Date = Date()
}