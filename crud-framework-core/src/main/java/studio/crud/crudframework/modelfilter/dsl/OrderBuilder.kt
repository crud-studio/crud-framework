package studio.crud.crudframework.modelfilter.dsl

import studio.crud.crudframework.modelfilter.dsl.annotation.FilterFieldDsl

@FilterFieldDsl
class OrderBuilder(var by: String = "id", private var desc: Boolean = true) {

    val descending: Unit
        get() {
            desc = true
        }
    val ascending: Unit
        get() {
            desc = false
        }

    fun build() = Pair(by, desc)
}