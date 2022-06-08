package studio.crud.crudframework.mongo.annotation

import org.springframework.context.annotation.Import
import studio.crud.crudframework.crud.annotation.EnableCrudFramework
import studio.crud.crudframework.mongo.config.CrudMongoConnectorConfiguration

@Target(AnnotationTarget.CLASS)
@Import(CrudMongoConnectorConfiguration::class)
@EnableCrudFramework
annotation class EnableMongoCrud