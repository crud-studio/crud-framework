package studio.crud.crudframework.crud.policy

import studio.crud.crudframework.model.PersistentEntity
import java.security.Principal

fun main() {
    // Alerts
    class Alert(val userId: Long) : PersistentEntity
    val alertPolicy = policy<Alert> {
        canAccess {
            filter { principal ->
                Alert::userId Equal principal.name.toLong()
            }
        }
    }

    // if request status == x then filter by y
    // else if request status == y then filter by z

    // takeme
    // if ride.status == pending then filter { principal.id == ride.riderId }
    // if ride.status == waitingForPickup then filter { principal.id == ride.driverId }
    class Ride(val status: String, val riderId: Long, val driverId: Long) : PersistentEntity
    val ridePolicy = policy<Ride> {
        canAccess {
            filter { principal ->
                or {
                    and {
                        Ride::status Equal "pending"
                        Ride::riderId Equal principal.name.toLong()
                    }
                    and {
                        Ride::status Equal "waitingForPickup"
                        Ride::driverId Equal principal.name.toLong()
                    }
                }
            }
        }
    }

    // velotix
    // if request.status == nodePending then condition { principal.hasRole("CONSUMER") }

    class Request(val status: String) : PersistentEntity
    val requestPolicy = policy<Request> {
        canAccess {
            filter {
                if(it.name == "consumer") {
                    Request::status Equal "nodePending"
                }
            }
        }
    }

    // val operator = operatorHandler.getOperator(accessorId)
    //
    //filter.add(and { "role.permissionsLevel" LowerOrEqual (operator.role?.permissionsLevel ?: 0) })
    //
    //val operatorBusinessIds = businessHandler.getOperatorBusinessIds(operator)
    //if (operatorBusinessIds != null) {
    //    filter.add(and { "businessId" In operatorBusinessIds })
    //}
    //
    //if (!PermissionUtils.hasPermission(operator, Permission.READ_OPERATORS, PermissionScope.All)) {
    //    filter.add(and { "id" Equal operator.id })
    //}
    //
    //if (!PermissionUtils.hasPermission(operator, Permission.SEE_TEST_OPERATORS)) {
    //    filter.add(and { "isTest" Equal false })
    //}
    //
    //if (!PermissionUtils.hasPermission(operator, Permission.MASTER_ROLE)) {
    //    val validRoleIds: List<Long> = roleHandler.getRoles(RoleModelFilter().setIsDeleted(false))
    //        .stream()
    //        .filter { r: Role? -> !PermissionUtils.hasPermission(r, Permission.MASTER_ROLE) }
    //        .map { r: Role -> r.id }
    //        .collect(Collectors.toList<Long>())
    //    filter.add(and { "roleId" In validRoleIds })
    //}
    class Operator(val permissionsLevel: Int, val businessId: Long, val id: Long, val isTest: Boolean, val roleId: Long) : PersistentEntity
    fun Principal.hasPermission(permission: String) = false
    fun Principal.permissionsLevel() = 5
    val operatorPolicy = policy<Operator> {
        canAccess {
            filter { principal ->
                Operator::permissionsLevel LowerOrEqual (principal.permissionsLevel())
                val operatorBusinessIds = listOf(1L, 2L, 3L)
                Operator::businessId In operatorBusinessIds
                if (!principal.hasPermission("READ_OPERATORS")) {
                    Operator::id Equal principal.name.toLong()
                }

                if (!principal.hasPermission("SEE_TEST_OPERATORS")) {
                    Operator::isTest Equal false
                }

                if (!principal.hasPermission("MASTER_ROLE")) {
                    val validRoleIds = listOf(1L, 2L, 3L)
                    Operator::roleId In validRoleIds
                }
            }
        }
    }
    //if (!PermissionUtils.hasPermission(operator, Permission.SEE_TEST_USERS)) {
    //    filter.add(and { "isTest" Equal false })
    //}
    //
    //if (!PermissionUtils.hasPermission(operator, Permission.SEE_HIDDEN_RIDES)) {
    //    filter.add(and { "hidden" Equal false })
    //}
    //
    //val operatorBusinessIds = businessHandler.getOperatorBusinessIds(operator)
    //if (operatorBusinessIds != null) {
    //    filter.add(and { "businessId" In operatorBusinessIds })
    //}
    //
    //if (!PermissionUtils.hasPermission(operator, Permission.READ_RIDE, PermissionScope.All)) {
    //    filter.add(and { "orderOperatorId" Equal operator.id })
    //}
    val anotherOperatorPolicy = policy<Operator> {
        canAccess {
            filter { principal ->
                if (!principal.hasPermission("SEE_TEST_USERS")) {
                    Operator::isTest Equal false
                }

                if (!principal.hasPermission("SEE_HIDDEN_RIDES")) {
                    Operator::isTest Equal false
                }

                val operatorBusinessIds = listOf(1L, 2L, 3L)
                Operator::businessId In operatorBusinessIds

                if (!principal.hasPermission("READ_RIDE")) {
                    Operator::id Equal principal.name.toLong()
                }
            }
        }
    }
}