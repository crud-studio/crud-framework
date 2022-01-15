package studio.crud.crudframework.crud.model

import studio.crud.crudframework.crud.annotation.WithHooks
import studio.crud.crudframework.crud.hooks.interfaces.CRUDHooks
import studio.crud.crudframework.jpa.model.AbstractJpaCrudEntity
import studio.crud.crudframework.model.BaseCrudEntity
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Table

class GenericPersistentHooks<ID : Serializable> : CRUDHooks<ID, BaseCrudEntity<ID>>

@WithHooks(hooks = [GenericPersistentHooks::class])
annotation class NestedWithHooks

@Entity
@Table(name = "test_kotlin_entity")
@NestedWithHooks
class TestKotlinEntity : AbstractJpaCrudEntity()