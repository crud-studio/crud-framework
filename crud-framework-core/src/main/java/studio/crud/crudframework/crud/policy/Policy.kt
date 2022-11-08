package studio.crud.crudframework.crud.policy

import studio.crud.crudframework.model.PersistentEntity
import studio.crud.crudframework.modelfilter.FilterField
import java.security.Principal

class Policy<RootType : PersistentEntity>(
    val clazz: Class<RootType>,
    val canAccessVerbs: List<PolicyVerb<RootType>>,
    val canUpdateVerbs: List<PolicyVerb<RootType>>,
    val canDeleteVerbs: List<PolicyVerb<RootType>>,
    val canCreateVerbs: List<PolicyVerb<RootType>>
) {
    fun matchesCanAccess(entity: RootType, principal: Principal?): Boolean {
        return canAccessVerbs.all { it.matches(entity, principal) }
    }

    fun getCanAccessFilterFields(principal: Principal?): List<FilterField> {
        return canAccessVerbs.flatMap { it.getFilterFields(principal) }
    }

    fun matchesCanUpdate(entity: RootType, principal: Principal?): Boolean {
        return matchesCanAccess(entity, principal) && canUpdateVerbs.all { it.matches(entity, principal) }
    }

    fun getCanUpdateFilterFields(principal: Principal?): List<FilterField> {
        return getCanAccessFilterFields(principal) + canUpdateVerbs.flatMap { it.getFilterFields(principal) }
    }

    fun matchesCanDelete(entity: RootType, principal: Principal?): Boolean {
        return matchesCanAccess(entity, principal) && canDeleteVerbs.all { it.matches(entity, principal) }
    }

    fun getCanDeleteFilterFields(principal: Principal?): List<FilterField> {
        return getCanAccessFilterFields(principal) + canDeleteVerbs.flatMap { it.getFilterFields(principal) }
    }

    fun matchesCanCreate(principal: Principal?): Boolean {
        return canCreateVerbs.all { it.conditionsMatch(principal) }
    }

    companion object {
        fun <RootType : PersistentEntity> Collection<Policy<RootType>>.matchesCanAccess(entity: RootType, principal: Principal?): Boolean {
            return this.all { it.matchesCanAccess(entity, principal) }
        }

        fun <RootType : PersistentEntity> Collection<Policy<RootType>>.getCanAccessFilterFields(principal: Principal?): List<FilterField> {
            return this.flatMap { it.getCanAccessFilterFields(principal) }
        }

        fun <RootType : PersistentEntity> Collection<Policy<RootType>>.matchesCanUpdate(entity: RootType, principal: Principal?): Boolean {
            return this.all { it.matchesCanUpdate(entity, principal) }
        }

        fun <RootType : PersistentEntity> Collection<Policy<RootType>>.getCanUpdateFilterFields(principal: Principal?): List<FilterField> {
            return this.flatMap { it.getCanUpdateFilterFields(principal) }
        }

        fun <RootType : PersistentEntity> Collection<Policy<RootType>>.matchesCanDelete(entity: RootType, principal: Principal?): Boolean {
            return this.all { it.matchesCanDelete(entity, principal) }
        }

        fun <RootType : PersistentEntity> Collection<Policy<RootType>>.getCanDeleteFilterFields(principal: Principal?): List<FilterField> {
            return this.flatMap { it.getCanDeleteFilterFields(principal) }
        }

        fun <RootType : PersistentEntity> Collection<Policy<RootType>>.matchesCanCreate(principal: Principal?): Boolean {
            return this.all { it.matchesCanCreate(principal) }
        }
    }
}

inline fun <reified RootType : PersistentEntity> policy(block: PolicyBuilder<RootType>.() -> Unit): Policy<RootType> {
    val policyBuilder = PolicyBuilder(RootType::class.java)
    policyBuilder.block()
    return policyBuilder.build()
}