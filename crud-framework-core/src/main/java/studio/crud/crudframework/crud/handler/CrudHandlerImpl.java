package studio.crud.crudframework.crud.handler;

import org.springframework.beans.factory.annotation.Autowired;
import studio.crud.crudframework.crud.enums.ShowByMode;
import studio.crud.crudframework.crud.exception.CrudException;
import studio.crud.crudframework.crud.hooks.create.CRUDOnCreateHook;
import studio.crud.crudframework.crud.hooks.create.CRUDPostCreateHook;
import studio.crud.crudframework.crud.hooks.create.CRUDPreCreateHook;
import studio.crud.crudframework.crud.hooks.create.from.CRUDOnCreateFromHook;
import studio.crud.crudframework.crud.hooks.create.from.CRUDPostCreateFromHook;
import studio.crud.crudframework.crud.hooks.create.from.CRUDPreCreateFromHook;
import studio.crud.crudframework.crud.hooks.delete.CRUDOnDeleteHook;
import studio.crud.crudframework.crud.hooks.delete.CRUDPostDeleteHook;
import studio.crud.crudframework.crud.hooks.delete.CRUDPreDeleteHook;
import studio.crud.crudframework.crud.hooks.index.CRUDOnIndexHook;
import studio.crud.crudframework.crud.hooks.index.CRUDPostIndexHook;
import studio.crud.crudframework.crud.hooks.index.CRUDPreIndexHook;
import studio.crud.crudframework.crud.hooks.show.CRUDOnShowHook;
import studio.crud.crudframework.crud.hooks.show.CRUDPostShowHook;
import studio.crud.crudframework.crud.hooks.show.CRUDPreShowHook;
import studio.crud.crudframework.crud.hooks.show.by.CRUDOnShowByHook;
import studio.crud.crudframework.crud.hooks.show.by.CRUDPostShowByHook;
import studio.crud.crudframework.crud.hooks.show.by.CRUDPreShowByHook;
import studio.crud.crudframework.crud.hooks.update.CRUDOnUpdateHook;
import studio.crud.crudframework.crud.hooks.update.CRUDPostUpdateHook;
import studio.crud.crudframework.crud.hooks.update.CRUDPreUpdateHook;
import studio.crud.crudframework.crud.hooks.update.from.CRUDOnUpdateFromHook;
import studio.crud.crudframework.crud.hooks.update.from.CRUDPostUpdateFromHook;
import studio.crud.crudframework.crud.hooks.update.from.CRUDPreUpdateFromHook;
import studio.crud.crudframework.crud.model.MassUpdateCRUDRequestBuilder;
import studio.crud.crudframework.crud.model.ReadCRUDRequestBuilder;
import studio.crud.crudframework.crud.model.UpdateCRUDRequestBuilder;
import studio.crud.crudframework.exception.WrapException;
import studio.crud.crudframework.model.BaseCrudEntity;
import studio.crud.crudframework.modelfilter.DynamicModelFilter;
import studio.crud.crudframework.ro.PagingDTO;
import studio.crud.crudframework.util.DynamicModelFilterUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@WrapException(CrudException.class)
public class CrudHandlerImpl implements CrudHandler {
	private CrudReadHandler crudReadHandler;

	private CrudUpdateHandler crudUpdateHandler;

	private CrudDeleteHandler crudDeleteHandler;

	private CrudCreateHandler crudCreateHandler;

	@Autowired
	public void setCrudReadHandler(CrudReadHandler crudReadHandler) {
		this.crudReadHandler = crudReadHandler;
	}

	@Autowired
	public void setCrudUpdateHandler(CrudUpdateHandler crudUpdateHandler) {
		this.crudUpdateHandler = crudUpdateHandler;
	}

	@Autowired
	public void setCrudDeleteHandler(CrudDeleteHandler crudDeleteHandler) {
		this.crudDeleteHandler = crudDeleteHandler;
	}

	@Autowired
	public void setCrudCreateHandler(CrudCreateHandler crudCreateHandler) {
		this.crudCreateHandler = crudCreateHandler;
	}

