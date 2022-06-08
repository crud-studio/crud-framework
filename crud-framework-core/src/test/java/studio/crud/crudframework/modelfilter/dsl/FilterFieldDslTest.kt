package studio.crud.crudframework.modelfilter.dsl

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import studio.crud.crudframework.model.PersistentEntity
import studio.crud.crudframework.modelfilter.FilterField
import studio.crud.crudframework.modelfilter.enums.FilterFieldDataType
import studio.crud.crudframework.modelfilter.enums.FilterFieldOperation
import java.util.Date

class FilterFieldDslTest {
    enum class TestEnum {
        One, Two, Three
    }
    class TestClazz : PersistentEntity {
        val testString: String = "test"
        val testInt: Int = 1
        val testLong: Long = 1L
        val testDouble: Double = 1.0
        val testDate: Date = Date(0)
        val testBoolean: Boolean = true
        val testEnum: TestEnum = TestEnum.One
        val testStringList: List<String> = emptyList()
        val testIntList: List<Int> = emptyList()
        val testLongList: List<Long> = emptyList()
        val testDoubleList: List<Double> = emptyList()
        val testDateList: List<Date> = emptyList()
        val testBooleanList: List<Boolean> = emptyList()
        val testEnumList: List<TestEnum> = emptyList()
    }
    @Test
    fun `test Equal#String`() {
        val filterField = and<TestClazz> {
            TestClazz::testString Equal "value"
        }.children.first()

        filterField.runAssertions(
            "testString",
            FilterFieldOperation.Equal,
            FilterFieldDataType.String,
            1,
            arrayOf("value")
        )
    }

    @Test
    fun `test Equal#Int`() {
        val filterField = and<TestClazz> {
            TestClazz::testInt Equal 1
        }.children.first()

        filterField.runAssertions(
            "testInt",
            FilterFieldOperation.Equal,
            FilterFieldDataType.Integer,
            1,
            arrayOf(1)
        )
    }

    @Test
    fun `test Equal#Long`() {
        val filterField = and<TestClazz> {
            TestClazz::testLong Equal 1L
        }.children.first()

        filterField.runAssertions(
            "testLong",
            FilterFieldOperation.Equal,
            FilterFieldDataType.Long,
            1,
            arrayOf(1L)
        )
    }

    @Test
    fun `test Equal#Double`() {
        val filterField = and<TestClazz> {
            TestClazz::testDouble Equal 1.0
        }.children.first()

        filterField.runAssertions(
            "testDouble",
            FilterFieldOperation.Equal,
            FilterFieldDataType.Double,
            1,
            arrayOf(1.0)
        )
    }

    @Test
    fun `test Equal#Boolean`() {
        val filterField = and<TestClazz> {
            TestClazz::testBoolean Equal true
        }.children.first()

        filterField.runAssertions(
            "testBoolean",
            FilterFieldOperation.Equal,
            FilterFieldDataType.Boolean,
            1,
            arrayOf(true)
        )
    }

    @Test
    fun `test Equal#Date`() {
        val filterField = and<TestClazz> {
            TestClazz::testDate Equal Date(0)
        }.children.first()

        filterField.runAssertions(
            "testDate",
            FilterFieldOperation.Equal,
            FilterFieldDataType.Date,
            1,
            arrayOf(Date(0))
        )
    }

    @Test
    fun `test Equal#Enum`() {
        val filterField = and<TestClazz> {
            TestClazz::testEnum Equal TestEnum.One
        }.children.first()

        filterField.runAssertions(
            "testEnum",
            FilterFieldOperation.Equal,
            FilterFieldDataType.Enum,
            1,
            arrayOf(TestEnum.One),
            TestEnum::class.java.canonicalName
        )
    }

    @Test
    fun `test NotEqual#String`() {
        val filterField = and<TestClazz> {
            TestClazz::testString NotEqual "value"
        }.children.first()

        filterField.runAssertions(
            "testString",
            FilterFieldOperation.NotEqual,
            FilterFieldDataType.String,
            1,
            arrayOf("value")
        )
    }

    @Test
    fun `test NotEqual#Int`() {
        val filterField = and<TestClazz> {
            TestClazz::testInt NotEqual 1
        }.children.first()

        filterField.runAssertions(
            "testInt",
            FilterFieldOperation.NotEqual,
            FilterFieldDataType.Integer,
            1,
            arrayOf(1)
        )
    }

