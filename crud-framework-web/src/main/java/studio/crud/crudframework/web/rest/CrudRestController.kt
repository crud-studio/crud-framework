package studio.crud.crudframework.web.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import studio.crud.crudframework.modelfilter.DynamicModelFilter
import studio.crud.crudframework.web.controller.BaseController
import studio.crud.crudframework.web.ro.ResultRO
import java.io.Serializable

abstract class CrudRestController : BaseController() {
    @Autowired
    private lateinit var crudRestService: CrudRestService

    open fun wrapResult(response: ResultRO<*>?): ResponseEntity<*> = ResponseEntity.ok(response)

    @GetMapping("/{type}/{id}")
    fun show(@PathVariable type: String, @PathVariable id: Serializable): ResponseEntity<*> {
        val result = wrapResult {
            crudRestService.show(type, id)
        }
        return this.wrapResult(result)
    }

    @PostMapping("/{type}/index")
    fun index(@PathVariable type: String, @RequestBody filter: DynamicModelFilter): ResponseEntity<*> {
        val result = wrapResult {
            crudRestService.index(type, filter)
        }
        return this.wrapResult(result)
    }

    @PostMapping("/{type}/create")
    fun create(@PathVariable type: String, @RequestBody body: String): ResponseEntity<*> {
        val result = wrapResult {
            crudRestService.create(type, body)
        }
        return this.wrapResult(result)
    }

    @PutMapping("/{type}/{id}")
    fun update(@PathVariable type: String, @PathVariable id: Serializable, @RequestBody body: String): ResponseEntity<*> {
        val result = wrapResult {
            crudRestService.update(type, id, body)
        }
        return this.wrapResult(result)
    }

    @DeleteMapping("/{type}/{id}")
    fun delete(@PathVariable type: String, @PathVariable id: Serializable): ResponseEntity<*> {
        val result = wrapResult {
            crudRestService.delete(type, id)
        }
        return this.wrapResult(result)
    }
}