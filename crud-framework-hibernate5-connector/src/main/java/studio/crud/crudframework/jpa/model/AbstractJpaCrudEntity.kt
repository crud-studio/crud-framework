package studio.crud.crudframework.jpa.model

import dev.krud.shapeshift.resolver.annotation.MappedField
import studio.crud.crudframework.jpa.annotation.JpaCrudEntity
import studio.crud.crudframework.jpa.ro.AbstractJpaCrudRO
import studio.crud.crudframework.model.BaseCrudEntity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
@JpaCrudEntity
abstract class AbstractJpaCrudEntity : BaseCrudEntity<Long>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @MappedField(target = AbstractJpaCrudRO::class)
    final override var id: Long = 0L

    final override fun exists(): Boolean = id != null && id != 0L

    final override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        other as AbstractJpaCrudEntity?
        if (this === other) return true
        if (this.id == other.id) return true
        return true
    }

    final override fun hashCode(): Int {
        if (id == 0L) {
            return System.identityHashCode(this)
        }
        return id.hashCode()
    }
}