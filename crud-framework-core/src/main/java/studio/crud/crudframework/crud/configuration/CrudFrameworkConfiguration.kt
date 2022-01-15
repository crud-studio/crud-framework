package studio.crud.crudframework.crud.configuration

import studio.crud.crudframework.crud.configuration.properties.CrudFrameworkProperties
import studio.crud.crudframework.crud.handler.*
import studio.crud.crudframework.exception.WrapExceptionAspect
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

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
    fun wrapExceptionAspect(): WrapExceptionAspect = WrapExceptionAspect()
}