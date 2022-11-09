package studio.crud.crudframework.crud.policy

import studio.crud.crudframework.model.PersistentEntity
import studio.crud.crudframework.modelfilter.FilterField
import studio.crud.crudframework.modelfilter.dsl.and
import studio.crud.crudframework.util.filtersMatch
import java.security.Principal

class PolicyRule<RootType : PersistentEntity>(
    val name: String,
    val location: PolicyElementLocation,
    val policyFilterFieldSuppliers: List<PolicyFilterFieldSupplier>,
    val policyConditions: List<PolicyCondition>,
) {
    fun filtersMatch(entity: RootType, principal: Principal?): Boolean {
        return getFilterFields(principal).all { it.filtersMatch(entity) }
    }

    fun evaluateConditions(principal: Principal?): Result<RootType> {
        val conditionResults = policyConditions.map {
            val result = it.supplier(principal)
            PolicyCondition.Result(result, it)
        }
        return Result(
            conditionResults.all { it.success },
            this,
            conditionResults
        )
    }

    fun getFilterFields(principal: Principal?): List<FilterField> {
        if (!evaluateConditions(principal).success) {
            return listOf(
                and<RootType> { noop() }
            )
        }
        return policyFilterFieldSuppliers.flatMap { it(principal) }
    }

    data class Result<RootType : PersistentEntity>(
        val success: Boolean,
        val rule: PolicyRule<RootType>,
        val conditionResults: List<PolicyCondition.Result>
    )
}