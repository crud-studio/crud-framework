package studio.crud.crudframework.mongo.model

import dev.krud.shapeshift.resolver.annotation.MappedField
import org.springframework.data.annotation.Id
import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.crudframework.mongo.annotation.MongoCrudEntity
import studio.crud.crudframework.mongo.ro.BaseMongoRO
import java.util.Date

@MongoCrudEntity
abstract class BaseMongoEntity : BaseCrudEntity<String>() {
    @MappedField(target = BaseMongoRO::class)
    @Id
    override lateinit var id: String

    @MappedField(target = BaseMongoRO::class)
    var creationTime: Date = Date()

    override fun exists(): Boolean = this::id.isInitialized
}