package studio.crud.crudframework.crud.policy

import org.springframework.context.annotation.Configuration
import studio.crud.crudframework.crud.handler.CrudHandlerImpl
import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.crudframework.modelfilter.FilterField
import java.security.Principal

typealias PolicyCondition = (Principal?) -> Boolean
typealias PolicyFilterFieldSupplier = (Principal?) -> List<FilterField>

class TestEntity(var userId: Long? = null) : BaseCrudEntity<Long>() {
    override var id: Long = 1L
    override fun exists(): Boolean {
        return id != 0L
    }

}


fun main() {
    val matchPolicy = policy<TestEntity> {
        canAccess {
            filter {
                if (!it.hasRole('admin')) {
                    TestEntity::userId = it?.name?.toLong()
                }
            }
        }
    }

    val principal = Principal { "1" }
    val testEntity = TestEntity(1L)
    println("Match: " + matchPolicy.matchesCanAccess(testEntity, principal))
    println("Filter: " + matchPolicy.getCanAccessFilterFields(principal))
    println("toString: " + matchPolicy.canAccessVerbs.first().policyConditions.first())

    val crudHandler = CrudHandlerImpl()
}

