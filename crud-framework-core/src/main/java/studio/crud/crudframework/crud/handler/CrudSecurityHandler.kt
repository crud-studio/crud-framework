package studio.crud.crudframework.crud.handler

import studio.crud.crudframework.crud.policy.Policy
import studio.crud.crudframework.model.PersistentEntity
import studio.crud.crudframework.modelfilter.FilterField

interface CrudSecurityHandler {
    fun getPolicies(clazz: Class<out PersistentEntity>): List<Policy<out PersistentEntity>>
    fun getFilterFields(clazz: Class<out PersistentEntity>): List<FilterField>
    fun evaluatePreCanAccess(clazz: Class<out PersistentEntity>): MultiPolicyResult
    fun evaluatePostCanAccess(entity: PersistentEntity, clazz: Class<out PersistentEntity>): MultiPolicyResult
}