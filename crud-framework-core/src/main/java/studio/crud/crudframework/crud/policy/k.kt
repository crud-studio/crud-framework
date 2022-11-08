package studio.crud.crudframework.crud.policy

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
            filter { principal ->
                TestEntity::userId Equal principal.name.toLong()
            }

            condition { principal ->
                true
            }
        }

        canAccess {
            filter { principal ->
                TestEntity::userId Equal principal.name.toLong()
            }
        }

        canCreate {
            condition { principal ->
                true
            }
        }
    }

    val principal = Principal { "1" }
    val testEntity = TestEntity(1L)
    println("Match: " + matchPolicy.matchesCanAccess(testEntity, principal))
    println("Filter: " + matchPolicy.getCanAccessFilterFields(principal))

    val crudHandler = CrudHandlerImpl()
}

