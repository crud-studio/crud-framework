package studio.crud.crudframework.modelfilter.dsl

import studio.crud.crudframework.modelfilter.FilterField
import studio.crud.crudframework.modelfilter.enums.FilterFieldDataType
import studio.crud.crudframework.modelfilter.enums.FilterFieldOperation

class BetweenBuilder<T>(val fieldName: String, val source: T, val type: FilterFieldDataType) {

    infix fun build(target: T): FilterField {
        return FilterField(fieldName, FilterFieldOperation.Between, type, listOf(source, target))
    }

}