	@Autowired
	public void setCrudHelper(CrudHelper crudHelper) {
		this.crudHelper = crudHelper;
	}

	@Autowired
	private CrudHelper crudHelper;

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> ReadCRUDRequestBuilder<CRUDPreIndexHook<ID, Entity>, CRUDOnIndexHook<ID, Entity>, CRUDPostIndexHook<ID, Entity>, PagingDTO<Entity>> index(
			DynamicModelFilter filter, Class<Entity> clazz) {
		return new ReadCRUDRequestBuilder<>(
				(hooks, fromCache, persistCopy, accessorDTO) -> crudReadHandler.indexInternal(filter, clazz, hooks, fromCache, persistCopy, accessorDTO, false),
				(hooks, fromCache, persistCopy, accessorDTO) -> crudReadHandler.indexInternal(filter, clazz, hooks, fromCache, persistCopy, accessorDTO, true).getPagingRO().getTotal()
		);
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> ReadCRUDRequestBuilder<CRUDPreIndexHook<ID, Entity>, CRUDOnIndexHook<ID, Entity>, CRUDPostIndexHook<ID, Entity>, PagingDTO<RO>> index(
			DynamicModelFilter filter, Class<Entity> clazz, Class<RO> toClazz) {
		return new ReadCRUDRequestBuilder<>(
				(hooks, fromCache, persistCopy, accessorDTO) -> {
					PagingDTO<Entity> resultDTO = crudReadHandler.indexInternal(filter, clazz, hooks, fromCache, persistCopy, accessorDTO, false);
					List<RO> mappedResults = crudHelper.fillMany(resultDTO.getData(), toClazz);
					return new PagingDTO<>(resultDTO.getPagingRO(), mappedResults);
				}, (hooks, fromCache, persistCopy, accessorDTO) -> crudReadHandler.indexInternal(filter, clazz, hooks, fromCache, persistCopy, accessorDTO, true).getPagingRO().getTotal());
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> UpdateCRUDRequestBuilder<CRUDPreDeleteHook<ID, Entity>, CRUDOnDeleteHook<ID, Entity>, CRUDPostDeleteHook<ID, Entity>, Void> delete(ID id,
			Class<Entity> clazz) {
		return new UpdateCRUDRequestBuilder<>((hooks, accessorDTO) -> {
			crudDeleteHandler.deleteInternal(id, clazz, hooks, accessorDTO);
			return null;
		});
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> UpdateCRUDRequestBuilder<CRUDPreCreateFromHook<ID, Entity>, CRUDOnCreateFromHook<ID, Entity>, CRUDPostCreateFromHook<ID, Entity>, Entity> createFrom(
			Object object, Class<Entity> clazz) {
		return new UpdateCRUDRequestBuilder<>((hooks, accessorDTO) -> crudCreateHandler.createFromInternal(object, clazz, hooks, accessorDTO));
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> UpdateCRUDRequestBuilder<CRUDPreCreateFromHook<ID, Entity>, CRUDOnCreateFromHook<ID, Entity>, CRUDPostCreateFromHook<ID, Entity>, RO> createFrom(
			Object object, Class<Entity> clazz, Class<RO> toClazz) {
		return new UpdateCRUDRequestBuilder<>((hooks, accessorDTO) -> {
			Entity result = crudCreateHandler.createFromInternal(object, clazz, hooks, accessorDTO);
			return crudHelper.fill(result, toClazz);
		});
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> UpdateCRUDRequestBuilder<CRUDPreCreateHook<ID, Entity>, CRUDOnCreateHook<ID, Entity>, CRUDPostCreateHook<ID, Entity>, Entity> create(
			Entity entity) {
		return new UpdateCRUDRequestBuilder<>((hooks, accessorDTO) -> crudCreateHandler.createInternal(entity, hooks, accessorDTO));
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> UpdateCRUDRequestBuilder<CRUDPreCreateHook<ID, Entity>, CRUDOnCreateHook<ID, Entity>, CRUDPostCreateHook<ID, Entity>, RO> create(Entity entity,
			Class<RO> toClazz) {
		return new UpdateCRUDRequestBuilder<>((hooks, accessorDTO) -> {
			Entity result = crudCreateHandler.createInternal(entity, hooks, accessorDTO);
			return crudHelper.fill(result, toClazz);
		});
	}


	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> UpdateCRUDRequestBuilder<CRUDPreUpdateFromHook<ID, Entity>, CRUDOnUpdateFromHook<ID, Entity>, CRUDPostUpdateFromHook<ID, Entity>, Entity> updateFrom(
			ID id, Object object, Class<Entity> clazz) {
		return new UpdateCRUDRequestBuilder<>((hooks, accessorDTO) -> crudUpdateHandler.updateFromInternal(id, object, clazz, hooks, accessorDTO));
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> UpdateCRUDRequestBuilder<CRUDPreUpdateFromHook<ID, Entity>, CRUDOnUpdateFromHook<ID, Entity>, CRUDPostUpdateFromHook<ID, Entity>, RO> updateFrom(
			ID id, Object object, Class<Entity> clazz, Class<RO> toClazz) {
		return new UpdateCRUDRequestBuilder<>((hooks, accessorDTO) -> {
			Entity result = crudUpdateHandler.updateFromInternal(id, object, clazz, hooks, accessorDTO);
			return crudHelper.fill(result, toClazz);
		});
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> UpdateCRUDRequestBuilder<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>, Entity> update(
			Entity entity) {
		return new UpdateCRUDRequestBuilder<>((hooks, accessorDTO) -> crudUpdateHandler.updateInternal(entity, hooks, accessorDTO));
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> UpdateCRUDRequestBuilder<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>, RO> update(Entity entity,
			Class<RO> toClazz) {
		return new UpdateCRUDRequestBuilder<>((hooks, accessorDTO) -> {
			Entity result = crudUpdateHandler.updateInternal(entity, hooks, accessorDTO);
			return crudHelper.fill(result, toClazz);
		});
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> MassUpdateCRUDRequestBuilder<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>, List<Entity>> update(
			List<Entity> entities) {
		return new MassUpdateCRUDRequestBuilder<>((hooks, persistCopy, accessorDTO) -> crudUpdateHandler.updateManyTransactional(entities, hooks, persistCopy, accessorDTO));
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> MassUpdateCRUDRequestBuilder<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>, List<RO>> update(
			List<Entity> entities, Class<RO> toClazz) {
		return new MassUpdateCRUDRequestBuilder<>((hooks, persistCopy, accessorDTO) -> {
			List<Entity> result = crudUpdateHandler.updateManyTransactional(entities, hooks, persistCopy, accessorDTO);
			return crudHelper.fillMany(result, toClazz);
		});
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> MassUpdateCRUDRequestBuilder<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>, List<Entity>> updateByFilter(
			DynamicModelFilter filter, Class<Entity> entityClazz) {
		return new MassUpdateCRUDRequestBuilder<>((hooks, persistCopy, accessorDTO) -> crudUpdateHandler.updateByFilterTransactional(filter, entityClazz, hooks, persistCopy, accessorDTO));
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> MassUpdateCRUDRequestBuilder<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>, List<RO>> updateByFilter(
			DynamicModelFilter filter, Class<Entity> entityClazz, Class<RO> toClazz) {
		return new MassUpdateCRUDRequestBuilder<>((hooks, persistCopy, accessorDTO) -> {
			List<Entity> result = crudUpdateHandler.updateByFilterTransactional(filter, entityClazz, hooks, persistCopy, accessorDTO);
			return crudHelper.fillMany(result, toClazz);
		});
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> ReadCRUDRequestBuilder<CRUDPreShowByHook<ID, Entity>, CRUDOnShowByHook<ID, Entity>, CRUDPostShowByHook<ID, Entity>, Entity> showBy(
			DynamicModelFilter filter, Class<Entity> clazz) {
		return showBy(filter, clazz, ShowByMode.THROW_EXCEPTION);
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> ReadCRUDRequestBuilder<CRUDPreShowByHook<ID, Entity>, CRUDOnShowByHook<ID, Entity>, CRUDPostShowByHook<ID, Entity>, Entity> showBy(
			DynamicModelFilter filter, Class<Entity> clazz, ShowByMode mode) {
		return new ReadCRUDRequestBuilder<>(
				(hooks, fromCache, persistCopy, accessorDTO) -> crudReadHandler.showByInternal(filter, clazz, hooks, fromCache, persistCopy, mode, accessorDTO),
				(hooks, fromCache, persistCopy, accessorDTO) -> crudReadHandler.showByInternal(filter, clazz, hooks, fromCache, persistCopy, mode, accessorDTO) != null ? 1L : 0L
		);
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> ReadCRUDRequestBuilder<CRUDPreShowByHook<ID, Entity>, CRUDOnShowByHook<ID, Entity>, CRUDPostShowByHook<ID, Entity>, RO> showBy(
			DynamicModelFilter filter, Class<Entity> clazz, Class<RO> toClazz) {
		return showBy(filter, clazz, toClazz, ShowByMode.THROW_EXCEPTION);
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> ReadCRUDRequestBuilder<CRUDPreShowByHook<ID, Entity>, CRUDOnShowByHook<ID, Entity>, CRUDPostShowByHook<ID, Entity>, RO> showBy(
			DynamicModelFilter filter, Class<Entity> clazz, Class<RO> toClazz, ShowByMode mode) {
		return new ReadCRUDRequestBuilder<>(
				(hooks, fromCache, persistCopy, accessorDTO) -> {
					Entity result = crudReadHandler.showByInternal(filter, clazz, hooks, fromCache, persistCopy, mode, accessorDTO);
					if(result == null) {
						return null;
					}

					return crudHelper.fill(result, toClazz);
				}, (hooks, fromCache, persistCopy, accessorDTO) -> crudReadHandler.showByInternal(filter, clazz, hooks, fromCache, persistCopy, mode, accessorDTO) != null ? 1L : 0L
		);
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> ReadCRUDRequestBuilder<CRUDPreShowHook<ID, Entity>, CRUDOnShowHook<ID, Entity>, CRUDPostShowHook<ID, Entity>, Entity> show(ID id,
			Class<Entity> clazz) {
		return new ReadCRUDRequestBuilder<>(
				(hooks, fromCache, persistCopy, accessorDTO) -> crudReadHandler.showInternal(id, clazz, hooks, fromCache, persistCopy, accessorDTO),
				(hooks, fromCache, persistCopy, accessorDTO) -> crudReadHandler.showInternal(id, clazz, hooks, fromCache, persistCopy, accessorDTO) != null ? 1L : 0L
		);
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> ReadCRUDRequestBuilder<CRUDPreShowHook<ID, Entity>, CRUDOnShowHook<ID, Entity>, CRUDPostShowHook<ID, Entity>, RO> show(ID id,
			Class<Entity> clazz, Class<RO> toClazz) {
		return new ReadCRUDRequestBuilder<>((hooks, fromCache, persistCopy, accessorDTO) -> {
			Entity result = crudReadHandler.showInternal(id, clazz, hooks, fromCache, persistCopy, accessorDTO);
			if(result == null) {
				return null;
			}

			return crudHelper.fill(result, toClazz);
		}, (hooks, fromCache, persistCopy, accessorDTO) -> crudReadHandler.showInternal(id, clazz, hooks, fromCache, persistCopy, accessorDTO) != null ? 1L : 0L
		);
	}

    @Override
	public void validate(Object target) {
		crudHelper.validate(target);
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> void validateFilter(DynamicModelFilter filter, Class<Entity> clazz) {
		crudHelper.validateAndFillFilterFieldMetadata(filter.getFilterFields(), clazz);
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> boolean filterMatches(DynamicModelFilter filter, Entity entity) {
		Objects.requireNonNull(entity, "'entity' cannot be null");
		validateFilter(filter, entity.getClass());
		return DynamicModelFilterUtils.filtersMatch(filter, entity);
	}
}
