package studio.crud.crudframework.crud.policy

import com.github.tomaslanger.chalk.Chalk
import studio.crud.crudframework.crud.policy.PolicyElementLocation.Companion.toPolicyElementLocation
import studio.crud.crudframework.model.PersistentEntity
import studio.crud.crudframework.modelfilter.FilterField
import java.security.Principal

class Policy<RootType : PersistentEntity>(
    val name: String,
    val location: PolicyElementLocation,
    val clazz: Class<RootType>,
    private val canAccessRules: List<PolicyRule<RootType>>,
    private val canUpdateRules: List<PolicyRule<RootType>>,
    private val canDeleteRules: List<PolicyRule<RootType>>,
    private val canCreateRules: List<PolicyRule<RootType>>
) {
    fun getCanAccessFilterFields(principal: Principal?): List<FilterField> {
        return canAccessRules.flatMap { it.getFilterFields(principal) }
    }

    fun getCanUpdateFilterFields(principal: Principal?): List<FilterField> {
        return getCanAccessFilterFields(principal) + canUpdateRules.flatMap { it.getFilterFields(principal) }
    }

    fun getCanDeleteFilterFields(principal: Principal?): List<FilterField> {
        return getCanAccessFilterFields(principal) + canDeleteRules.flatMap { it.getFilterFields(principal) }
    }

    fun evaluatePreCanAccess(principal: Principal?): Result<RootType> {
        val result = canAccessRules.map { it.evaluatePreConditions(principal) }
        return Result(
            result.all { it.success },
            this,
            result
        )
    }

    fun evaluatePreCanCreate(principal: Principal?): Result<RootType> {
        val result = canCreateRules.map { it.evaluatePreConditions(principal) }
        return evaluatePreCanAccess(principal) + Result(
            result.all { it.success },
            this,
            result
        )
    }

    fun evaluatePreCanUpdate(principal: Principal?): Result<RootType> {
        val result = canUpdateRules.map { it.evaluatePreConditions(principal) }
        return evaluatePreCanAccess(principal) + Result(
            result.all { it.success },
            this,
            result
        )
    }

    fun evaluatePreCanDelete(principal: Principal?): Result<RootType> {
        val result = canDeleteRules.map { it.evaluatePreConditions(principal) }
        return evaluatePreCanAccess(principal) + Result(
            result.all { it.success },
            this,
            result
        )
    }

    fun evaluatePostCanAccess(entity: RootType, principal: Principal?): Result<RootType> {
        val result = canAccessRules.map { it.evaluatePostConditions(entity, principal) }
        return Result(
            result.all { it.success },
            this,
            result
        )
    }

    fun evaluatePostCanCreate(entity: RootType, principal: Principal?): Result<RootType> {
        val result = canCreateRules.map { it.evaluatePostConditions(entity, principal) }
        return evaluatePostCanAccess(entity, principal) + Result(
            result.all { it.success },
            this,
            result
        )
    }

    fun evaluatePostCanUpdate(entity: RootType, principal: Principal?): Result<RootType> {
        val result = canUpdateRules.map { it.evaluatePostConditions(entity, principal) }
        return evaluatePostCanAccess(entity, principal) + Result(
            result.all { it.success },
            this,
            result
        )
    }

    fun evaluatePostCanDelete(entity: RootType, principal: Principal?): Result<RootType> {
        val result = canDeleteRules.map { it.evaluatePostConditions(entity, principal) }
        return evaluatePostCanAccess(entity, principal) + Result(
            result.all { it.success },
            this,
            result
        )
    }

    data class Result<RootType : PersistentEntity>(
        val success: Boolean,
        val policy: Policy<RootType>,
        val ruleResults: List<PolicyRule.Result<RootType>>
    ) {
        operator fun plus(other: Result<RootType>): Result<RootType> {
            return Result(
                success && other.success,
                policy,
                ruleResults + other.ruleResults
            )
        }

        fun outputShortOutcome(): String {
            val sb = StringBuilder()
            sb.appendLine("${Chalk.on(policy.name).bold()} - ${appendSuccess(success)}")
            ruleResults.forEachIndexed { ruleIndex, ruleResult ->
                sb.appendLine("\t${ruleIndex + 1}. ${Chalk.on(ruleResult.rule.name).bold()} - ${appendSuccess(ruleResult.success)}")
                ruleResult.preConditionResults.forEachIndexed { conditionIndex, conditionResult ->
                    sb.appendLine("\t\t${conditionIndex + 1}. ${Chalk.on(conditionResult.condition.name).bold()} - ${appendSuccess(conditionResult.success)}")
                }
                ruleResult.postConditionResults.forEachIndexed { conditionIndex, conditionResult ->
                    sb.appendLine("\t\t${conditionIndex + 1}. ${Chalk.on(conditionResult.condition.name).bold()} - ${appendSuccess(conditionResult.success)}")
                }
            }
            return sb.toString()
        }

        private fun appendSuccess(success: Boolean): String {
            return if(success) {
                Chalk.on("PASSED").bold().green().toString()
            } else {
                Chalk.on("FAILED").bold().red().toString()
            }
        }
    }
}

inline fun <reified RootType : PersistentEntity> policy(
    name: String? = null,
    block: PolicyBuilder<RootType>.() -> Unit
): Policy<RootType> {
    val policyBuilder = PolicyBuilder(name, Thread.currentThread().stackTrace[1].toPolicyElementLocation(), RootType::class.java)
    policyBuilder.block()
    return policyBuilder.build()
}