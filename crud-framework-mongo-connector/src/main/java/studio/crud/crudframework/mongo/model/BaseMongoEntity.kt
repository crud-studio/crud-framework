package studio.crud.crudframework.mongo.model

import dev.krud.shapeshift.annotation.MappedField
import dev.krud.shapeshift.transformer.DateToLongTransformer
import org.springframework.data.annotation.Id
import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.crudframework.mongo.annotation.MongoCrudEntity
import studio.crud.crudframework.mongo.ro.BaseMongoRO
import java.util.*


@MongoCrudEntity
abstract class BaseMongoEntity : BaseCrudEntity<String>() {
    @MappedField(target = BaseMongoRO::class)
    @Id
    override lateinit var id: String

    @MappedField(target = BaseMongoRO::class, transformer = DateToLongTransformer::class)
    var creationTime: Date = Date()

    override fun exists(): Boolean = this::id.isInitialized
}

