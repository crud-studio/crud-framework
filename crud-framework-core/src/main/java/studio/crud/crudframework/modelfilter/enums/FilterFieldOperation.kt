package studio.crud.crudframework.modelfilter.enums

enum class FilterFieldOperation(val junction: Boolean = false) {
    Equal,
    NotEqual,
    In,
    NotIn,
    GreaterThan,
    GreaterEqual,
    LowerThan,
    LowerEqual,
    Between,
    Contains,
    IsNull,
    IsNotNull,
    And(true),
    Or(true),
    Not(true),
    ContainsIn,
    NotContainsIn,
    StartsWith,
    EndsWith,
    Noop;
}