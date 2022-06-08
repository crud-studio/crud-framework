package studio.crud.crudframework.crud.test

import studio.crud.crudframework.crud.annotation.CrudEntity
import studio.crud.crudframework.model.BaseCrudEntity

@CrudEntity(TestCrudDao::class)
abstract class AbstractTestEntity : BaseCrudEntity<Long>() {
    override var id: Long
        get() = 0L
        set(value) {}

    override fun exists(): Boolean {
        return false
    }
}