package studio.crud.crudframework.utils.component.startup.annotation

import studio.crud.crudframework.utils.component.startup.configuration.PostStartupConfiguration
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Import(PostStartupConfiguration::class)
annotation class EnablePostStartup