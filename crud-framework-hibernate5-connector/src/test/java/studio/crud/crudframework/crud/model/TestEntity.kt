package studio.crud.crudframework.crud.model

import studio.crud.crudframework.fieldmapper.annotation.MappedField
import studio.crud.crudframework.fieldmapper.annotation.MappedFields
import studio.crud.crudframework.fieldmapper.transformer.CommaDelimitedStringToListTransformer
import studio.crud.crudframework.fieldmapper.transformer.DateToLongTransformer
import studio.crud.crudframework.fieldmapper.transformer.LongToCurrencyDoubleTransformer
import studio.crud.crudframework.fieldmapper.transformer.ToStringTransformer
import studio.crud.crudframework.jpa.model.AbstractJpaUpdatableCrudEntity
import java.util.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "test_entity")
class TestEntity : AbstractJpaUpdatableCrudEntity {
    @MappedField(target = TestEntityRO::class, transformer = CommaDelimitedStringToListTransformer::class, mapTo = "stringList")
    var commaDelimitedString = "val1,val2,val3,val4"

    @MappedField(target = TestEntityRO::class, transformer = LongToCurrencyDoubleTransformer::class, mapTo = "doubleCurrency")
    var longCurrency = 100000L

    @MappedFields(
        MappedField(target = TestEntityRO::class),
        MappedField(target = TestEntityRO::class, transformer = ToStringTransformer::class, mapTo = "genericVariableString")
    )
    var genericVariable = 198283L

    @MappedField(target = TestEntityRO::class, transformer = DateToLongTransformer::class)
    var date = Date(10000L)

    constructor() {}
    constructor(commaDelimitedString: String, longCurrency: Long, genericVariable: Long, date: Date) {
        this.commaDelimitedString = commaDelimitedString
        this.longCurrency = longCurrency
        this.genericVariable = genericVariable
        this.date = date
    }
}