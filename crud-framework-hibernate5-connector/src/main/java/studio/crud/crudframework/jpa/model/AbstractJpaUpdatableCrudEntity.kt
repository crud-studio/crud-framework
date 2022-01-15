package studio.crud.crudframework.jpa.model

import studio.crud.crudframework.fieldmapper.annotation.MappedField
import studio.crud.crudframework.jpa.ro.AbstractJpaCrudRO
import studio.crud.crudframework.jpa.ro.AbstractJpaUpdatableCrudRO
import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*

@MappedSuperclass
abstract class AbstractJpaUpdatableCrudEntity : AbstractJpaCrudEntity() {
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column
    @MappedField(target = AbstractJpaUpdatableCrudRO::class)
    val creationTime: Date = Date()

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @Version
    @MappedField(target = AbstractJpaUpdatableCrudRO::class)
    var lastUpdateTime: Date = Date()
}