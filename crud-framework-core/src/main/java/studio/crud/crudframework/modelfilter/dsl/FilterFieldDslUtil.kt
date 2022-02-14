package studio.crud.crudframework.modelfilter.dsl

import studio.crud.crudframework.modelfilter.DynamicModelFilter
import studio.crud.crudframework.modelfilter.FilterField
import studio.crud.crudframework.modelfilter.enums.FilterFieldOperation


fun filter(setup: ModelFilterBuilder.() -> Unit): DynamicModelFilter {
    val modelFilterBuilder = ModelFilterBuilder()
    setup(modelFilterBuilder)
    return modelFilterBuilder.build()
}

fun where(setup: FilterFieldsBuilder.() -> Unit): DynamicModelFilter {
    val filterFieldsBuilder = FilterFieldsBuilder()
    setup(filterFieldsBuilder)
    return DynamicModelFilter(filterFieldsBuilder.build().toMutableList())
}

fun and(setup: FilterFieldsBuilder.() -> Unit): FilterField {
    val filterFieldsBuilder = FilterFieldsBuilder()
    setup(filterFieldsBuilder)
    val filter = DynamicModelFilter(filterFieldsBuilder.build().toMutableList())

    return FilterField().apply {
        operation = FilterFieldOperation.And
        children = filter.filterFields
    }
}

fun or(setup: FilterFieldsBuilder.() -> Unit): FilterField {
    val filterFieldsBuilder = FilterFieldsBuilder()
    setup(filterFieldsBuilder)
    val filter = DynamicModelFilter(filterFieldsBuilder.build().toMutableList())

    return FilterField().apply {
        operation = FilterFieldOperation.Or
        children = filter.filterFields
    }
}

fun not(setup: FilterFieldsBuilder.() -> Unit): FilterField {
    val filterFieldsBuilder = FilterFieldsBuilder()
    setup(filterFieldsBuilder)
    val filter = DynamicModelFilter(filterFieldsBuilder.build().toMutableList())

    return FilterField().apply {
        operation = FilterFieldOperation.Not
        children = filter.filterFields
    }
}