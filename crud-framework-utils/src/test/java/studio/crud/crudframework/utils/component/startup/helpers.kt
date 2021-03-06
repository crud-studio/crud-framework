package studio.crud.crudframework.utils.component.startup

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import studio.crud.crudframework.utils.component.startup.annotation.PostStartUp

@Component // Needed since PostStartUp currently explicitly checks for @Component, remove when fixed
class PostStartUpUser {
    var initCalled = false

    @PostStartUp
    private fun init() {
        initCalled = true
    }
}

@Configuration
class PostStartUpTestConfig {
    @Bean
    fun postStartUpUser() = PostStartUpUser()
}