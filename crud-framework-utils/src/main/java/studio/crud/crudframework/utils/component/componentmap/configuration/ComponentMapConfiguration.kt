package studio.crud.crudframework.utils.component.componentmap.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import studio.crud.crudframework.utils.component.componentmap.ComponentMapPostProcessor

@Configuration
class ComponentMapConfiguration {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun componentMapPostProcessor(): ComponentMapPostProcessor {
        return ComponentMapPostProcessor()
    }
}