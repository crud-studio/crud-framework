package studio.crud.crudframework.web.rest

import studio.crud.crudframework.model.BaseCrudEntity
import java.io.Serializable
import kotlin.reflect.KClass

data class CrudControllerDefinition(
        val annotation: CrudController, val clazz: KClass<BaseCrudEntity<out Serializable>>
)