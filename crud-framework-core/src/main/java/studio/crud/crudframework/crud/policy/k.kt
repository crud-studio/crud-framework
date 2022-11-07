package studio.crud.crudframework.crud.policy

import org.springframework.beans.factory.annotation.Autowired
import studio.crud.crudframework.crud.handler.CrudHandlerImpl
import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.crudframework.model.PersistentEntity
import studio.crud.crudframework.modelfilter.FilterField
import studio.crud.crudframework.modelfilter.dsl.where
import java.security.Principal

typealias PolicyCondition = (Principal) -> Boolean
typealias PolicyFilterFieldSupplier = (Principal) -> List<FilterField>

class TestEntity(var userId: Long? = null) : BaseCrudEntity<Long>() {
    override var id: Long = 1L
    override fun exists(): Boolean {
        return id != 0L
    }

}


fun <RootType : PersistentEntity> PolicyBuilder<RootType>.permissionGate(readPermission: String) {
    canAccess {
        fun Principal.hasPermission(permission: String) = false
        condition {
            principal -> principal.hasPermission(readPermission)
        }
    }
}

fun main() {
    val matchPolicy = policy<TestEntity> {
        permissionGate("read_test_entity")
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
            condition { principal ->
                when (principal) {
                    is UserInfo ->
                    is RiderInfo ->
                }
                false
            }
        }
    }

    val principal = Principal { "1" }
    val testEntity = TestEntity(1L)
    println("Match: " + matchPolicy.matchesCanAccess(testEntity, principal))
    println("Filter: " + matchPolicy.getCanAccessFilterFields(principal))

    val crudHandler = CrudHandlerImpl()

    @Autowired lateinit var policies

    crudHandler.index(where<TestEntity> { }, TestEntity::class.java).applyDefaultPolicies()

    crudHandler.index(where<TestEntity> { }, TestEntity::class.java)
            .applyDefaultPolicies()

    CrudSecurityHelper
            .applyDefaultPolicies(crudHandler.index(where<TestEntity> { }, TestEntity::class.java))
            .execute()
}

