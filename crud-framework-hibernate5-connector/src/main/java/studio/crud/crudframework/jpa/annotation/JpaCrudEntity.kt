package studio.crud.crudframework.jpa.annotation

import studio.crud.crudframework.crud.annotation.CrudEntity
import studio.crud.crudframework.jpa.dao.CrudDaoImpl

@CrudEntity(dao = CrudDaoImpl::class)
annotation class JpaCrudEntity