    @Test
    fun `test NotEqual#Long`() {
        val filterField = and<TestClazz> {
            TestClazz::testLong NotEqual 1L
        }.children.first()

        filterField.runAssertions(
            "testLong",
            FilterFieldOperation.NotEqual,
            FilterFieldDataType.Long,
            1,
            arrayOf(1L)
        )
    }

    @Test
    fun `test NotEqual#Double`() {
        val filterField = and<TestClazz> {
            TestClazz::testDouble NotEqual 1.0
        }.children.first()

        filterField.runAssertions(
            "testDouble",
            FilterFieldOperation.NotEqual,
            FilterFieldDataType.Double,
            1,
            arrayOf(1.0)
        )
    }

    @Test
    fun `test NotEqual#Boolean`() {
        val filterField = and<TestClazz> {
            TestClazz::testBoolean NotEqual true
        }.children.first()

        filterField.runAssertions(
            "testBoolean",
            FilterFieldOperation.NotEqual,
            FilterFieldDataType.Boolean,
            1,
            arrayOf(true)
        )
    }

    @Test
    fun `test NotEqual#Date`() {
        val filterField = and<TestClazz> {
            TestClazz::testDate NotEqual Date(0)
        }.children.first()

        filterField.runAssertions(
            "testDate",
            FilterFieldOperation.NotEqual,
            FilterFieldDataType.Date,
            1,
            arrayOf(Date(0))
        )
    }

    @Test
    fun `test NotEqual#Enum`() {
        val filterField = and<TestClazz> {
            TestClazz::testEnum NotEqual TestEnum.One
        }.children.first()

        filterField.runAssertions(
            "testEnum",
            FilterFieldOperation.NotEqual,
            FilterFieldDataType.Enum,
            1,
            arrayOf(TestEnum.One),
            TestEnum::class.java.canonicalName
        )
    }

    @Test
    fun `test Contains#String`() {
        val filterField = and<TestClazz> {
            TestClazz::testString Contains "value"
        }.children.first()

        filterField.runAssertions(
            "testString",
            FilterFieldOperation.Contains,
            FilterFieldDataType.String,
            1,
            arrayOf("value")
        )
    }

    @Test
    fun `test StartsWith#String`() {
        val filterField = and<TestClazz> {
            TestClazz::testString StartsWith "value"
        }.children.first()

        filterField.runAssertions(
            "testString",
            FilterFieldOperation.StartsWith,
            FilterFieldDataType.String,
            1,
            arrayOf("value")
        )
    }

    @Test
    fun `test EndsWith#String`() {
        val filterField = and<TestClazz> {
            TestClazz::testString EndsWith "value"
        }.children.first()

        filterField.runAssertions(
            "testString",
            FilterFieldOperation.EndsWith,
            FilterFieldDataType.String,
            1,
            arrayOf("value")
        )
    }

    @Test
    fun `test GreaterThan#Int`() {
        val filterField = and<TestClazz> {
            TestClazz::testInt GreaterThan 1
        }.children.first()

        filterField.runAssertions(
            "testInt",
            FilterFieldOperation.GreaterThan,
            FilterFieldDataType.Integer,
            1,
            arrayOf(1)
        )
    }

    @Test
    fun `test GreaterThan#Long`() {
        val filterField = and<TestClazz> {
            TestClazz::testLong GreaterThan 1L
        }.children.first()

        filterField.runAssertions(
            "testLong",
            FilterFieldOperation.GreaterThan,
            FilterFieldDataType.Long,
            1,
            arrayOf(1L)
        )
    }

    @Test
    fun `test GreaterThan#Double`() {
        val filterField = and<TestClazz> {
            TestClazz::testDouble GreaterThan 1.0
        }.children.first()

        filterField.runAssertions(
            "testDouble",
            FilterFieldOperation.GreaterThan,
            FilterFieldDataType.Double,
            1,
            arrayOf(1.0)
        )
    }

    @Test
    fun `test GreaterThan#Date`() {
        val filterField = and<TestClazz> {
            TestClazz::testDate GreaterThan Date(0)
        }.children.first()

        filterField.runAssertions(
            "testDate",
            FilterFieldOperation.GreaterThan,
            FilterFieldDataType.Date,
            1,
            arrayOf(Date(0))
        )
    }

