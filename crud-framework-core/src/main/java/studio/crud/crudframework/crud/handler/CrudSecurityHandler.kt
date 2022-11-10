package studio.crud.crudframework.crud.handler

import studio.crud.crudframework.crud.policy.Policy
import studio.crud.crudframework.model.PersistentEntity
import studio.crud.crudframework.modelfilter.FilterField

interface CrudSecurityHandler {
    fun getPolicies(clazz: Class<*>): List<Policy<out PersistentEntity>>
    fun getFilterFields(clazz: Class<*>): List<FilterField>
    fun evaluatePreCanAccess(clazz: Class<*>): MultiPolicyResult
    fun evaluatePostCanAccess(entity: PersistentEntity, clazz: Class<*>): MultiPolicyResult
}