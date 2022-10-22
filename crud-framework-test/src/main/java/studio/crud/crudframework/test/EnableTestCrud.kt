package studio.crud.crudframework.test

import org.springframework.context.annotation.Import
import studio.crud.crudframework.crud.annotation.EnableCrudFramework

/**
 * Enable the Crud Framework with the `TestCrudDaoImpl` dao
 */
@EnableCrudFramework
@Import(TestCrudConfiguration::class)
annotation class EnableTestCrud()
