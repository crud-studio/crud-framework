package studio.crud.crudframework.web.rest

annotation class CrudController(
        val resourceName: String,
        val actions: CrudActions = CrudActions(),
        val roMapping: RoMapping = RoMapping()
)