    @Test
    fun `test GreaterOrEqual#Int`() {
        val filterField = and<TestClazz> {
            TestClazz::testInt GreaterOrEqual 1
        }.children.first()

        filterField.runAssertions(
            "testInt",
            FilterFieldOperation.GreaterEqual,
            FilterFieldDataType.Integer,
            1,
            arrayOf(1)
        )
    }

    @Test
    fun `test GreaterOrEqual#Long`() {
        val filterField = and<TestClazz> {
            TestClazz::testLong GreaterOrEqual 1L
        }.children.first()

        filterField.runAssertions(
            "testLong",
            FilterFieldOperation.GreaterEqual,
            FilterFieldDataType.Long,
            1,
            arrayOf(1L)
        )
    }

    @Test
    fun `test GreaterOrEqual#Double`() {
        val filterField = and<TestClazz> {
            TestClazz::testDouble GreaterOrEqual 1.0
        }.children.first()

        filterField.runAssertions(
            "testDouble",
            FilterFieldOperation.GreaterEqual,
            FilterFieldDataType.Double,
            1,
            arrayOf(1.0)
        )
    }

    @Test
    fun `test GreaterOrEqual#Date`() {
        val filterField = and<TestClazz> {
            TestClazz::testDate GreaterOrEqual Date(0)
        }.children.first()

        filterField.runAssertions(
            "testDate",
            FilterFieldOperation.GreaterEqual,
            FilterFieldDataType.Date,
            1,
            arrayOf(Date(0))
        )
    }

    @Test
    fun `test LowerThan#Int`() {
        val filterField = and<TestClazz> {
            TestClazz::testInt LowerThan 1
        }.children.first()

        filterField.runAssertions(
            "testInt",
            FilterFieldOperation.LowerThan,
            FilterFieldDataType.Integer,
            1,
            arrayOf(1)
        )
    }

    @Test
    fun `test LowerThan#Long`() {
        val filterField = and<TestClazz> {
            TestClazz::testLong LowerThan 1L
        }.children.first()

        filterField.runAssertions(
            "testLong",
            FilterFieldOperation.LowerThan,
            FilterFieldDataType.Long,
            1,
            arrayOf(1L)
        )
    }

    @Test
    fun `test LowerThan#Double`() {
        val filterField = and<TestClazz> {
            TestClazz::testDouble LowerThan 1.0
        }.children.first()

        filterField.runAssertions(
            "testDouble",
            FilterFieldOperation.LowerThan,
            FilterFieldDataType.Double,
            1,
            arrayOf(1.0)
        )
    }

    @Test
    fun `test LowerThan#Date`() {
        val filterField = and<TestClazz> {
            TestClazz::testDate LowerThan Date(0)
        }.children.first()

        filterField.runAssertions(
            "testDate",
            FilterFieldOperation.LowerThan,
            FilterFieldDataType.Date,
            1,
            arrayOf(Date(0))
        )
    }

    @Test
    fun `test LowerOrEqual#Int`() {
        val filterField = and<TestClazz> {
            TestClazz::testInt LowerOrEqual 1
        }.children.first()

        filterField.runAssertions(
            "testInt",
            FilterFieldOperation.LowerEqual,
            FilterFieldDataType.Integer,
            1,
            arrayOf(1)
        )
    }

    @Test
    fun `test LowerOrEqual#Long`() {
        val filterField = and<TestClazz> {
            TestClazz::testLong LowerOrEqual 1L
        }.children.first()

        filterField.runAssertions(
            "testLong",
            FilterFieldOperation.LowerEqual,
            FilterFieldDataType.Long,
            1,
            arrayOf(1L)
        )
    }

    @Test
    fun `test LowerOrEqual#Double`() {
        val filterField = and<TestClazz> {
            TestClazz::testDouble LowerOrEqual 1.0
        }.children.first()

        filterField.runAssertions(
            "testDouble",
            FilterFieldOperation.LowerEqual,
            FilterFieldDataType.Double,
            1,
            arrayOf(1.0)
        )
    }

    @Test
    fun `test LowerOrEqual#Date`() {
        val filterField = and<TestClazz> {
            TestClazz::testDate LowerOrEqual Date(0)
        }.children.first()

        filterField.runAssertions(
            "testDate",
            FilterFieldOperation.LowerEqual,
            FilterFieldDataType.Date,
            1,
            arrayOf(Date(0))
        )
    }

