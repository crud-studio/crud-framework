package studio.crud.crudframework.mongo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import studio.crud.crudframework.crud.handler.CrudDao
import studio.crud.crudframework.mongo.dao.MongoCrudDaoImpl

@Configuration
class CrudMongoConnectorConfiguration {
    @Bean
    fun mongoCrudDao(): CrudDao = MongoCrudDaoImpl()
}