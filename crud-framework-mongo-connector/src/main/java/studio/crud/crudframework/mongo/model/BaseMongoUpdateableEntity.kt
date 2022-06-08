package studio.crud.crudframework.mongo.model

import dev.krud.shapeshift.annotation.MappedField
import dev.krud.shapeshift.transformer.DateToLongTransformer
import studio.crud.crudframework.mongo.ro.BaseMongoUpdatableRO
import java.util.Date

abstract class BaseMongoUpdateableEntity : BaseMongoEntity() {
    @MappedField(target = BaseMongoUpdatableRO::class, transformer = DateToLongTransformer::class)
    var lastUpdateTime: Date = Date()
}