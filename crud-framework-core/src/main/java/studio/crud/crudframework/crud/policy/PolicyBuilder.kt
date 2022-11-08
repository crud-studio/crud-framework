package studio.crud.crudframework.crud.policy

import studio.crud.crudframework.model.PersistentEntity

@PolicyDsl
class PolicyBuilder<RootType : PersistentEntity>(private val clazz: Class<RootType>) {
    private val canAccessVerbs = mutableListOf<PolicyVerb<RootType>>()
    private val canUpdateVerbs = mutableListOf<PolicyVerb<RootType>>()
    private val canDeleteVerbs = mutableListOf<PolicyVerb<RootType>>()
    private val canCreateVerbs = mutableListOf<PolicyVerb<RootType>>()

    fun canAccess(block: PolicyVerbBuilder<RootType>.() -> Unit) {
        val policyVerbBuilder = PolicyVerbBuilder<RootType>()
        policyVerbBuilder.block()
        val action = policyVerbBuilder.build()
        canAccessVerbs.add(action)
    }

    fun canUpdate(block: PolicyVerbBuilder<RootType>.() -> Unit) {
        val policyVerbBuilder = PolicyVerbBuilder<RootType>()
        policyVerbBuilder.block()
        val action = policyVerbBuilder.build()
        canUpdateVerbs.add(action)
    }

    fun canDelete(block: PolicyVerbBuilder<RootType>.() -> Unit) {
        val policyVerbBuilder = PolicyVerbBuilder<RootType>()
        policyVerbBuilder.block()
        val action = policyVerbBuilder.build()
        canDeleteVerbs.add(action)
    }

    fun canCreate(block: PolicyVerbBuilder<RootType>.() -> Unit) {
        val policyVerbBuilder = PolicyVerbBuilder<RootType>()
        policyVerbBuilder.block()
        val action = policyVerbBuilder.build()
        canCreateVerbs.add(action)
    }

    fun build(): Policy<RootType> {
        return Policy(
            clazz, canAccessVerbs, canUpdateVerbs, canDeleteVerbs, canCreateVerbs
        )
    }
}

