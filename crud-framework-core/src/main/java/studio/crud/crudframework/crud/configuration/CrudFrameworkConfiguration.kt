package studio.crud.crudframework.crud.configuration

import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import studio.crud.crudframework.crud.configuration.properties.CrudFrameworkProperties
import studio.crud.crudframework.crud.handler.*
import studio.crud.crudframework.crud.policy.Policy
import studio.crud.crudframework.crud.security.PrincipalProvider
import studio.crud.crudframework.exception.WrapExceptionAspect
import studio.crud.crudframework.model.PersistentEntity

@Configuration
@EnableConfigurationProperties(CrudFrameworkProperties::class)
class CrudFrameworkConfiguration {

    @Bean
    fun crudHandler(): CrudHandler = CrudHandlerImpl()

    @Bean
    fun crudHelper(): CrudHelper = CrudHelperImpl()

    @Bean
    fun crudCreateHandler(): CrudCreateHandler = CrudCreateHandlerImpl()

    @Bean
    fun crudDeleteHandler(): CrudDeleteHandler = CrudDeleteHandlerImpl()

    @Bean
    fun crudUpdateHandler(): CrudUpdateHandler = CrudUpdateHandlerImpl()

    @Bean
    fun crudReadHandler(): CrudReadHandler = CrudReadHandlerImpl()

    @Bean
    fun crudSecurityHandler(policies: ObjectProvider<Policy<PersistentEntity>>, principalProvider: ObjectProvider<PrincipalProvider>): CrudSecurityHandler {
        return CrudSecurityHandlerImpl(policies, principalProvider)
    }

    @Bean
    fun wrapExceptionAspect(): WrapExceptionAspect = WrapExceptionAspect()
}