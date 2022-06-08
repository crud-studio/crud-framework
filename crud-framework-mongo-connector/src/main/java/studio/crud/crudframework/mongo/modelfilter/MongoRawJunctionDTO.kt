package studio.crud.crudframework.mongo.modelfilter

import org.springframework.data.mongodb.core.query.Criteria
import studio.crud.crudframework.modelfilter.BaseRawJunction

class MongoRawJunctionDTO(junction: Criteria) : BaseRawJunction<Criteria>(junction)