package studio.crud.crudframework.crud.handler;

import studio.crud.crudframework.model.BaseCrudEntity;
import studio.crud.crudframework.modelfilter.DynamicModelFilter;

import java.io.Serializable;
import java.util.List;

public interface CrudDao {
	<ID extends Serializable, Entity extends BaseCrudEntity<ID>, E extends DynamicModelFilter> List<Entity> index(E filter, Class<Entity> clazz);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>, E extends DynamicModelFilter> long indexCount(E filter, Class<Entity> clazz);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> void hardDeleteById(ID id, Class<Entity> clazz);

	<ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity saveOrUpdate(Entity entity);
}
