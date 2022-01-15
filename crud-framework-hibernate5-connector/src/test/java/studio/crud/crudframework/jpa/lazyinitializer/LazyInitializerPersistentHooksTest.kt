package studio.crud.crudframework.jpa.lazyinitializer

import studio.crud.crudframework.modelfilter.DynamicModelFilter
import studio.crud.crudframework.ro.PagingDTO
import studio.crud.crudframework.ro.PagingRO
import org.junit.Assert.*
import org.junit.Test

class LazyInitializerPersistentHooksTest {

    @Test
    fun `test index hook doesn't fail on null result`() {
        val subject = LazyInitializerPersistentHooks()
        subject.onIndex(DynamicModelFilter(), PagingDTO(PagingRO(0, 20, 100), null))
    }
}