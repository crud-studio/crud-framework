package studio.crud.crudframework.crud.handler

import studio.crud.crudframework.crud.policy.Policy
import studio.crud.crudframework.model.PersistentEntity
import java.lang.RuntimeException

data class MultiPolicyResult(
    val success: Boolean,
    val results: List<Policy.Result<out PersistentEntity>>
) {
    fun throwIfFailed() {
        if (!success) {
            throw RuntimeException("Policy evaluation failed") //TODO
        }
    }
}