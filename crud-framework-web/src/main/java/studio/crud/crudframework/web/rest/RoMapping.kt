package studio.crud.crudframework.web.rest

import studio.crud.crudframework.ro.BaseRO
import kotlin.reflect.KClass

annotation class RoMapping(
        val mainRoClass: KClass<out BaseRO<*>> = BaseRO::class,
        val showRoClass: KClass<out BaseRO<*>> = BaseRO::class,
        val createRoClass: KClass<out BaseRO<*>> = BaseRO::class,
        val updateRoClass: KClass<out BaseRO<*>> = BaseRO::class,
        val indexRoClass: KClass<out BaseRO<*>> = BaseRO::class
)