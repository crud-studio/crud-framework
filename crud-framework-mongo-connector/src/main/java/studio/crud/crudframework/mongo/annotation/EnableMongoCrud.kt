package studio.crud.crudframework.mongo.annotation

import studio.crud.crudframework.crud.annotation.EnableCrudFramework
import studio.crud.crudframework.mongo.config.CrudMongoConnectorConfiguration
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Import(CrudMongoConnectorConfiguration::class)
@EnableCrudFramework
annotation class EnableMongoCrud