package studio.crud.crudframework.crud.annotation

import org.springframework.context.annotation.Import
import studio.crud.crudframework.crud.configuration.CrudCacheConfiguration
import studio.crud.crudframework.crud.configuration.CrudFrameworkConfiguration
import studio.crud.crudframework.utils.component.startup.annotation.EnablePostStartup

/**
 * Used to enable the framework
 * Enables the component map and post startup features
 * Additionally triggers [CrudFrameworkConfiguration]
 */
@Target(AnnotationTarget.CLASS)
@Import(CrudFrameworkConfiguration::class, CrudCacheConfiguration::class)
@EnablePostStartup
annotation class EnableCrudFramework