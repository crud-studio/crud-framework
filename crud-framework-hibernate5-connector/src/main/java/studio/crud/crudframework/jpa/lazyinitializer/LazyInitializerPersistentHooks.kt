package studio.crud.crudframework.jpa.lazyinitializer

import org.hibernate.Hibernate
import studio.crud.crudframework.crud.hooks.interfaces.CreateFromHooks
import studio.crud.crudframework.crud.hooks.interfaces.CreateHooks
import studio.crud.crudframework.crud.hooks.interfaces.IndexHooks
import studio.crud.crudframework.crud.hooks.interfaces.ShowByHooks
import studio.crud.crudframework.crud.hooks.interfaces.ShowHooks
import studio.crud.crudframework.crud.hooks.interfaces.UpdateFromHooks
import studio.crud.crudframework.crud.hooks.interfaces.UpdateHooks
import studio.crud.crudframework.jpa.lazyinitializer.annotation.InitializeLazyOn
import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.crudframework.modelfilter.DynamicModelFilter
import studio.crud.crudframework.ro.PagingDTO
import studio.crud.crudframework.utils.utils.ReflectionUtils

class LazyInitializerPersistentHooks :
    ShowHooks<Long, BaseCrudEntity<Long>>,
    ShowByHooks<Long, BaseCrudEntity<Long>>,
    IndexHooks<Long, BaseCrudEntity<Long>>,
    UpdateHooks<Long, BaseCrudEntity<Long>>,
    UpdateFromHooks<Long, BaseCrudEntity<Long>>,
    CreateHooks<Long, BaseCrudEntity<Long>>,
    CreateFromHooks<Long, BaseCrudEntity<Long>> {

    override fun onShow(entity: BaseCrudEntity<Long>?) {
        entity ?: return
        initializeLazyFields(entity) { it.show }
    }

    override fun onCreateFrom(entity: BaseCrudEntity<Long>, ro: Any) {
        initializeLazyFields(entity) { it.createFrom }
    }

    override fun onCreate(entity: BaseCrudEntity<Long>) {
        initializeLazyFields(entity) { it.create }
    }

    override fun onIndex(filter: DynamicModelFilter, result: PagingDTO<BaseCrudEntity<Long>>) {
        result.data ?: return
        for (entity in result.data) {
            initializeLazyFields(entity) { it.index }
        }
    }

    override fun onShowBy(entity: BaseCrudEntity<Long>?) {
        entity ?: return
        initializeLazyFields(entity) { it.showBy }
    }

    override fun onUpdateFrom(entity: BaseCrudEntity<Long>, ro: Any) {
        initializeLazyFields(entity) { it.updateFrom }
    }

    override fun onUpdate(entity: BaseCrudEntity<Long>) {
        initializeLazyFields(entity) { it.update }
    }

    private fun initializeLazyFields(entity: BaseCrudEntity<Long>, condition: (annotation: InitializeLazyOn) -> Boolean) {
        ReflectionUtils.doWithFields(entity::class.java) {
            val annotation = it.getDeclaredAnnotation(ANNOTATION_TYPE) ?: return@doWithFields
            if (condition(annotation)) {
                ReflectionUtils.makeAccessible(it)
                Hibernate.initialize(it.get(entity))
            }
        }
    }

    companion object {
        private val ANNOTATION_TYPE = InitializeLazyOn::class.java
    }
}