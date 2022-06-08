package studio.crud.crudframework.jpa.annotation

import org.springframework.context.annotation.Import
import studio.crud.crudframework.crud.annotation.EnableCrudFramework
import studio.crud.crudframework.jpa.config.CrudHibernate5ConnectorConfiguration

@EnableCrudFramework
@Import(CrudHibernate5ConnectorConfiguration::class)
annotation class EnableJpaCrud