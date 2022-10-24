package studio.crud.crudframework.web.rest

import com.google.gson.Gson
import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import studio.crud.crudframework.crud.exception.CrudException
import studio.crud.crudframework.crud.handler.CrudHandler
import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.crudframework.modelfilter.DynamicModelFilter
import studio.crud.crudframework.ro.PagingDTO
import java.io.Serializable
import kotlin.reflect.KClass

class CrudRestServiceImpl(
    private val crudHandler: CrudHandler,
    private val crudControllerDefinitions: List<CrudControllerDefinition>
) : CrudRestService {
    private val crudControllerDefinitionMap = crudControllerDefinitions.associateBy { it.annotation.type }

    // #id = bla
    override fun show(type: String, id: Serializable): Any? {
        val definition = getCrudControllerDefinition(type)
        if (!definition.annotation.actions.show) {
            throw CrudException("Show action is not allowed for type $type")
        }
        definition.clazz as KClass<BaseCrudEntity<Serializable>>

        return crudHandler.show(id, definition.clazz.java, definition.effectiveShowRoClass()).execute()
    }

    override fun index(type: String, filter: DynamicModelFilter): PagingDTO<out Any> {
        val definition = getCrudControllerDefinition(type)
        if (!definition.annotation.actions.index) {
            throw CrudException("Index action is not allowed for type $type")
        }
        definition.clazz as KClass<BaseCrudEntity<Serializable>>
        return crudHandler.index(filter, definition.clazz.java, definition.effectiveIndexRoClass()).execute()
    }

    override fun delete(type: String, id: Serializable): Any? {
        val definition = getCrudControllerDefinition(type)
        if (!definition.annotation.actions.delete) {
            throw CrudException("Delete action is not allowed for type $type")
        }
        definition.clazz as KClass<BaseCrudEntity<Serializable>>
        return crudHandler.delete(id, definition.clazz.java).execute()
    }

    override fun create(type: String, body: String): Any? {
        val definition = getCrudControllerDefinition(type)
        if (!definition.annotation.actions.create) {
            throw CrudException("Create action is not allowed for type $type")
        }
        definition.clazz as KClass<BaseCrudEntity<Serializable>>
        val roClazz = definition.effectiveCreateRoClass()
        return crudHandler.createFrom(GSON.fromJson(body, roClazz), definition.clazz.java, definition.effectiveShowRoClass()).execute()
    }

    override fun update(type: String, id: Serializable, body: String): Any? {
        val definition = getCrudControllerDefinition(type)
        if (!definition.annotation.actions.update) {
            throw CrudException("Update action is not allowed for type $type")
        }
        definition.clazz as KClass<BaseCrudEntity<Serializable>>
        val roClazz = definition.effectiveUpdateRoClass()
        return crudHandler.updateFrom(id, GSON.fromJson(body, roClazz), definition.clazz.java, definition.effectiveShowRoClass()).execute()
    }

    private fun CrudControllerDefinition.effectiveMainRoClass(): Class<*> {
        return if (annotation.roMapping.mainRoClass != Unit::class) {
            annotation.roMapping.mainRoClass.java
        } else {
            val defaultMappingTarget = clazz.java.getAnnotation(DefaultMappingTarget::class.java)
            defaultMappingTarget?.value ?: throw IllegalArgumentException("No default mapping target found for class ${clazz.java.name} and no roMapping.mainRoClass specified")
            defaultMappingTarget.value.java
        }
    }

    private fun CrudControllerDefinition.effectiveShowRoClass(): Class<*> {
        return if (annotation.roMapping.showRoClass != Unit::class) {
            annotation.roMapping.showRoClass.java
        } else {
            effectiveMainRoClass()
        }
    }

    private fun CrudControllerDefinition.effectiveIndexRoClass(): Class<*> {
        return if (annotation.roMapping.indexRoClass != Unit::class) {
            annotation.roMapping.indexRoClass.java
        } else {
            effectiveMainRoClass()
        }
    }

    private fun CrudControllerDefinition.effectiveUpdateRoClass(): Class<*> {
        return if (annotation.roMapping.updateRoClass != Unit::class) {
            annotation.roMapping.updateRoClass.java
        } else {
            effectiveMainRoClass()
        }
    }

    private fun CrudControllerDefinition.effectiveCreateRoClass(): Class<*> {
        return if (annotation.roMapping.createRoClass != Unit::class) {
            annotation.roMapping.createRoClass.java
        } else {
            effectiveMainRoClass()
        }
    }

    private fun getCrudControllerDefinition(type: String): CrudControllerDefinition {
        return crudControllerDefinitionMap[type] ?: throw IllegalArgumentException("No crud controller definition found for type $type")
    }

    companion object {
        private val GSON = Gson()
    }
}