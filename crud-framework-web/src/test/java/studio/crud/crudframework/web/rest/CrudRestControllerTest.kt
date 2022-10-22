package studio.crud.crudframework.web.rest

import dev.krud.shapeshift.resolver.annotation.DefaultMappingTarget
import dev.krud.shapeshift.resolver.annotation.MappedField
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isNull
import strikt.assertions.isTrue
import studio.crud.crudframework.crud.annotation.Deleteable
import studio.crud.crudframework.modelfilter.dsl.where
import studio.crud.crudframework.test.AbstractTestEntity
import studio.crud.crudframework.test.AbstractTestRO
import studio.crud.crudframework.test.EnableTestCrud
import studio.crud.crudframework.test.TestCrudDaoImpl
import studio.crud.crudframework.web.ro.ResultRO

class TestCrudRestController : CrudRestController()

@ExtendWith(SpringExtension::class)
@EnableTestCrud
@EnableCrudControllers
@Import(CrudRestControllerTest.TestConfig::class)
class CrudRestControllerTest {
    @Autowired
    private lateinit var testCrudDao: TestCrudDaoImpl

    @Autowired
    private lateinit var testCrudRestController: TestCrudRestController

    @Configuration
    class TestConfig {
        @Bean
        fun testCrudRestController() = TestCrudRestController()
    }

    @Test
    @DirtiesContext
    fun `show happy flow`() {
        val subject = TestEntity(1L, "test")
        testCrudDao.entities += subject
        val response = testCrudRestController.show("test", 1L)
        val result = response.body as ResultRO<TestEntityRO>
        expectThat(result.isSuccess).isTrue()
        expectThat(result.result.id)
                .isEqualTo(1L)
        expectThat(result.result.name)
                .isEqualTo(subject.name)
    }

    @Test
    fun `show should throw if type is not registered`() {
        val response = testCrudRestController.show("nonexistent", 1L)
        val result = response.body as ResultRO<*>
        expectThat(result.isSuccess).isFalse()
        expectThat(result.error).isEqualTo("No crud controller definition found for type nonexistent")
    }

    @Test
    fun `show should return null if entity is not found`() {
        val response = testCrudRestController.show("test", 1L)
        val result = response.body as ResultRO<*>
        expectThat(result.isSuccess).isTrue()
        expectThat(result.result).isNull()
    }

    @Test
    @DirtiesContext
    fun `delete happy flow`() {
        val subject = TestEntity(1L, "test")
        testCrudDao.entities += subject
        val response = testCrudRestController.delete("test", 1L)
        val result = response.body as ResultRO<Unit>
        expectThat(result.isSuccess).isTrue()
        expectThat(testCrudDao.entities).isEmpty()
    }

    @Test
    fun `delete should throw if type is not registered`() {
        val response = testCrudRestController.delete("nonexistent", 1L)
        val result = response.body as ResultRO<TestEntity>
        expectThat(result.isSuccess).isFalse()
        expectThat(result.error).isEqualTo("No crud controller definition found for type nonexistent")
    }

    @Test
    @DirtiesContext
    fun `index happy flow`() {
        val subject = TestEntity(1L, "test")
        val secondSubject = TestEntity(2L, "test2")
        testCrudDao.entities += listOf(subject, secondSubject)
        val response = testCrudRestController.index("test", where<TestEntity> { TestEntity::name Equal "test" })
        val result = response.body as ResultRO<List<TestEntityRO>>
        expectThat(result.isSuccess).isTrue()
        expectThat(result.result)
                .hasSize(1)
        expectThat(result.result.first().id)
                .isEqualTo(1L)
        expectThat(result.result.first().name)
                .isEqualTo(subject.name)
    }
}

@CrudController(
    type = "test",
    roMapping = RoMapping(
        mainRoClass = TestEntityRO::class
    )
)
@DefaultMappingTarget(TestEntityRO::class)
@Deleteable(softDelete = false)
class TestEntity(id: Long, @MappedField var name: String = "test") : AbstractTestEntity(id) {
}

class TestEntityRO(var name: String? = null) : AbstractTestRO()