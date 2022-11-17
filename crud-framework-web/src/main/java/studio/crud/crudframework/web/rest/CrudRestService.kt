package studio.crud.crudframework.web.rest

import studio.crud.crudframework.modelfilter.DynamicModelFilter
import studio.crud.crudframework.ro.BaseRO
import studio.crud.crudframework.ro.PagingDTO
import studio.crud.crudframework.web.ro.ManyCrudResult
import java.io.Serializable

interface CrudRestService {
    fun show(resourceName: String, id: Serializable): Any?
    fun index(resourceName: String, filter: DynamicModelFilter): PagingDTO<out Any>
    fun indexCount(resourceName: String, filter: DynamicModelFilter): Long
    fun delete(resourceName: String, id: Serializable): Any?
    fun create(resourceName: String, body: String): Any?
    fun createMany(resourceName: String, body: String): ManyCrudResult<out BaseRO<*>, out BaseRO<*>>
    fun update(resourceName: String, id: Serializable, body: String): Any?
    fun updateMany(resourceName: String, body: String): ManyCrudResult<out BaseRO<*>, out BaseRO<*>>
}