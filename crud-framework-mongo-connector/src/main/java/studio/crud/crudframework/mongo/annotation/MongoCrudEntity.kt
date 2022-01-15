package studio.crud.crudframework.mongo.annotation

import studio.crud.crudframework.crud.annotation.CrudEntity
import studio.crud.crudframework.mongo.dao.MongoCrudDaoImpl

@CrudEntity(dao = MongoCrudDaoImpl::class)
annotation class MongoCrudEntity