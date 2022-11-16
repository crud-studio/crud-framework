package studio.crud.crudframework.crud.policy

import studio.crud.crudframework.model.BaseCrudEntity

class TestEntity(override var id: Long = 1L, var name: String = "") : BaseCrudEntity<Long>() {
    override fun exists(): Boolean {
        return id != 0L
    }

}

fun main() {
    val p = policy<TestEntity>("main test entity checks") {
        filter { principal ->

        }
        canAccess("permission check") {
            preCondition {
                false // principal.hasPermission("read_test_entity")
            }
            postCondition("fail always") { _, _ ->
                false
            }
        }
        canUpdate("Can update") {
            preCondition {
                it?.name == "john"
            }
            postCondition("target is anne") { entity, principal ->
                entity.name == "anne"
            }
        }
    }
}