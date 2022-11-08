package studio.crud.crudframework.crud.handler

import studio.crud.crudframework.crud.policy.Policy
import studio.crud.crudframework.model.PersistentEntity
import studio.crud.crudframework.modelfilter.FilterField

interface CrudSecurityHandler {
    fun getPolicies(clazz: Class<*>): List<Policy<out PersistentEntity>>
    fun matchesCanAccess(entity: PersistentEntity, clazz: Class<*>): Boolean
    fun matchesCanUpdate(entity: PersistentEntity, clazz: Class<*>): Boolean
    fun getCanAccessFilterFields(clazz: Class<*>): List<FilterField>
    fun getCanUpdateFilterFields(clazz: Class<*>): List<FilterField>
    fun matchesCanDelete(entity: PersistentEntity, clazz: Class<*>): Boolean
    fun getDeleteFilterFields(clazz: Class<*>): List<FilterField>
    fun matchesCanCreate(clazz: Class<*>): Boolean
}