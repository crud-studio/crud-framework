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
				(context) -> crudReadHandler.indexInternal(filter, clazz, context.getHooksDTO(), context.getFromCache(), context.getPersistCopy(), context.getApplyDefaultPolicies(), false),
				(context) -> crudReadHandler.indexInternal(filter, clazz, context.getHooksDTO(), context.getFromCache(), context.getPersistCopy(), context.getApplyDefaultPolicies(), true).getPagingRO().getTotal()
		);
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> ReadCRUDRequestBuilder<CRUDPreIndexHook<ID, Entity>, CRUDOnIndexHook<ID, Entity>, CRUDPostIndexHook<ID, Entity>, PagingDTO<RO>> index(
			DynamicModelFilter filter, Class<Entity> clazz, Class<RO> toClazz) {
		return new ReadCRUDRequestBuilder<>(
				(context) -> {
					PagingDTO<Entity> resultDTO = crudReadHandler.indexInternal(filter, clazz, context.getHooksDTO(), context.getFromCache(), context.getPersistCopy(), context.getApplyDefaultPolicies(), false);
					List<RO> mappedResults = crudHelper.fillMany(resultDTO.getData(), toClazz);
					return new PagingDTO<>(resultDTO.getPagingRO(), mappedResults);
				}, (context) -> crudReadHandler.indexInternal(filter, clazz, context.getHooksDTO(), context.getFromCache(), context.getPersistCopy(), context.getApplyDefaultPolicies(), true).getPagingRO().getTotal());
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> UpdateCRUDRequestBuilder<CRUDPreDeleteHook<ID, Entity>, CRUDOnDeleteHook<ID, Entity>, CRUDPostDeleteHook<ID, Entity>, Void> delete(ID id,
			Class<Entity> clazz) {
		return new UpdateCRUDRequestBuilder<>((context) -> {
			crudDeleteHandler.deleteInternal(id, clazz, context.getHooksDTO(), context.getApplyDefaultPolicies());
			return null;
		});
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> UpdateCRUDRequestBuilder<CRUDPreCreateFromHook<ID, Entity>, CRUDOnCreateFromHook<ID, Entity>, CRUDPostCreateFromHook<ID, Entity>, Entity> createFrom(
			Object object, Class<Entity> clazz) {
		return new UpdateCRUDRequestBuilder<>((context) -> crudCreateHandler.createFromInternal(object, clazz, context.getHooksDTO()));
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> UpdateCRUDRequestBuilder<CRUDPreCreateFromHook<ID, Entity>, CRUDOnCreateFromHook<ID, Entity>, CRUDPostCreateFromHook<ID, Entity>, RO> createFrom(
			Object object, Class<Entity> clazz, Class<RO> toClazz) {
		return new UpdateCRUDRequestBuilder<>((context) -> {
			Entity result = crudCreateHandler.createFromInternal(object, clazz, context.getHooksDTO());
			return crudHelper.fill(result, toClazz);
		});
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> UpdateCRUDRequestBuilder<CRUDPreCreateHook<ID, Entity>, CRUDOnCreateHook<ID, Entity>, CRUDPostCreateHook<ID, Entity>, Entity> create(
			Entity entity) {
		return new UpdateCRUDRequestBuilder<>((context) -> crudCreateHandler.createInternal(entity, context.getHooksDTO()));
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> UpdateCRUDRequestBuilder<CRUDPreCreateHook<ID, Entity>, CRUDOnCreateHook<ID, Entity>, CRUDPostCreateHook<ID, Entity>, RO> create(Entity entity,
			Class<RO> toClazz) {
		return new UpdateCRUDRequestBuilder<>((context) -> {
			Entity result = crudCreateHandler.createInternal(entity, context.getHooksDTO());
			return crudHelper.fill(result, toClazz);
		});
	}


	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> UpdateCRUDRequestBuilder<CRUDPreUpdateFromHook<ID, Entity>, CRUDOnUpdateFromHook<ID, Entity>, CRUDPostUpdateFromHook<ID, Entity>, Entity> updateFrom(
			ID id, Object object, Class<Entity> clazz) {
		return new UpdateCRUDRequestBuilder<>((context) -> crudUpdateHandler.updateFromInternal(id, object, clazz, context.getHooksDTO()));
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> UpdateCRUDRequestBuilder<CRUDPreUpdateFromHook<ID, Entity>, CRUDOnUpdateFromHook<ID, Entity>, CRUDPostUpdateFromHook<ID, Entity>, RO> updateFrom(
			ID id, Object object, Class<Entity> clazz, Class<RO> toClazz) {
		return new UpdateCRUDRequestBuilder<>((context) -> {
			Entity result = crudUpdateHandler.updateFromInternal(id, object, clazz, context.getHooksDTO());
			return crudHelper.fill(result, toClazz);
		});
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> UpdateCRUDRequestBuilder<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>, Entity> update(
			Entity entity) {
		return new UpdateCRUDRequestBuilder<>((context) -> crudUpdateHandler.updateInternal(entity, context.getHooksDTO()));
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> UpdateCRUDRequestBuilder<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>, RO> update(Entity entity,
			Class<RO> toClazz) {
		return new UpdateCRUDRequestBuilder<>((context) -> {
			Entity result = crudUpdateHandler.updateInternal(entity, context.getHooksDTO());
			return crudHelper.fill(result, toClazz);
		});
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> MassUpdateCRUDRequestBuilder<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>, List<Entity>> update(
			List<Entity> entities) {
		return new MassUpdateCRUDRequestBuilder<>((context) -> crudUpdateHandler.updateManyTransactional(entities, context.getHooksDTO(), context.getPersistCopy()));
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> MassUpdateCRUDRequestBuilder<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>, List<RO>> update(
			List<Entity> entities, Class<RO> toClazz) {
		return new MassUpdateCRUDRequestBuilder<>((context) -> {
			List<Entity> result = crudUpdateHandler.updateManyTransactional(entities, context.getHooksDTO(), context.getPersistCopy());
			return crudHelper.fillMany(result, toClazz);
		});
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> MassUpdateCRUDRequestBuilder<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>, List<Entity>> updateByFilter(
			DynamicModelFilter filter, Class<Entity> entityClazz) {
		return new MassUpdateCRUDRequestBuilder<>((context) -> crudUpdateHandler.updateByFilterTransactional(filter, entityClazz, context.getHooksDTO(), context.getPersistCopy()));
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> MassUpdateCRUDRequestBuilder<CRUDPreUpdateHook<ID, Entity>, CRUDOnUpdateHook<ID, Entity>, CRUDPostUpdateHook<ID, Entity>, List<RO>> updateByFilter(
			DynamicModelFilter filter, Class<Entity> entityClazz, Class<RO> toClazz) {
		return new MassUpdateCRUDRequestBuilder<>((context) -> {
			List<Entity> result = crudUpdateHandler.updateByFilterTransactional(filter, entityClazz, context.getHooksDTO(), context.getPersistCopy());
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
				(context) -> crudReadHandler.showByInternal(filter, clazz, context.getHooksDTO(), context.getFromCache(), context.getPersistCopy(), mode, context.getApplyDefaultPolicies()),
				(context) -> crudReadHandler.showByInternal(filter, clazz, context.getHooksDTO(), context.getFromCache(), context.getPersistCopy(), mode, context.getApplyDefaultPolicies()) != null ? 1L : 0L
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
				(context) -> {
					Entity result = crudReadHandler.showByInternal(filter, clazz, context.getHooksDTO(), context.getFromCache(), context.getPersistCopy(), mode, context.getApplyDefaultPolicies());
					if(result == null) {
						return null;
					}

					return crudHelper.fill(result, toClazz);
				}, (context) -> crudReadHandler.showByInternal(filter, clazz, context.getHooksDTO(), context.getFromCache(), context.getPersistCopy(), mode, context.getApplyDefaultPolicies()) != null ? 1L : 0L
		);
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> ReadCRUDRequestBuilder<CRUDPreShowHook<ID, Entity>, CRUDOnShowHook<ID, Entity>, CRUDPostShowHook<ID, Entity>, Entity> show(ID id,
			Class<Entity> clazz) {
		return new ReadCRUDRequestBuilder<>(
				(context) -> crudReadHandler.showInternal(id, clazz, context.getHooksDTO(), context.getFromCache(), context.getPersistCopy(), context.getApplyDefaultPolicies()),
				(context) -> crudReadHandler.showInternal(id, clazz, context.getHooksDTO(), context.getFromCache(), context.getPersistCopy(), context.getApplyDefaultPolicies()) != null ? 1L : 0L
		);
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>, RO> ReadCRUDRequestBuilder<CRUDPreShowHook<ID, Entity>, CRUDOnShowHook<ID, Entity>, CRUDPostShowHook<ID, Entity>, RO> show(ID id,
			Class<Entity> clazz, Class<RO> toClazz) {
		return new ReadCRUDRequestBuilder<>((context) -> {
			Entity result = crudReadHandler.showInternal(id, clazz, context.getHooksDTO(), context.getFromCache(), context.getPersistCopy(), context.getApplyDefaultPolicies());
			if(result == null) {
				return null;
			}

			return crudHelper.fill(result, toClazz);
		}, (context) -> crudReadHandler.showInternal(id, clazz, context.getHooksDTO(), context.getFromCache(), context.getPersistCopy(), context.getApplyDefaultPolicies()) != null ? 1L : 0L
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
