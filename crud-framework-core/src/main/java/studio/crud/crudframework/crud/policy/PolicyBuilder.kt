package studio.crud.crudframework.crud.policy

import studio.crud.crudframework.crud.policy.PolicyElementLocation.Companion.toPolicyElementLocation
import studio.crud.crudframework.model.PersistentEntity

@PolicyDsl
class PolicyBuilder<RootType : PersistentEntity>(private val name: String?, private val location: PolicyElementLocation, private val clazz: Class<RootType>) {
    private val canAccessRules = mutableListOf<PolicyRule<RootType>>()
    private val canUpdateRules = mutableListOf<PolicyRule<RootType>>()
    private val canDeleteRules = mutableListOf<PolicyRule<RootType>>()
    private val canCreateRules = mutableListOf<PolicyRule<RootType>>()

    fun canAccess(name: String? = null, block: PolicyRuleBuilder<RootType>.() -> Unit) {
        val policyRuleBuilder = PolicyRuleBuilder<RootType>(name, Thread.currentThread().stackTrace[2].toPolicyElementLocation())
        policyRuleBuilder.block()
        val action = policyRuleBuilder.build()
        canAccessRules.add(action)
    }

    fun canUpdate(name: String? = null, block: PolicyRuleBuilder<RootType>.() -> Unit) {
        val policyRuleBuilder = PolicyRuleBuilder<RootType>(name, Thread.currentThread().stackTrace[2].toPolicyElementLocation())
        policyRuleBuilder.block()
        val action = policyRuleBuilder.build()
        canUpdateRules.add(action)
    }

    fun canDelete(name: String? = null, block: PolicyRuleBuilder<RootType>.() -> Unit) {
        val policyRuleBuilder = PolicyRuleBuilder<RootType>(name, Thread.currentThread().stackTrace[2].toPolicyElementLocation())
        policyRuleBuilder.block()
        val action = policyRuleBuilder.build()
        canDeleteRules.add(action)
    }

    fun canCreate(name: String? = null, block: PolicyRuleBuilder<RootType>.() -> Unit) {
        val policyRuleBuilder = PolicyRuleBuilder<RootType>(name, Thread.currentThread().stackTrace[2].toPolicyElementLocation())
        policyRuleBuilder.block()
        val action = policyRuleBuilder.build()
        canCreateRules.add(action)
    }

    fun build(): Policy<RootType> {
        return Policy(
            name ?: DEFAULT_POLICY_NAME, location, clazz, canAccessRules, canUpdateRules, canDeleteRules, canCreateRules
        )
    }

    companion object {
        const val DEFAULT_POLICY_NAME = "unnamed policy"
    }
}