    @Test
    fun `test RequireIn#String if empty`() {
        val filterField = and<TestClazz> {
            TestClazz::testStringList RequireIn listOf<String>()
        }.children.first()

        expectThat(filterField.operation)
            .isEqualTo(FilterFieldOperation.Noop)
    }

    @Test
    fun `test RequireIn#String`() {
        val filterField = and<TestClazz> {
            TestClazz::testStringList RequireIn listOf("a", "b")
        }.children.first()

        filterField.runAssertions(
            "testStringList",
            FilterFieldOperation.In,
            FilterFieldDataType.String,
            2,
            arrayOf("a", "b")
        )
    }

    @Test
    fun `test RequireIn#Int if empty`() {
        val filterField = and<TestClazz> {
            TestClazz::testIntList RequireIn listOf<Int>()
        }.children.first()

        expectThat(filterField.operation)
            .isEqualTo(FilterFieldOperation.Noop)
    }

    @Test
    fun `test RequireIn#Int`() {
        val filterField = and<TestClazz> {
            TestClazz::testIntList RequireIn listOf<Int>(1, 2)
        }.children.first()

        filterField.runAssertions(
            "testIntList",
            FilterFieldOperation.In,
            FilterFieldDataType.Integer,
            2,
            arrayOf(1, 2)
        )
    }

    @Test
    fun `test RequireIn#Long if empty`() {
        val filterField = and<TestClazz> {
            TestClazz::testLongList RequireIn listOf<Long>()
        }.children.first()

        expectThat(filterField.operation)
            .isEqualTo(FilterFieldOperation.Noop)
    }

    @Test
    fun `test RequireIn#Long`() {
        val filterField = and<TestClazz> {
            TestClazz::testLongList RequireIn listOf(1L, 2L)
        }.children.first()

        filterField.runAssertions(
            "testLongList",
            FilterFieldOperation.In,
            FilterFieldDataType.Long,
            2,
            arrayOf(1L, 2L)
        )
    }

    @Test
    fun `test RequireIn#Double if empty`() {
        val filterField = and<TestClazz> {
            TestClazz::testDoubleList RequireIn listOf<Double>()
        }.children.first()

        expectThat(filterField.operation)
            .isEqualTo(FilterFieldOperation.Noop)
    }

    @Test
    fun `test RequireIn#Double`() {
        val filterField = and<TestClazz> {
            TestClazz::testDoubleList RequireIn listOf(1.0, 2.0)
        }.children.first()

        filterField.runAssertions(
            "testDoubleList",
            FilterFieldOperation.In,
            FilterFieldDataType.Double,
            2,
            arrayOf(1.0, 2.0)
        )
    }

    @Test
    fun `test RequireIn#Date if empty`() {
        val filterField = and<TestClazz> {
            TestClazz::testDateList RequireIn listOf<Date>()
        }.children.first()

        expectThat(filterField.operation)
            .isEqualTo(FilterFieldOperation.Noop)
    }

    @Test
    fun `test RequireIn#Date`() {
        val filterField = and<TestClazz> {
            TestClazz::testDateList RequireIn listOf(Date(0), Date(1))
        }.children.first()

        filterField.runAssertions(
            "testDateList",
            FilterFieldOperation.In,
            FilterFieldDataType.Date,
            2,
            arrayOf(Date(0), Date(1))
        )
    }

    @Test
    fun `test RequireIn#Enum if empty`() {
        val filterField = and<TestClazz> {
            TestClazz::testEnumList RequireIn listOf<TestEnum>()
        }.children.first()

        expectThat(filterField.operation)
            .isEqualTo(FilterFieldOperation.Noop)
    }

    @Test
    fun `test RequireIn#Enum`() {
        val filterField = and<TestClazz> {
            TestClazz::testEnumList RequireIn listOf(TestEnum.One, TestEnum.Two)
        }.children.first()

        filterField.runAssertions(
            "testEnumList",
            FilterFieldOperation.In,
            FilterFieldDataType.Enum,
            2,
            arrayOf(TestEnum.One, TestEnum.Two),
            TestEnum::class.java.canonicalName
        )
    }

