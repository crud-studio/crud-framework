package studio.crud.crudframework.test

import dev.krud.shapeshift.resolver.annotation.MappedField
import studio.crud.crudframework.crud.annotation.CrudEntity
import studio.crud.crudframework.model.BaseCrudEntity

@CrudEntity(dao = TestCrudDaoImpl::class)
abstract class AbstractTestEntity(@MappedField(target = AbstractTestRO::class) override var id: Long) : BaseCrudEntity<Long>() {
    override fun exists(): Boolean {
        return id != 0L
    }
}