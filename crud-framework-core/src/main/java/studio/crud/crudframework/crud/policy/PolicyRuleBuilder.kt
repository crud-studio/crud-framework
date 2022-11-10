package studio.crud.crudframework.crud.policy

import studio.crud.crudframework.crud.policy.PolicyElementLocation.Companion.toPolicyElementLocation
import studio.crud.crudframework.model.PersistentEntity
import studio.crud.crudframework.modelfilter.dsl.FilterFieldsBuilder
import java.security.Principal

class PolicyRuleBuilder<RootType : PersistentEntity>(private val name: String?, private val location: PolicyElementLocation) {
    private var filterFieldSuppliers = mutableListOf<PolicyFilterFieldSupplier>()
    private var preConditions = mutableListOf<PolicyPreCondition>()
    private var postConditions = mutableListOf<PolicyPostCondition<RootType>>()

    fun filter(block: FilterFieldsBuilder<RootType>.(Principal?) -> Unit) {
        filterFieldSuppliers += { principal ->
            val filterFieldsBuilder = FilterFieldsBuilder<RootType>()
            filterFieldsBuilder.block(principal)
            filterFieldsBuilder.build()
        }
    }

    fun preCondition(name: String? = null, supplier: PolicyPreConditionSupplier) {
        this.preConditions += PolicyPreCondition(name ?: DEFAULT_PRE_CONDITION_NAME, Thread.currentThread().stackTrace[2].toPolicyElementLocation(), supplier)
    }

    fun postCondition(name: String? = null, supplier: PolicyPostConditionSupplier<RootType>) {
        this.postConditions += PolicyPostCondition(name ?: DEFAULT_POST_CONDITION_NAME, Thread.currentThread().stackTrace[2].toPolicyElementLocation(), supplier)
    }

    fun build(): PolicyRule<RootType> {
        return PolicyRule(name ?: DEFAULT_RULE_NAME, location, filterFieldSuppliers, preConditions, postConditions)
    }

    companion object {
        const val DEFAULT_RULE_NAME = "unnamed rule"
        const val DEFAULT_PRE_CONDITION_NAME = "unnamed pre condition"
        const val DEFAULT_POST_CONDITION_NAME = "unnamed post condition"
    }
}