package studio.crud.crudframework.util

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isFalse
import strikt.assertions.isTrue
import studio.crud.crudframework.model.BaseCrudEntity
import studio.crud.crudframework.model.PersistentEntity
import studio.crud.crudframework.modelfilter.BaseRawJunction
import studio.crud.crudframework.modelfilter.dsl.where
import java.util.Date

private class Customer(
    val name: String = "Test",
    val age: Int = 55,
    val creationTime: Date = Date(1000L),
    val email: String = "test@example.com",
    val address: Address = Address(),
    val favoriteFruits: List<String> = listOf("Apple", "Orange", "Apricot", "Banana"),
    val zodiacSign: String? = null
) : BaseCrudEntity<Long>() {
    override var id: Long = 1

    override fun exists(): Boolean {
        return true
    }
}

private class Address(
    val street: String = "Example Street",
    val city: String = "Colorado Springs",
    val state: State = State(),
    val zipCode: String = "55234"
) : PersistentEntity

private class State(
    val code: String = "CO",
    val name: String = "Colorado",
    val active: Boolean = true
) : PersistentEntity

class DynamicModelFilterUtilsTest {
    @Test
    fun `test Equal operation happy flow`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::address Sub {
                Address::street Equal "Example Street"
            }
        }
        expectThat(
            filter.matches(customer)
        ).isTrue()
    }

    @Test
    fun `test Equal operation with false outcome`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::address Sub {
                Address::street Equal "Example"
            }
        }
        expectThat(
            filter.matches(customer)
        ).isFalse()
    }

    @Test
    fun `test NotEqual operation happy flow`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::address Sub {
                Address::street NotEqual "Example"
            }
        }
        expectThat(
            filter.matches(customer)
        ).isTrue()
    }

    @Test
    fun `test NotEqual operation with false outcome`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::address Sub {
                Address::street NotEqual "Example Street"
            }
        }
        expectThat(
            filter.matches(customer)
        ).isFalse()
    }

    @Test
    fun `test In operation happy flow`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::favoriteFruits In listOf("Apple", "Apricot")
        }
        expectThat(
            filter.matches(customer)
        ).isTrue()
    }

    @Test
    fun `test In operation with false outcome`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::favoriteFruits In listOf("Tomato", "Apple")
        }
        expectThat(
            filter.matches(customer)
        ).isFalse()
    }

    @Test
    fun `test NotIn operation happy flow`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::favoriteFruits NotIn listOf("Tomato", "Mango")
        }
        expectThat(
            filter.matches(customer)
        ).isTrue()
    }

    @Test
    fun `test NotIn operation with false outcome`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::favoriteFruits NotIn listOf("Apricot", "Apple")
        }
        expectThat(
            filter.matches(customer)
        ).isFalse()
    }

    @Test
    fun `test GreaterThan operation happy flow`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::age GreaterThan 50
        }
        expectThat(
            filter.matches(customer)
        ).isTrue()
    }

    @Test
    fun `test GreaterThan operation with false outcome`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::age GreaterThan 56
        }
        expectThat(
            filter.matches(customer)
        ).isFalse()
    }

    @Test
    fun `test GreaterEqual operation happy flow`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::age GreaterOrEqual 55
        }
        expectThat(
            filter.matches(customer)
        ).isTrue()
    }

    @Test
    fun `test GreaterEqual operation with false outcome`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::age GreaterOrEqual 56
        }
        expectThat(
            filter.matches(customer)
        ).isFalse()
    }

    @Test
    fun `test LowerThan operation happy flow`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::age LowerThan 60
        }
        expectThat(
            filter.matches(customer)
        ).isTrue()
    }

    @Test
    fun `test LowerThan operation with false outcome`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::age LowerThan 54
        }
        expectThat(
            filter.matches(customer)
        ).isFalse()
    }

    @Test
    fun `test LowerEqual operation happy flow`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::age LowerOrEqual 55
        }
        expectThat(
            filter.matches(customer)
        ).isTrue()
    }

    @Test
    fun `test LowerEqual operation with false outcome`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::age LowerOrEqual 54
        }
        expectThat(
            filter.matches(customer)
        ).isFalse()
    }

    @Test
    fun `test Between operation happy flow`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::creationTime Between Date(0) And Date(2000)
        }
        expectThat(
            filter.matches(customer)
        ).isTrue()
    }

    @Test
    fun `test Between operation with false outcome`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::creationTime Between Date(1500) And Date(2000)
        }
        expectThat(
            filter.matches(customer)
        ).isFalse()
    }

    @Test
    fun `test Contains operation happy flow`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::name Contains "Te"
        }
        expectThat(
            filter.matches(customer)
        ).isTrue()
    }

    @Test
    fun `test Contains operation with false outcome`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::name Contains "John"
        }
        expectThat(
            filter.matches(customer)
        ).isFalse()
    }

    @Test
    fun `test StartsWith operation happy flow`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::name StartsWith "Te"
        }
        expectThat(
            filter.matches(customer)
        ).isTrue()
    }

    @Test
    fun `test StartsWith  operation with false outcome`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::name StartsWith "De"
        }
        expectThat(
            filter.matches(customer)
        ).isFalse()
    }

    @Test
    fun `test EndsWith operation happy flow`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::name EndsWith "st"
        }
        expectThat(
            filter.matches(customer)
        ).isTrue()
    }

    @Test
    fun `test EndsWith  operation with false outcome`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::name EndsWith "dt"
        }
        expectThat(
            filter.matches(customer)
        ).isFalse()
    }

    @Test
    fun `test IsNull operation happy flow`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::zodiacSign.isNull()
        }
        expectThat(
            filter.matches(customer)
        ).isTrue()
    }

    @Test
    fun `test IsNull operation with false outcome`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::name.isNull()
        }
        expectThat(
            filter.matches(customer)
        ).isFalse()
    }

    @Test
    fun `test IsNotNull operation happy flow`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::name.isNotNull()
        }
        expectThat(
            filter.matches(customer)
        ).isTrue()
    }

    @Test
    fun `test IsNotNull operation with false outcome`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::zodiacSign.isNotNull()
        }
        expectThat(
            filter.matches(customer)
        ).isFalse()
    }

    @Test
    fun `test ContainsIn operation happy flow`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::favoriteFruits ContainsIn listOf("App")
        }

        expectThrows<UnsupportedOperationException> {
            filter.matches(customer)
        }
    }

    @Test
    fun `test NotContainsIn operation happy flow`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::favoriteFruits NotContainsIn listOf("App")
        }

        expectThrows<UnsupportedOperationException> {
            filter.matches(customer)
        }
    }

    @Test
    fun `test RawJunction operation happy flow`() {
        val customer = Customer()
        val filter = where<Customer> {
            rawJunction {
                object : BaseRawJunction<Any>(1) {}
            }
        }

        expectThrows<UnsupportedOperationException> {
            filter.matches(customer)
        }
    }

    @Test
    fun `test nested field matches`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::address Sub {
                Address::state Sub {
                    State::code Equal "CO"
                }
            }
        }
        expectThat(
            filter.matches(customer)
        ).isTrue()
    }

    @Test
    fun `test nested field doesn't match`() {
        val customer = Customer()
        val filter = where<Customer> {
            Customer::address Sub {
                Address::state Sub {
                    State::active NotEqual true
                }
            }
        }
        expectThat(
            filter.matches(customer)
        ).isFalse()
    }
}