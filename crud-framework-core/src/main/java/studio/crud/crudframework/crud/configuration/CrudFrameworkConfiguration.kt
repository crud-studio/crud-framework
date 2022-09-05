package studio.crud.crudframework.crud.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import studio.crud.crudframework.crud.configuration.properties.CrudFrameworkProperties
import studio.crud.crudframework.crud.handler.CrudCreateHandler
import studio.crud.crudframework.crud.handler.CrudCreateHandlerImpl
import studio.crud.crudframework.crud.handler.CrudDeleteHandler
import studio.crud.crudframework.crud.handler.CrudDeleteHandlerImpl
import studio.crud.crudframework.crud.handler.CrudHandler
import studio.crud.crudframework.crud.handler.CrudHandlerImpl
import studio.crud.crudframework.crud.handler.CrudHelper
import studio.crud.crudframework.crud.handler.CrudHelperImpl
import studio.crud.crudframework.crud.handler.CrudReadHandler
import studio.crud.crudframework.crud.handler.CrudReadHandlerImpl
import studio.crud.crudframework.crud.handler.CrudUpdateHandler
import studio.crud.crudframework.crud.handler.CrudUpdateHandlerImpl
import studio.crud.crudframework.exception.WrapExceptionAspect

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