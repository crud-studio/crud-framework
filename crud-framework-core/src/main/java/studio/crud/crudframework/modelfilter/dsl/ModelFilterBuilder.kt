package studio.crud.crudframework.modelfilter.dsl

import studio.crud.crudframework.model.PersistentEntity
import studio.crud.crudframework.modelfilter.DynamicModelFilter
import studio.crud.crudframework.modelfilter.FilterField
import studio.crud.crudframework.modelfilter.OrderDTO
import studio.crud.crudframework.modelfilter.dsl.annotation.FilterFieldDsl

@FilterFieldDsl
class ModelFilterBuilder<RootType : PersistentEntity>(
    var orders: MutableSet<OrderDTO> = mutableSetOf(),
    var start: Int = 0,
    var limit: Int = 10000,
    var filterFields: MutableList<FilterField> = mutableListOf()
) {

    fun where(setup: FilterFieldsBuilder<RootType>.() -> Unit) {
        val filterFieldsBuilder = FilterFieldsBuilder<RootType>()
        filterFieldsBuilder.setup()
        this.filterFields.addAll(filterFieldsBuilder.build())
    }

    fun order(setup: OrderBuilder<RootType>.() -> Unit) {
        val orderBuilder = OrderBuilder<RootType>()
        orderBuilder.setup()
        val dto = orderBuilder.build()
        this.orders.add(dto)
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