package studio.crud.crudframework.web.rest

import studio.crud.crudframework.modelfilter.DynamicModelFilter
import studio.crud.crudframework.ro.PagingDTO
import java.io.Serializable

interface CrudRestService {
    fun show(type: String, id: Serializable): Any?
    fun index(type: String, filter: DynamicModelFilter): PagingDTO<out Any>
    fun delete(type: String, id: Serializable): Any?
}