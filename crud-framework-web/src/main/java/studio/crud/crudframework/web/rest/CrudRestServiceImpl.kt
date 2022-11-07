package studio.crud.crudframework.web.rest

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.transaction.annotation.Transactional
import studio.crud.crudframework.crud.exception.CrudException
import studio.crud.crudframework.crud.handler.CrudHandler
import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.crudframework.modelfilter.DynamicModelFilter
import studio.crud.crudframework.ro.BaseRO
import studio.crud.crudframework.ro.PagingDTO
import studio.crud.crudframework.web.ro.ManyCrudResult
import studio.crud.crudframework.web.ro.ManyFailedReason
import java.io.Serializable
import kotlin.reflect.KClass

class CrudRestServiceImpl(
    private val crudHandler: CrudHandler,
    private val crudControllerDefinitions: List<CrudControllerDefinition>
) : CrudRestService {
    private val crudControllerDefinitionMap = crudControllerDefinitions.associateBy { it.annotation.resourceName }

    override fun show(resourceName: String, id: Serializable): Any? {
        val definition = getCrudControllerDefinition(resourceName)
        if (!definition.annotation.actions.show) {
            throw CrudException("Show action is not allowed for resource $resourceName")
        }
        definition.clazz as KClass<BaseCrudEntity<Serializable>>

        return crudHandler.show(id, definition.clazz.java, definition.effectiveShowRoClass()).execute()
    }

    override fun indexCount(resourceName: String, filter: DynamicModelFilter): Long {
        val definition = getCrudControllerDefinition(resourceName)
        if (!definition.annotation.actions.index) {
            throw CrudException("Index action is not allowed for resource $resourceName")
        }
        definition.clazz as KClass<BaseCrudEntity<Serializable>>

        return crudHandler.index(filter, definition.clazz.java, definition.effectiveIndexRoClass()).count()
    }

    override fun index(resourceName: String, filter: DynamicModelFilter): PagingDTO<out Any> {
        val definition = getCrudControllerDefinition(resourceName)
        if (!definition.annotation.actions.index) {
            throw CrudException("Index action is not allowed for resource $resourceName")
        }
        definition.clazz as KClass<BaseCrudEntity<Serializable>>
        return crudHandler.index(filter, definition.clazz.java, definition.effectiveIndexRoClass()).execute()
    }

    override fun delete(resourceName: String, id: Serializable): Any? {
        val definition = getCrudControllerDefinition(resourceName)
        if (!definition.annotation.actions.delete) {
            throw CrudException("Delete action is not allowed for resource $resourceName")
        }
        definition.clazz as KClass<BaseCrudEntity<Serializable>>
        return crudHandler.delete(id, definition.clazz.java).execute()
    }

    override fun create(resourceName: String, body: String): Any? {
        val definition = getCrudControllerDefinition(resourceName)
        if (!definition.annotation.actions.create) {
            throw CrudException("Create action is not allowed for resource $resourceName")
        }
        definition.clazz as KClass<BaseCrudEntity<Serializable>>
        val roClazz = definition.effectiveCreateRoClass()
        return crudHandler.createFrom(GSON.fromJson(body, roClazz), definition.clazz.java, definition.effectiveShowRoClass()).execute()
    }

    override fun createMany(resourceName: String, body: String): ManyCrudResult<out BaseRO<*>, out BaseRO<*>> {
        val definition = getCrudControllerDefinition(resourceName)
        if (!definition.annotation.actions.create) {
            throw CrudException("Create action is not allowed for resource $resourceName")
        }
        definition.clazz as KClass<BaseCrudEntity<Serializable>>
        val typeToken = TypeToken.getParameterized(List::class.java, definition.effectiveCreateRoClass()).type
        val list = Gson().fromJson(body, typeToken) as List<BaseRO<*>>

        val successList = mutableSetOf<BaseRO<*>>()
        val failureList = mutableListOf<ManyFailedReason<BaseRO<*>>>()
        for (item in list) {
            try {
                val result = crudHandler.createFrom(item, definition.clazz.java, definition.effectiveShowRoClass()).execute()
                successList.add(result)
            } catch (e: Exception) {
                failureList.add(ManyFailedReason(item, e.message ?: "Unknown error"))
            }
        }
        return ManyCrudResult(successList, failureList)
    }

    override fun update(resourceName: String, id: Serializable, body: String): Any? {
        val definition = getCrudControllerDefinition(resourceName)
        if (!definition.annotation.actions.update) {
            throw CrudException("Update action is not allowed for resource $resourceName")
        }
        definition.clazz as KClass<BaseCrudEntity<Serializable>>
        val roClazz = definition.effectiveUpdateRoClass()
        return crudHandler.updateFrom(id, GSON.fromJson(body, roClazz), definition.clazz.java, definition.effectiveShowRoClass()).execute()
    }

    override fun updateMany(resourceName: String, body: String): ManyCrudResult<out BaseRO<*>, out BaseRO<*>> {
        val definition = getCrudControllerDefinition(resourceName)
        if (!definition.annotation.actions.update) {
            throw CrudException("Update action is not allowed for resource $resourceName")
        }
        definition.clazz as KClass<BaseCrudEntity<Serializable>>
        val typeToken = TypeToken.getParameterized(List::class.java, definition.effectiveUpdateRoClass()).type
        val list = Gson().fromJson(body, typeToken) as List<BaseRO<*>>

        val successList = mutableSetOf<BaseRO<*>>()
        val failureList = mutableListOf<ManyFailedReason<BaseRO<*>>>()
        for (item in list) {
            try {
                val result = crudHandler.updateFrom(item.id as Serializable, item, definition.clazz.java, definition.effectiveShowRoClass()).execute()
                successList.add(result)
            } catch (e: Exception) {
                failureList.add(ManyFailedReason(item, e.message ?: "Unknown error"))
            }
        }
        return ManyCrudResult(successList, failureList)
    }

    private fun CrudControllerDefinition.effectiveMainRoClass(): Class<out BaseRO<*>> {
        return if (annotation.roMapping.mainRoClass != BaseRO::class) {
            annotation.roMapping.mainRoClass.java
        } else {
            throw IllegalArgumentException("No roMapping.mainRoClass specified for resource ${annotation.resourceName}")
        }
    }

    private fun CrudControllerDefinition.effectiveShowRoClass(): Class<out BaseRO<*>> {
        return if (annotation.roMapping.showRoClass != BaseRO::class) {
            annotation.roMapping.showRoClass.java
        } else {
            effectiveMainRoClass()
        }
    }

    private fun CrudControllerDefinition.effectiveIndexRoClass(): Class<out BaseRO<*>> {
        return if (annotation.roMapping.indexRoClass != BaseRO::class) {
            annotation.roMapping.indexRoClass.java
        } else {
            effectiveMainRoClass()
        }
    }

    private fun CrudControllerDefinition.effectiveUpdateRoClass(): Class<out BaseRO<*>> {
        return if (annotation.roMapping.updateRoClass != BaseRO::class) {
            annotation.roMapping.updateRoClass.java
        } else {
            effectiveMainRoClass()
        }
    }

    private fun CrudControllerDefinition.effectiveCreateRoClass(): Class<out BaseRO<*>> {
        return if (annotation.roMapping.createRoClass != BaseRO::class) {
            annotation.roMapping.createRoClass.java
        } else {
            effectiveMainRoClass()
        }
    }

    private fun getCrudControllerDefinition(resourceName: String): CrudControllerDefinition {
        return crudControllerDefinitionMap[resourceName] ?: throw IllegalArgumentException("No crud controller definition found for resource $resourceName")
    }

    companion object {
        private val GSON = Gson()
    }
}