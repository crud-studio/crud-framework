package studio.crud.crudframework.crud.model

import studio.crud.crudframework.crud.hooks.HooksDTO

data class UpdateRequestContext<PreHook, OnHook, PostHook, EntityType>(
        val hooksDTO: HooksDTO<PreHook, OnHook, PostHook>, val applyDefaultPolicies: Boolean)