    @Test
    fun `test RequireNotIn#String if empty`() {
        val filterField = and<TestClazz> {
            TestClazz::testStringList RequireNotIn listOf<String>()
        }.children.first()

        expectThat(filterField.operation)
            .isEqualTo(FilterFieldOperation.Noop)
    }

    @Test
    fun `test RequireNotIn#String`() {
        val filterField = and<TestClazz> {
            TestClazz::testStringList RequireNotIn listOf("a", "b")
        }.children.first()

        filterField.runAssertions(
            "test",
            FilterFieldOperation.NotIn,
            FilterFieldDataType.String,
            2,
            arrayOf("a", "b")
        )
    }

    @Test
    fun `test RequireNotIn#Int if empty`() {
        val filterField = and<TestClazz> {
            TestClazz::testIntList RequireNotIn listOf<Int>()
        }.children.first()

        expectThat(filterField.operation)
            .isEqualTo(FilterFieldOperation.Noop)
    }

    @Test
    fun `test RequireNotIn#Int`() {
        val filterField = and<TestClazz> {
            TestClazz::testIntList RequireNotIn listOf<Int>(1, 2)
        }.children.first()

        filterField.runAssertions(
            "testIntList",
            FilterFieldOperation.NotIn,
            FilterFieldDataType.Integer,
            2,
            arrayOf(1, 2)
        )
    }

    @Test
    fun `test RequireNotIn#Long if empty`() {
        val filterField = and<TestClazz> {
            TestClazz::testLongList RequireNotIn listOf<Long>()
        }.children.first()

        expectThat(filterField.operation)
            .isEqualTo(FilterFieldOperation.Noop)
    }

    @Test
    fun `test RequireNotIn#Long`() {
        val filterField = and<TestClazz> {
            TestClazz::testLongList RequireNotIn listOf(1L, 2L)
        }.children.first()

        filterField.runAssertions(
            "testLongList",
            FilterFieldOperation.NotIn,
            FilterFieldDataType.Long,
            2,
            arrayOf(1L, 2L)
        )
    }

    @Test
    fun `test RequireNotIn#Double if empty`() {
        val filterField = and<TestClazz> {
            TestClazz::testDoubleList RequireNotIn listOf<Double>()
        }.children.first()

        expectThat(filterField.operation)
            .isEqualTo(FilterFieldOperation.Noop)
    }

    @Test
    fun `test RequireNotIn#Double`() {
        val filterField = and<TestClazz> {
            TestClazz::testDoubleList RequireNotIn listOf(1.0, 2.0)
        }.children.first()

        filterField.runAssertions(
            "testDoubleList",
            FilterFieldOperation.NotIn,
            FilterFieldDataType.Double,
            2,
            arrayOf(1.0, 2.0)
        )
    }

    @Test
    fun `test RequireNotIn#Date if empty`() {
        val filterField = and<TestClazz> {
            TestClazz::testDateList RequireNotIn listOf<Date>()
        }.children.first()

        expectThat(filterField.operation)
            .isEqualTo(FilterFieldOperation.Noop)
    }

    @Test
    fun `test RequireNotIn#Date`() {
        val filterField = and<TestClazz> {
            TestClazz::testDateList RequireNotIn listOf(Date(0), Date(1))
        }.children.first()

        filterField.runAssertions(
            "testDateList",
            FilterFieldOperation.NotIn,
            FilterFieldDataType.Date,
            2,
            arrayOf(Date(0), Date(1))
        )
    }

    @Test
    fun `test RequireNotIn#Enum if empty`() {
        val filterField = and<TestClazz> {
            TestClazz::testEnumList RequireNotIn listOf<TestEnum>()
        }.children.first()

        expectThat(filterField.operation)
            .isEqualTo(FilterFieldOperation.Noop)
    }

    @Test
    fun `test RequireNotIn#Enum`() {
        val filterField = and<TestClazz> {
            TestClazz::testEnumList RequireNotIn listOf(TestEnum.One, TestEnum.Two)
        }.children.first()

        filterField.runAssertions(
            "test",
            FilterFieldOperation.NotIn,
            FilterFieldDataType.Enum,
            2,
            arrayOf(TestEnum.One, TestEnum.Two),
            TestEnum::class.java.canonicalName
        )
    }

    @Test
    fun `test In#String`() {
        val filterField = and<TestClazz> {
            TestClazz::testStringList In listOf("a", "b")
        }.children.first()

        filterField.runAssertions(
            "testStringList",
            FilterFieldOperation.In,
            FilterFieldDataType.String,
            2,
            arrayOf("a", "b")
        )
    }

