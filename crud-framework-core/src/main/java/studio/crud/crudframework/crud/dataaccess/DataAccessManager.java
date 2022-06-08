package studio.crud.crudframework.crud.dataaccess;

import org.jetbrains.annotations.NotNull;
import studio.crud.crudframework.modelfilter.DynamicModelFilter;
import studio.crud.crudframework.utils.component.componentmap.annotation.ComponentMapKey;

public interface DataAccessManager<Accessor, AccessorId, Entity> {

	@ComponentMapKey
	String getKey();

	default void decorateViewOperation(@NotNull DynamicModelFilter filter, @NotNull AccessorId accessorId, Class<Accessor> accessorClazz) {
	}

	default void decorateUpdateOperation(@NotNull DynamicModelFilter filter, @NotNull AccessorId accessorId, @NotNull Class<Accessor> accessorClazz) {
	}

	default void decorateCreateOperation(@NotNull Entity entity, @NotNull AccessorId accessorId, @NotNull Class<Accessor> accessorClazz) {
	}

}
