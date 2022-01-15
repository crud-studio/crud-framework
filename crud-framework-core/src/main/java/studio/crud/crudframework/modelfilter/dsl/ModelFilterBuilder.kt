package studio.crud.crudframework.modelfilter.dsl

import studio.crud.crudframework.modelfilter.DynamicModelFilter
import studio.crud.crudframework.modelfilter.FilterField
import studio.crud.crudframework.modelfilter.OrderDTO
import studio.crud.crudframework.modelfilter.dsl.annotation.FilterFieldDsl


@FilterFieldDsl
class ModelFilterBuilder(
        var orders: MutableSet<OrderDTO> = mutableSetOf(),
        var start: Int = 0,
        var limit: Int = 10000,
        var filterFields: MutableList<FilterField> = mutableListOf()
) {


    fun where(setup: FilterFieldsBuilder.() -> Unit) {
        val filterFieldsBuilder = FilterFieldsBuilder()
        filterFieldsBuilder.setup()
        this.filterFields.addAll(filterFieldsBuilder.build())
    }

    fun order(setup: OrderBuilder.() -> Unit) {
        val orderBuilder = OrderBuilder()
        orderBuilder.setup()
        val (orderBy, orderDesc) = orderBuilder.build()
        this.orders.add(OrderDTO(orderBy, orderDesc))
    }

    fun build(): DynamicModelFilter {
        return DynamicModelFilter(
            start,
            limit,
            orders,
            filterFields
        )
    }
}