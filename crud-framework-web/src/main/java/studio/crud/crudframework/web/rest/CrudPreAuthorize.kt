package studio.crud.crudframework.web.rest

annotation class CrudPreAuthorize(
        val create: String = "", val delete: String = "", val update: String = "", val show: String = "", val index: String = "")