    @Test
    fun `test In#Int`() {
        val filterField = and<TestClazz> {
            TestClazz::testIntList In listOf<Int>(1, 2)
        }.children.first()

        filterField.runAssertions(
            "testIntList",
            FilterFieldOperation.In,
            FilterFieldDataType.Integer,
            2,
            arrayOf(1, 2)
        )
    }

    @Test
    fun `test In#Long`() {
        val filterField = and<TestClazz> {
            TestClazz::testLongList In listOf(1L, 2L)
        }.children.first()

        filterField.runAssertions(
            "testLongList",
            FilterFieldOperation.In,
            FilterFieldDataType.Long,
            2,
            arrayOf(1L, 2L)
        )
    }

    @Test
    fun `test In#Double`() {
        val filterField = and<TestClazz> {
            TestClazz::testDoubleList In listOf(1.0, 2.0)
        }.children.first()

        filterField.runAssertions(
            "testDoubleList",
            FilterFieldOperation.In,
            FilterFieldDataType.Double,
            2,
            arrayOf(1.0, 2.0)
        )
    }

    @Test
    fun `test In#Date`() {
        val filterField = and<TestClazz> {
            TestClazz::testDateList In listOf(Date(0), Date(1))
        }.children.first()

        filterField.runAssertions(
            "testDateList",
            FilterFieldOperation.In,
            FilterFieldDataType.Date,
            2,
            arrayOf(Date(0), Date(1))
        )
    }

    @Test
    fun `test In#Enum if empty`() {
        expectThrows<NoSuchElementException> {
            // Since we derive the type from the first element in the collection, this will throw an exception
            and<TestClazz> {
                TestClazz::testEnumList In emptyList()
            }.children.first()
        }
    }

    @Test
    fun `test In#Enum`() {
        val filterField = and<TestClazz> {
            TestClazz::testEnumList In listOf(TestEnum.One, TestEnum.Two)
        }.children.first()

        filterField.runAssertions(
            "testEnumList",
            FilterFieldOperation.In,
            FilterFieldDataType.Enum,
            2,
            arrayOf(TestEnum.One, TestEnum.Two),
            TestEnum::class.java.canonicalName
        )
    }

    @Test
    fun `test NotIn#String`() {
        val filterField = and<TestClazz> {
            TestClazz::testStringList NotIn listOf("a", "b")
        }.children.first()

        filterField.runAssertions(
            "testStringList",
            FilterFieldOperation.NotIn,
            FilterFieldDataType.String,
            2,
            arrayOf("a", "b")
        )
    }

    @Test
    fun `test NotIn#NotInt`() {
        val filterField = and<TestClazz> {
            TestClazz::testIntList NotIn listOf<Int>(1, 2)
        }.children.first()

        filterField.runAssertions(
            "testIntList",
            FilterFieldOperation.NotIn,
            FilterFieldDataType.Integer,
            2,
            arrayOf(1, 2)
        )
    }

    @Test
    fun `test NotIn#Long`() {
        val filterField = and<TestClazz> {
            TestClazz::testLongList NotIn listOf(1L, 2L)
        }.children.first()

        filterField.runAssertions(
            "testLongList",
            FilterFieldOperation.NotIn,
            FilterFieldDataType.Long,
            2,
            arrayOf(1L, 2L)
        )
    }

    @Test
    fun `test NotIn#Double`() {
        val filterField = and<TestClazz> {
            TestClazz::testDoubleList NotIn listOf(1.0, 2.0)
        }.children.first()

        filterField.runAssertions(
            "testDoubleList",
            FilterFieldOperation.NotIn,
            FilterFieldDataType.Double,
            2,
            arrayOf(1.0, 2.0)
        )
    }

    @Test
    fun `test NotIn#Date`() {
        val filterField = and<TestClazz> {
            TestClazz::testDateList NotIn listOf(Date(0), Date(1))
        }.children.first()

        filterField.runAssertions(
            "testDateList",
            FilterFieldOperation.NotIn,
            FilterFieldDataType.Date,
            2,
            arrayOf(Date(0), Date(1))
        )
    }

