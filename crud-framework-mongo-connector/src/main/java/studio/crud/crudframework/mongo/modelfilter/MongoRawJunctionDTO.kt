package studio.crud.crudframework.mongo.modelfilter

import studio.crud.crudframework.modelfilter.BaseRawJunction
import org.springframework.data.mongodb.core.query.Criteria

class MongoRawJunctionDTO(junction: Criteria) : BaseRawJunction<Criteria>(junction) {
}