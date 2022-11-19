package studio.crud.crudframework.crud.security

import java.security.Principal

interface PrincipalProvider {
    fun getPrincipal(): Principal?
}