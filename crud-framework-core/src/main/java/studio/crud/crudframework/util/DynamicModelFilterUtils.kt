@file:JvmName("DynamicModelFilterUtils")
package studio.crud.crudframework.util

import studio.crud.crudframework.modelfilter.DynamicModelFilter
import studio.crud.crudframework.modelfilter.FilterField
import studio.crud.crudframework.modelfilter.enums.FilterFieldOperation

private fun getPropertyValue(target: Any, string: String): Any? {
    var item: Any? = target
    val parts = string.split(".").toTypedArray()
    for (part in parts) {
        if (item == null) {
            break
        }
        val clazz: Class<*> = item.javaClass
        val field = clazz.getDeclaredField(part)
        field.isAccessible = true
        item = field.get(item)
    }
    return item
}

/**
 * Check if [target] matches all filter fields in [this]
 */
fun DynamicModelFilter.matches(target: Any): Boolean {
    return this.filterFields.all { filterField ->
        return@all filterField.matches(target)
    }
}

/**
 * Check if [target] matches the given filter field
 */
fun FilterField.matches(target: Any): Boolean {
    val actualValue: Any? = if (this.operation.junction) {
        null
    } else {
        getPropertyValue(target, this.fieldName.replace("/", "."))
    }
    when (this.operation) {
        FilterFieldOperation.Equal -> {
            return actualValue == this.value1
        }
        FilterFieldOperation.NotEqual -> {
            return actualValue != this.value1
        }
        FilterFieldOperation.In -> {
            return this.value1 in actualValue as Collection<*>
        }
        FilterFieldOperation.NotIn -> {
            return this.value1 !in actualValue as Collection<*>
        }
        FilterFieldOperation.GreaterThan -> {
            actualValue as Comparable<Any>
            val value = this.value1 as Comparable<Any>
            return actualValue > value
        }
        FilterFieldOperation.GreaterEqual -> {
            actualValue as Comparable<Any>
            val value = this.value1 as Comparable<Any>
            return actualValue >= value
        }
        FilterFieldOperation.LowerThan -> {
            actualValue as Comparable<Any>
            val value = this.value1 as Comparable<Any>
            return actualValue < value
        }
        FilterFieldOperation.LowerEqual -> {
            actualValue as Comparable<Any>
            val value = this.value1 as Comparable<Any>
            return actualValue <= value
        }
        FilterFieldOperation.Between -> {
            actualValue as Comparable<Any>
            val value1 = this.value1 as Comparable<Any>
            val value2 = this.value2 as Comparable<Any>
            return actualValue >= value1 && actualValue < value2
        }
        FilterFieldOperation.Contains -> {
            actualValue as String
            val value = this.value1 as String
            return actualValue.contains(value)
        }
        FilterFieldOperation.IsNull -> {
            return actualValue == null
        }
        FilterFieldOperation.IsNotNull -> {
            return actualValue != null
        }
        FilterFieldOperation.And -> {
            return children.all { it.matches(target) }
        }
        FilterFieldOperation.Or -> {
            return children.any { it.matches(target) }
        }
        FilterFieldOperation.Not -> {
            return children.none { it.matches(target) }
        }
        FilterFieldOperation.ContainsIn -> {
            throw UnsupportedOperationException("Cannot use ContainsIn in FilterField.matches")
        }
        FilterFieldOperation.NotContainsIn -> {
            throw UnsupportedOperationException("Cannot use NotContainsIn in FilterField.matches")
        }
        FilterFieldOperation.StartsWith -> {
            actualValue as String
            val value = this.value1 as String
            return actualValue.startsWith(value)
        }
        FilterFieldOperation.EndsWith -> {
            actualValue as String
            val value = this.value1 as String
            return actualValue.endsWith(value)
        }
        FilterFieldOperation.Noop -> return false
        null -> return false
    }
}