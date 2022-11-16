package studio.crud.crudframework.web.rest

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNull
import studio.crud.crudframework.crud.handler.CrudHandler
import studio.crud.crudframework.crud.policy.Policy
import studio.crud.crudframework.crud.policy.policy
import studio.crud.crudframework.crud.security.PrincipalProvider
import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.crudframework.modelfilter.dsl.emptyFilter
import studio.crud.crudframework.test.EnableTestCrud
import studio.crud.crudframework.test.TestCrudDaoImpl
import studio.crud.crudframework.test.TestCrudHandler
import studio.crud.crudframework.web.ro.ResultRO
import java.security.Principal


@ExtendWith(SpringExtension::class)
@EnableTestCrud
@Import(PolicyTest.TestConfig::class)
class PolicyTest {
    @Autowired
    private lateinit var testCrudDao: TestCrudDaoImpl

    @Autowired
    private lateinit var crudHandler: CrudHandler

    class TestEntity(override var id: Long, val name: String) : BaseCrudEntity<Long>() {
        override fun exists(): Boolean {
            return id != 0L
        }
    }

    @Configuration
    class TestConfig {
        @Bean
        fun canAccessPolicy(): Policy<*> = policy<TestEntityMain> {
            canAccess {
                preCondition("principal is admin") {
                    it?.name == "admin"
                }
                filter {
                    TestEntityMain::name Equal "john"
                }
            }
        }

        @Bean
        fun principalProvider() = object : PrincipalProvider {
            override fun getPrincipal(): Principal {
                return Principal { "admin2" }
            }
        }
    }

    @Test
    @DirtiesContext
    fun `canAccess test`() {
        val subject = TestEntityMain(1L, "test")
        testCrudDao.entities += subject
        val result = crudHandler.index(emptyFilter<TestEntityMain>(), TestEntityMain::class.java)
            .applyDefaultPolicies()
            .execute()
        println()
    }
}