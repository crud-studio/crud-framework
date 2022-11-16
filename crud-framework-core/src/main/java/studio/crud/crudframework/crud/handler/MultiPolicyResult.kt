package studio.crud.crudframework.crud.handler

import studio.crud.crudframework.crud.policy.Policy
import studio.crud.crudframework.crud.policy.Policy.Result.Companion.distinctRuleTypes
import studio.crud.crudframework.model.PersistentEntity
import java.lang.RuntimeException

data class MultiPolicyResult(
    val clazz: Class<out PersistentEntity>,
    val success: Boolean,
    val results: List<Policy.Result<out PersistentEntity>>
) {
    fun throwIfFailed() {
        if (!success) {
            throw RuntimeException("Policy evaluation failed for [ ${clazz.simpleName} ], failed checks for [ ${results.distinctRuleTypes().joinToString()} ]")
        }
    }
}