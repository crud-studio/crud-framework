package studio.crud.crudframework.crud.policy

import studio.crud.crudframework.model.PersistentEntity
import studio.crud.crudframework.modelfilter.dsl.FilterFieldsBuilder
import java.security.Principal

class PolicyVerbBuilder<RootType : PersistentEntity> {

    private var policyFilterFieldSuppliers = mutableListOf<PolicyFilterFieldSupplier>()
    private var policyConditions = mutableListOf<PolicyCondition>()

    fun filter(block: FilterFieldsBuilder<RootType>.(Principal) -> Unit) {
        policyFilterFieldSuppliers += { principal ->
            val filterFieldsBuilder = FilterFieldsBuilder<RootType>()
            filterFieldsBuilder.block(principal)
            filterFieldsBuilder.build()
        }
    }

    fun condition(policyCondition: PolicyCondition) {
        this.policyConditions += policyCondition
    }

    fun build(): PolicyVerb<RootType> {
        return PolicyVerb(policyFilterFieldSuppliers, policyConditions)
    }
}