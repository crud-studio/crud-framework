package studio.crud.crudframework.web.rest

import kotlin.reflect.KClass

annotation class RoMapping(
        val mainRoClass: KClass<*> = Unit::class,
        val showRoClass: KClass<*> = Unit::class,
        val createRoClass: KClass<*> = Unit::class,
        val updateRoClass: KClass<*> = Unit::class,
        val indexRoClass: KClass<*> = Unit::class
)