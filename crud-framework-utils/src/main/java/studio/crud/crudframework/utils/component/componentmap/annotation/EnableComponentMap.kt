package studio.crud.crudframework.utils.component.componentmap.annotation

import org.springframework.context.annotation.Import
import studio.crud.crudframework.utils.component.componentmap.configuration.ComponentMapConfiguration

@Target(AnnotationTarget.CLASS)
@Import(ComponentMapConfiguration::class)
annotation class EnableComponentMap