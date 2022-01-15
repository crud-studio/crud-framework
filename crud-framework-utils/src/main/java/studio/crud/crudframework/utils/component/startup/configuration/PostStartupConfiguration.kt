package studio.crud.crudframework.utils.component.startup.configuration

import studio.crud.crudframework.utils.component.startup.PostStartupHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PostStartupConfiguration {

    @Bean
    fun postStartupHandler(): PostStartupHandler {
        return PostStartupHandler()
    }
}