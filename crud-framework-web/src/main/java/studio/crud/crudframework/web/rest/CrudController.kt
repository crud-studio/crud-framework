package studio.crud.crudframework.web.rest

annotation class CrudController(
    val type: String,
    val actions: CrudActions = CrudActions(),
    val preAuthorize: CrudPreAuthorize = CrudPreAuthorize(),
    val roMapping: RoMapping = RoMapping()
)