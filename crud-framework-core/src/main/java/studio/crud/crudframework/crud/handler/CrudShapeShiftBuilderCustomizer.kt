package studio.crud.crudframework.crud.handler

import dev.krud.shapeshift.ShapeShiftBuilder
import dev.krud.shapeshift.spring.ShapeShiftBuilderCustomizer
import dev.krud.shapeshift.transformer.DateToLongTransformer
import dev.krud.shapeshift.transformer.LongToDateTransformer
import org.springframework.beans.factory.annotation.Autowired
import studio.crud.crudframework.crud.configuration.properties.CrudFrameworkProperties

class CrudShapeShiftBuilderCustomizer : ShapeShiftBuilderCustomizer {
    @Autowired
    private lateinit var properties: CrudFrameworkProperties

    override fun customize(builder: ShapeShiftBuilder) {
        if (properties.defaultTransformersEnabled) {
            builder
                .withTransformer(DateToLongTransformer(), true)
                .withTransformer(LongToDateTransformer(), true)
        }
    }
}
