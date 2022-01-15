package studio.crud.crudframework.jpa.annotation

import studio.crud.crudframework.crud.annotation.EnableCrudFramework
import studio.crud.crudframework.jpa.config.CrudHibernate5ConnectorConfiguration
import org.springframework.context.annotation.Import

@EnableCrudFramework
@Import(CrudHibernate5ConnectorConfiguration::class)
annotation class EnableJpaCrud