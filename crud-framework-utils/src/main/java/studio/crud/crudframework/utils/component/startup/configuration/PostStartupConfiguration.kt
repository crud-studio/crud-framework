package studio.crud.crudframework.utils.component.startup.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import studio.crud.crudframework.utils.component.startup.PostStartupHandler

@Configuration
internal class PostStartupConfiguration {
    @Bean
    fun postStartupHandler(): PostStartupHandler {
        return PostStartupHandler()
    }
}