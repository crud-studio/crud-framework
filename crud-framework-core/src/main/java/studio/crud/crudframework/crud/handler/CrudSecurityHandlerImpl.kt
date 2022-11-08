package studio.crud.crudframework.crud.handler

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.ObjectProvider
import studio.crud.crudframework.crud.policy.Policy
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

    override fun getPolicies(clazz: Class<*>): List<Policy<PersistentEntity>> {
        return policyMap[clazz] ?: emptyList()
    }

    override fun matchesCanAccess(entity: PersistentEntity, clazz: Class<*>): Boolean {
        return getPolicies(clazz).all { it.matchesCanAccess(entity, getPrincipal()) }
    }

    override fun getCanAccessFilterFields(clazz: Class<*>): List<FilterField> {
        return getPolicies(clazz).flatMap { it.getCanAccessFilterFields(getPrincipal()) }
    }

    override fun matchesCanUpdate(entity: PersistentEntity, clazz: Class<*>): Boolean {
        return getPolicies(clazz).all { it.matchesCanUpdate(entity, getPrincipal()) }
    }

    override fun getCanUpdateFilterFields(clazz: Class<*>): List<FilterField> {
        return getPolicies(clazz).flatMap { it.getCanUpdateFilterFields(getPrincipal()) }
    }

    override fun matchesCanDelete(entity: PersistentEntity, clazz: Class<*>): Boolean {
        return getPolicies(clazz).all { it.matchesCanDelete(entity, getPrincipal()) }
    }

    override fun getDeleteFilterFields(clazz: Class<*>): List<FilterField> {
        return getPolicies(clazz).flatMap { it.getCanDeleteFilterFields(getPrincipal()) }
    }

    override fun matchesCanCreate(clazz: Class<*>): Boolean {
        return getPolicies(clazz).all { it.matchesCanCreate(getPrincipal()) }
    }

    private fun getPrincipal(): Principal? {
        return principalProvider.ifAvailable?.getPrincipal()
    }
}
