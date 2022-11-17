package studio.crud.crudframework.web.rest

import studio.crud.crudframework.ro.BaseRO
import kotlin.reflect.KClass

annotation class RoMapping(
        val mainRoClass: KClass<*> = BaseRO::class,
        val showRoClass: KClass<*> = BaseRO::class,
        val createRoClass: KClass<*> = BaseRO::class,
        val updateRoClass: KClass<*> = BaseRO::class,
        val indexRoClass: KClass<*> = BaseRO::class
)