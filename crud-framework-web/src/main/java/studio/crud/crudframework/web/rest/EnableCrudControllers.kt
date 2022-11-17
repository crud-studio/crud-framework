package studio.crud.crudframework.web.rest

import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Import(CrudControllerConfiguration::class)
annotation class EnableCrudControllers