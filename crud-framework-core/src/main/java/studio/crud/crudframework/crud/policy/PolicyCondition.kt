package studio.crud.crudframework.crud.policy

import java.security.Principal

typealias PolicyConditionSupplier = (Principal?) -> Boolean

data class PolicyCondition(
    val name: String,
    val location: PolicyElementLocation,
    val supplier: PolicyConditionSupplier
) {
    data class Result(
        val success: Boolean,
        val condition: PolicyCondition
    )
}