package studio.crud.crudframework.crud.model

import studio.crud.crudframework.crud.hooks.HooksDTO

data class MassUpdateRequestContext<PreHook, OnHook, PostHook, EntityType>(
        val hooksDTO: HooksDTO<PreHook, OnHook, PostHook>, val persistCopy: Boolean, val applyDefaultPolicies: Boolean)