    @Test
    fun `test NotIn#Enum if empty`() {
        expectThrows<NoSuchElementException> {
            // Since we derive the type from the first element in the collection, this will throw an exception
            and<TestClazz> {
                TestClazz::testEnumList NotIn listOf<TestEnum>()
            }.children.first()
        }
    }

    @Test
    fun `test NotIn#Enum`() {
        val filterField = and<TestClazz> {
            TestClazz::testEnumList NotIn listOf(TestEnum.One, TestEnum.Two)
        }.children.first()

        filterField.runAssertions(
            "testEnumList",
            FilterFieldOperation.NotIn,
            FilterFieldDataType.Enum,
            2,
            arrayOf(TestEnum.One, TestEnum.Two),
            TestEnum::class.java.canonicalName
        )
    }

    @Test
    fun `test ContainsIn#String`() {
        val filterField = and<TestClazz> {
            TestClazz::testStringList ContainsIn listOf("a", "b")
        }.children.first()

        filterField.runAssertions(
            "testStringList",
            FilterFieldOperation.ContainsIn,
            FilterFieldDataType.String,
            2,
            arrayOf("a", "b")
        )
    }

    @Test
    fun `test NotContainsIn#String`() {
        val filterField = and<TestClazz> {
            TestClazz::testStringList NotContainsIn listOf("a", "b")
        }.children.first()

        filterField.runAssertions(
            "testStringList",
            FilterFieldOperation.NotContainsIn,
            FilterFieldDataType.String,
            2,
            arrayOf("a", "b")
        )
    }

    @Test
    fun `test Between#Int`() {
        val filterField = and<TestClazz> {
            TestClazz::testInt Between 1 And 2
        }.children.first()

        filterField.runAssertions(
            "testInt",
            FilterFieldOperation.Between,
            FilterFieldDataType.Integer,
            2,
            arrayOf(1, 2)
        )
    }

    @Test
    fun `test Between#Long`() {
        val filterField = and<TestClazz> {
            TestClazz::testLong Between 1L And 2L
        }.children.first()

        filterField.runAssertions(
            "testLong",
            FilterFieldOperation.Between,
            FilterFieldDataType.Long,
            2,
            arrayOf(1L, 2L)
        )
    }

    @Test
    fun `test Between#Double`() {
        val filterField = and<TestClazz> {
            TestClazz::testDouble Between 1.0 And 2.0
        }.children.first()

        filterField.runAssertions(
            "testDouble",
            FilterFieldOperation.Between,
            FilterFieldDataType.Double,
            2,
            arrayOf(1.0, 2.0)
        )
    }

    @Test
    fun `test Between#Date`() {
        val filterField = and<TestClazz> {
            TestClazz::testDate Between Date(0) And Date(1)
        }.children.first()

        filterField.runAssertions(
            "testDate",
            FilterFieldOperation.Between,
            FilterFieldDataType.Date,
            2,
            arrayOf(Date(0), Date(1))
        )
    }

    @Test
    fun `test isNull`() {
        val filterField = and<TestClazz> {
            TestClazz::testString.isNull()
        }.children.first()

        filterField.runAssertions(
            "test",
            FilterFieldOperation.IsNull,
            FilterFieldDataType.Object,
            2,
            arrayOf(null, null)
        )
    }

    @Test
    fun `test isNotNull`() {
        val filterField = and<TestClazz> {
            TestClazz::testString.isNotNull()
        }.children.first()

        filterField.runAssertions(
            "test",
            FilterFieldOperation.IsNotNull,
            FilterFieldDataType.Object,
            2,
            arrayOf(null, null)
        )
    }

    private fun FilterField.runAssertions(
        expectedFieldName: String,
        expectedOperation: FilterFieldOperation,
        expectedDataType: FilterFieldDataType,
        expectedValuesLength: Int,
        expectedValues: Array<Any?>,
        expectedEnumType: String? = null
    ) {
        expectThat(this.fieldName)
            .isEqualTo(expectedFieldName)
        expectThat(this.operation)
            .isEqualTo(expectedOperation)
        expectThat(this.dataType)
            .isEqualTo(expectedDataType)
        expectThat(this.values.size)
            .isEqualTo(expectedValuesLength)
        expectThat(this.values)
            .isEqualTo(expectedValues)

        if (expectedEnumType != null) {
            expectThat(this.enumType)
                .isEqualTo(enumType)
        }
    }
}