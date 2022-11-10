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
        canUpdate("Can update") {
            preCondition("user is john") {
                it?.name == "john"
            }
            postCondition("target is anne") { entity, principal ->
                entity.name == "anne"
            }
        }
    }

    val y = p.evaluatePreCanUpdate(Principal { "john" })
    val z = p.evaluatePostCanUpdate(TestEntity(1L, "anne"), Principal { "john" })
    println(y.outputShortOutcome())
    println(z.outputShortOutcome())
}