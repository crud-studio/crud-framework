package studio.crud.crudframework.crud.policy

import studio.crud.crudframework.model.PersistentEntity
import studio.crud.crudframework.modelfilter.FilterField
import studio.crud.crudframework.modelfilter.dsl.and
import studio.crud.crudframework.util.filtersMatch
import java.security.Principal

class PolicyVerb<RootType : PersistentEntity>(
        val policyFilterFieldSuppliers: List<PolicyFilterFieldSupplier>,
        val policyConditions: List<PolicyCondition>
) {
    fun matches(entity: RootType, principal: Principal?): Boolean {
        return filtersMatch(entity, principal) && conditionsMatch(principal)
    }

    fun filtersMatch(entity: RootType, principal: Principal?): Boolean {
        return getFilterFields(principal).all { it.filtersMatch(entity) }
    }

    fun conditionsMatch(principal: Principal?): Boolean {
        return policyConditions.all { it(principal) }
    }

    fun getFilterFields(principal: Principal?): List<FilterField> {
        if (!conditionsMatch(principal)) {
            return listOf(
                and<RootType> { noop() }
            )
        }
        return policyFilterFieldSuppliers.flatMap { it(principal) }
    }
}