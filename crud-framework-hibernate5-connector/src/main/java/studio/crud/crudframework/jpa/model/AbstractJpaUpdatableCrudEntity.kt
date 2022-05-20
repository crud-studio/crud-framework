package studio.crud.crudframework.jpa.model

import dev.krud.shapeshift.annotation.MappedField
import org.hibernate.annotations.CreationTimestamp
import studio.crud.crudframework.jpa.ro.AbstractJpaUpdatableCrudRO
import java.util.*
import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Version

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