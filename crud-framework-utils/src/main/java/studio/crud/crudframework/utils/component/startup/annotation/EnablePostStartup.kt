package studio.crud.crudframework.utils.component.startup.annotation

import org.springframework.context.annotation.Import
import studio.crud.crudframework.utils.component.startup.configuration.PostStartupConfiguration

@Target(AnnotationTarget.CLASS)
@Import(PostStartupConfiguration::class)
annotation class EnablePostStartup