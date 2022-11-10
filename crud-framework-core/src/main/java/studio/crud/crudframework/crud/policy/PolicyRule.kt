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
    val preConditions: List<PolicyPreCondition>,
    val postConditions: List<PolicyPostCondition<RootType>>
) {
    fun filtersMatch(entity: RootType, principal: Principal?): Boolean {
        return getFilterFields(principal).all { it.filtersMatch(entity) }
    }

    fun evaluatePreConditions(principal: Principal?): Result<RootType> {
        val conditionResults = preConditions.map {
            val result = it.supplier(principal)
            PolicyPreCondition.Result(result, it)
        }
        return Result(
            conditionResults.all { it.success },
            this,
            conditionResults
        )
    }

    fun evaluatePostConditions(entity: RootType, principal: Principal?): Result<RootType> {
        val postConditionResults = postConditions.map {
            val result = it.supplier(entity, principal)
            PolicyPostCondition.Result(result, it)
        }
        return Result(
            postConditionResults.all { it.success },
            this,
            emptyList(),
            postConditionResults
        )
    }

    fun getFilterFields(principal: Principal?): List<FilterField> {
        if (!evaluatePreConditions(principal).success) {
            return listOf(
                and<RootType> { noop() }
            )
        }
        return policyFilterFieldSuppliers.flatMap { it(principal) }
    }

    data class Result<RootType : PersistentEntity>(
        val success: Boolean,
        val rule: PolicyRule<RootType>,
        val preConditionResults: List<PolicyPreCondition.Result>,
        val postConditionResults: List<PolicyPostCondition.Result<RootType>> = listOf()
    )
}