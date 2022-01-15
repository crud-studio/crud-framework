package studio.crud.crudframework.crud.enums

/**
 * The mode to use when running a [studio.crud.crudframework.crud.handler.CrudHandler.showBy] operation
 * The selected mode will determine the behaviour when more than one record is returned for the query
 */
enum class ShowByMode {
    /**
     * Will throw a [studio.crud.crudframework.crud.exception.CrudReadException]
     */
    THROW_EXCEPTION,
    /**
     * Will return the first record
     */
    RETURN_FIRST,

    /**
     * Will return a random record
     */
    RETURN_RANDOM
}