package studio.crud.crudframework.crud.policy

import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.crudframework.modelfilter.FilterField
import java.security.Principal

typealias PolicyFilterFieldSupplier = (Principal?) -> List<FilterField>

class TestEntity(override var id: Long = 1L, var name: String = "") : BaseCrudEntity<Long>() {
    override fun exists(): Boolean {
        return id != 0L
    }

}

fun main() {
    val p = policy<TestEntity>("main test entity checks") {
        canAccess("standard checks") {
            condition("is admin") {
                it?.name == "admin"
            }
        }
        canCreate {
            condition {
                it?.name == "john"
            }
        }

        canUpdate {
            preCondition {
                principal.hasPermission("update_test_entity")
            }
            postCondition { entity, principal ->
                entity.managerId = principal.name
            }
        }
    }

    val y = p.evaluateCanCreate(Principal { "john" })
    println(y.outputFullOutcome())
    println(y.outputShortOutcome())
}