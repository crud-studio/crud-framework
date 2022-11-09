package studio.crud.crudframework.crud.policy

import studio.crud.crudframework.crud.policy.PolicyElementLocation.Companion.toPolicyElementLocation
import studio.crud.crudframework.model.PersistentEntity
import studio.crud.crudframework.modelfilter.dsl.FilterFieldsBuilder
import java.security.Principal

class PolicyRuleBuilder<RootType : PersistentEntity>(private val name: String?, private val location: PolicyElementLocation) {

    private var policyFilterFieldSuppliers = mutableListOf<PolicyFilterFieldSupplier>()
    private var policyConditions = mutableListOf<PolicyCondition>()

    fun filter(block: FilterFieldsBuilder<RootType>.(Principal?) -> Unit) {
        policyFilterFieldSuppliers += { principal ->
            val filterFieldsBuilder = FilterFieldsBuilder<RootType>()
            filterFieldsBuilder.block(principal)
            filterFieldsBuilder.build()
        }
    }

    fun condition(name: String? = null, supplier: PolicyConditionSupplier) {
        this.policyConditions += PolicyCondition(name ?: DEFAULT_CONDITION_NAME, Thread.currentThread().stackTrace[2].toPolicyElementLocation(), supplier)
    }

    fun build(): PolicyRule<RootType> {
        return PolicyRule(name ?: DEFAULT_RULE_NAME, location, policyFilterFieldSuppliers, policyConditions)
    }

    companion object {
        const val DEFAULT_RULE_NAME = "unnamed rule"
        const val DEFAULT_CONDITION_NAME = "unnamed condition"
    }
}