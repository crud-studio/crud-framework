package studio.crud.crudframework.jpa.lazyinitializer.annotation

import studio.crud.crudframework.crud.annotation.WithHooks
import studio.crud.crudframework.jpa.lazyinitializer.LazyInitializerPersistentHooks

@WithHooks(hooks = [LazyInitializerPersistentHooks::class])
annotation class DynamicLazyInitialization

