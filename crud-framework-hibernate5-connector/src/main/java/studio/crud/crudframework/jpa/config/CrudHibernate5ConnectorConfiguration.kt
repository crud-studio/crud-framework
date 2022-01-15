package studio.crud.crudframework.jpa.config

import studio.crud.crudframework.crud.handler.CrudDao
import studio.crud.crudframework.jpa.dao.CrudDaoImpl
import studio.crud.crudframework.jpa.lazyinitializer.LazyInitializerPersistentHooks
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CrudHibernate5ConnectorConfiguration {
    @Bean
    fun jpaCrudDao(): CrudDao = CrudDaoImpl()

    @Bean
    fun lazyInitializerPersistentHooks(): LazyInitializerPersistentHooks = LazyInitializerPersistentHooks()
}