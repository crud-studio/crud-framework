package studio.crud.crudframework.crud.model

import studio.crud.crudframework.crud.hooks.HooksDTO

data class ReadRequestContext<PreHook, OnHook, PostHook, EntityType>(
        val hooksDTO: HooksDTO<PreHook, OnHook, PostHook>,
        val fromCache: Boolean,
        val persistCopy: Boolean,
        val applyDefaultPolicies: Boolean,
)