package studio.crud.crudframework.modelfilter.dsl

import studio.crud.crudframework.model.PersistentEntity
import studio.crud.crudframework.modelfilter.DynamicModelFilter
import studio.crud.crudframework.modelfilter.FilterField
import studio.crud.crudframework.modelfilter.enums.FilterFieldOperation

fun <RootType : PersistentEntity> filter(setup: ModelFilterBuilder<RootType>.() -> Unit): DynamicModelFilter {
    val modelFilterBuilder = ModelFilterBuilder<RootType>()
    setup(modelFilterBuilder)
    return modelFilterBuilder.build()
}

fun <RootType : PersistentEntity> where(setup: FilterFieldsBuilder<RootType>.() -> Unit): DynamicModelFilter {
    val filterFieldsBuilder = FilterFieldsBuilder<RootType>()
    setup(filterFieldsBuilder)
    return DynamicModelFilter(filterFieldsBuilder.build().toMutableList())
}

fun <RootType : PersistentEntity> and(setup: FilterFieldsBuilder<RootType>.() -> Unit): FilterField {
    val filterFieldsBuilder = FilterFieldsBuilder<RootType>()
    setup(filterFieldsBuilder)
    val filter = DynamicModelFilter(filterFieldsBuilder.build().toMutableList())

    return FilterField().apply {
        operation = FilterFieldOperation.And
        children = filter.filterFields
    }
}

fun <RootType : PersistentEntity> or(setup: FilterFieldsBuilder<RootType>.() -> Unit): FilterField {
    val filterFieldsBuilder = FilterFieldsBuilder<RootType>()
    setup(filterFieldsBuilder)
    val filter = DynamicModelFilter(filterFieldsBuilder.build().toMutableList())

    return FilterField().apply {
        operation = FilterFieldOperation.Or
        children = filter.filterFields
    }
}

fun <RootType : PersistentEntity> not(setup: FilterFieldsBuilder<RootType>.() -> Unit): FilterField {
    val filterFieldsBuilder = FilterFieldsBuilder<RootType>()
    setup(filterFieldsBuilder)
    val filter = DynamicModelFilter(filterFieldsBuilder.build().toMutableList())

    return FilterField().apply {
        operation = FilterFieldOperation.Not
        children = filter.filterFields
    }
}