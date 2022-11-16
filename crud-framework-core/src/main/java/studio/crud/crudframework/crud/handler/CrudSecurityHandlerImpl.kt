package studio.crud.crudframework.crud.handler

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.ObjectProvider
import studio.crud.crudframework.crud.policy.Policy
import studio.crud.crudframework.crud.policy.PolicyRuleType
import studio.crud.crudframework.crud.security.PrincipalProvider
import studio.crud.crudframework.model.PersistentEntity
import studio.crud.crudframework.modelfilter.FilterField
import java.security.Principal

internal class CrudSecurityHandlerImpl(
        private val policies: ObjectProvider<Policy<PersistentEntity>>,
        private val principalProvider: ObjectProvider<PrincipalProvider>
) : CrudSecurityHandler, InitializingBean {
    private val policyMap = mutableMapOf<Class<*>, MutableList<Policy<PersistentEntity>>>()

    override fun afterPropertiesSet() {
        for (policy in policies.orderedStream()) {
            policyMap.computeIfAbsent(policy.clazz) { mutableListOf() }
                    .add(policy)
        }
    }

    override fun getPolicies(clazz: Class<out PersistentEntity>): List<Policy<PersistentEntity>> {
        return policyMap[clazz] ?: emptyList()
    }

    override fun getFilterFields(clazz: Class<out PersistentEntity>): List<FilterField> {
        return getPolicies(clazz).flatMap { it.getFilterFields(principalProvider.getObject().getPrincipal()) }
    }

    override fun evaluatePreRules(type: PolicyRuleType, clazz: Class<out PersistentEntity>): MultiPolicyResult {
        val policies = getPolicies(clazz)
        val results = policies.map { it.evaluatePreRules(type, principalProvider.ifAvailable?.getPrincipal()) }
        return MultiPolicyResult(
            clazz,
            results.all { it.success },
            results
        )
    }

    override fun evaluatePostRules(entity: PersistentEntity, type: PolicyRuleType, clazz: Class<out PersistentEntity>): MultiPolicyResult {
        val results = getPolicies(clazz).map { it.evaluatePostRules(entity, type, principalProvider.getObject().getPrincipal()) }
        return MultiPolicyResult(clazz, results.all { it.success }, results)
    }

    private fun getPrincipal(): Principal? {
        return principalProvider.ifAvailable?.getPrincipal()
    }
}