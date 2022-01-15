package studio.crud.crudframework.crud.annotation

import studio.crud.crudframework.crud.configuration.CrudCacheConfiguration
import studio.crud.crudframework.crud.configuration.CrudFrameworkConfiguration
import studio.crud.crudframework.utils.component.componentmap.annotation.EnableComponentMap
import studio.crud.crudframework.utils.component.startup.annotation.EnablePostStartup
import org.springframework.context.annotation.Import


/**
 * Used to enable the framework
 * Enables the component map and post startup features
 * Additionally triggers [CrudFrameworkConfiguration]
 */
@Target(AnnotationTarget.CLASS)
@Import(CrudFrameworkConfiguration::class, CrudCacheConfiguration::class)
@EnableComponentMap
@EnablePostStartup
annotation class EnableCrudFramework