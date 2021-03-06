package studio.crud.crudframework.crud.model

import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.core.annotation.AnnotationUtils
import studio.crud.crudframework.crud.annotation.CachedBy
import studio.crud.crudframework.crud.annotation.CrudEntity
import studio.crud.crudframework.crud.annotation.DeleteColumn
import studio.crud.crudframework.crud.annotation.Deleteable
import studio.crud.crudframework.crud.annotation.Immutable
import studio.crud.crudframework.crud.annotation.PersistCopyOnFetch
import studio.crud.crudframework.crud.annotation.WithHooks
import studio.crud.crudframework.crud.cache.CrudCacheOptions
import studio.crud.crudframework.crud.handler.CrudDao
import studio.crud.crudframework.crud.hooks.interfaces.CRUDHooks
import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.crudframework.model.PersistentEntity
import studio.crud.crudframework.utils.utils.ReflectionUtils
import studio.crud.crudframework.utils.utils.getGenericClass
import java.lang.reflect.Field
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses

class EntityMetadataDTO {

    val simpleName: String

    val deleteField: Field?

    val deleteableType: DeleteableType

    val cacheMetadata: EntityCacheMetadata?

    val immutable: Boolean

    val alwaysPersistCopy: Boolean

    val hookTypesFromAnnotations: MutableSet<Class<CRUDHooks<*, *>>> = mutableSetOf()

    val hooksFromAnnotations: MutableSet<CRUDHooks<*, *>> = mutableSetOf()

    val fields: MutableMap<String, Field> = mutableMapOf()

    val daoClazz: Class<out CrudDao>

    constructor(entityClazz: KClass<out BaseCrudEntity<*>>) : this(entityClazz.java)

    constructor(entityClazz: Class<out BaseCrudEntity<*>>) {
        deleteField = getEntityDeleteField(entityClazz)
        deleteableType = getEntityDeleteableType(entityClazz)
        cacheMetadata = getEntityCacheMetadata(entityClazz)
        immutable = isEntityImmutable(entityClazz)
        alwaysPersistCopy = shouldAlwaysPersistCopy(entityClazz)
        collectHookAnnotations(entityClazz)
        daoClazz = getEntityDao(entityClazz)
        getFields(entityClazz)
        simpleName = entityClazz.simpleName
    }

    private fun getFields(entityClazz: Class<out PersistentEntity>, prefix: String? = null, currentDepth: Int = 0) {
        val effectivePrefix: String
        if (prefix.isNullOrBlank()) {
            effectivePrefix = ""
        } else {
            effectivePrefix = prefix.replace(".", "/") + "."
        }

        ReflectionUtils.getFields(entityClazz).forEach {
            if (it.name == "copy" && it.type == BaseCrudEntity::class.java) {
                return
            }

            var fieldClazz = it.type
            if (Collection::class.java.isAssignableFrom(fieldClazz)) {
                val potentialFieldClazz = it.getGenericClass(0)
                if (potentialFieldClazz != null && PersistentEntity::class.java.isAssignableFrom(potentialFieldClazz)) {
                    fieldClazz = potentialFieldClazz
                }
            }

            if (PersistentEntity::class.java.isAssignableFrom(fieldClazz) && currentDepth < MAX_FILTERFIELD_DEPTH) {
                getFields(fieldClazz as Class<out PersistentEntity>, effectivePrefix + it.name, currentDepth + 1)
            } else {
                fields[effectivePrefix + it.name] = it
            }
        }
    }

    private fun collectHookAnnotations(entityClazz: Class<out BaseCrudEntity<*>>) {
        val hookAnnotations = mutableSetOf<WithHooks>()
        val annotations = entityClazz.declaredAnnotations + entityClazz.kotlin.allSuperclasses
            .flatMap { it.java.declaredAnnotations.toList() }

        // The first search targets the WithHooks.List annotation, which is the repeatable container for WithHooks
        annotations
            .mapNotNull {
                AnnotationUtils.findAnnotation(AnnotatedElementUtils.forAnnotations(it), WithHooks.List::class.java)
            }
            .filter {
                it.value.isNotEmpty()
            }
            .flatMapTo(hookAnnotations) { it.value.toList() }

        // We run this second search because a nested, single WithHooks annotation in a Kotlin file does not register as WithHooks.List
        annotations
            .mapNotNullTo(hookAnnotations) { AnnotationUtils.findAnnotation(AnnotatedElementUtils.forAnnotations(it), WithHooks::class.java) }

        if (hookAnnotations.isNotEmpty()) {
            for (hookAnnotation in hookAnnotations) {
                val hooksArray = AnnotationUtils.getAnnotationAttributes(hookAnnotation)["hooks"] as Array<Class<CRUDHooks<*, *>>>
                if (hooksArray.isNotEmpty()) {
                    hookTypesFromAnnotations.addAll(hooksArray.toList())
                }
            }
        }
    }

    private fun getEntityDao(clazz: Class<out BaseCrudEntity<*>>): Class<out CrudDao> {
        val crudEntity = AnnotationUtils.findAnnotation(clazz, CrudEntity::class.java) ?: error("@CrudEntity not found on entity ${clazz.name}")
        return crudEntity.dao.java
    }

    private fun getEntityCacheMetadata(clazz: Class<out BaseCrudEntity<*>>): EntityCacheMetadata? {
        val cachedBy = clazz.getDeclaredAnnotation(CachedBy::class.java) ?: return null
        fun Long.nullIfMinusOne(): Long? = if (this == -1L) {
            null
        } else {
            this
        }

        return EntityCacheMetadata(
            cachedBy.value,
            cachedBy.createIfMissing,
            CrudCacheOptions(
                cachedBy.timeToLiveSeconds.nullIfMinusOne(),
                cachedBy.timeToIdleSeconds.nullIfMinusOne(),
                cachedBy.maxEntries.nullIfMinusOne()
            )
        )
    }

    private fun getEntityDeleteableType(clazz: Class<out BaseCrudEntity<*>>): DeleteableType {
        val deleteable = clazz.getDeclaredAnnotation(Deleteable::class.java)
        return when {
            deleteable == null -> DeleteableType.None
            deleteable.softDelete -> DeleteableType.Soft
            else -> DeleteableType.Hard
        }
    }

    private fun getEntityDeleteField(clazz: Class<out BaseCrudEntity<*>>): Field? {
        val fields = ReflectionUtils.getFields(clazz)

        var deleteField: Field? = null
        for (field in fields) {
            if (field.getDeclaredAnnotation(DeleteColumn::class.java) != null) {
                deleteField = field
            }
        }

        return deleteField
    }

    private fun isEntityImmutable(clazz: Class<out BaseCrudEntity<*>>): Boolean {
        return clazz.getDeclaredAnnotation(Immutable::class.java) != null
    }

    private fun shouldAlwaysPersistCopy(clazz: Class<out BaseCrudEntity<*>>): Boolean {
        return clazz.getDeclaredAnnotation(PersistCopyOnFetch::class.java) != null
    }

    enum class DeleteableType {
        None, Soft, Hard
    }

    companion object {
        private const val MAX_FILTERFIELD_DEPTH = 4
    }
}

data class EntityCacheMetadata(
    val name: String,
    val createIfMissing: Boolean,
    val options: CrudCacheOptions
)