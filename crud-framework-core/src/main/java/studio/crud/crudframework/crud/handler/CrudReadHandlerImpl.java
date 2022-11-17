package studio.crud.crudframework.crud.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import studio.crud.crudframework.crud.cache.CacheUtils;
import studio.crud.crudframework.crud.cache.CrudCache;
import studio.crud.crudframework.crud.enums.ShowByMode;
import studio.crud.crudframework.crud.exception.CrudReadException;
import studio.crud.crudframework.crud.hooks.HooksDTO;
import studio.crud.crudframework.crud.hooks.index.CRUDOnIndexHook;
import studio.crud.crudframework.crud.hooks.index.CRUDPostIndexHook;
import studio.crud.crudframework.crud.hooks.index.CRUDPreIndexHook;
import studio.crud.crudframework.crud.hooks.interfaces.IndexHooks;
import studio.crud.crudframework.crud.hooks.interfaces.ShowByHooks;
import studio.crud.crudframework.crud.hooks.interfaces.ShowHooks;
import studio.crud.crudframework.crud.hooks.show.CRUDOnShowHook;
import studio.crud.crudframework.crud.hooks.show.CRUDPostShowHook;
import studio.crud.crudframework.crud.hooks.show.CRUDPreShowHook;
import studio.crud.crudframework.crud.hooks.show.by.CRUDOnShowByHook;
import studio.crud.crudframework.crud.hooks.show.by.CRUDPostShowByHook;
import studio.crud.crudframework.crud.hooks.show.by.CRUDPreShowByHook;
import studio.crud.crudframework.crud.policy.PolicyRuleType;
import studio.crud.crudframework.exception.WrapException;
import studio.crud.crudframework.model.BaseCrudEntity;
import studio.crud.crudframework.modelfilter.DynamicModelFilter;
import studio.crud.crudframework.ro.PagingDTO;
import studio.crud.crudframework.ro.PagingRO;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

@WrapException(CrudReadException.class)
public class CrudReadHandlerImpl implements CrudReadHandler {

	@Autowired
	private CrudHelper crudHelper;

	@Resource(name = "crudReadHandler")
	private CrudReadHandler crudReadHandlerProxy;

	@Autowired
	private CrudSecurityHandler crudSecurityHandler;

	private static Random random = new Random();

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> PagingDTO<Entity> indexInternal(DynamicModelFilter filter, Class<Entity> clazz,
																										HooksDTO<CRUDPreIndexHook<ID, Entity>, CRUDOnIndexHook<ID, Entity>, CRUDPostIndexHook<ID, Entity>> hooks,
																										boolean fromCache, Boolean persistCopy, boolean applyDefaultPolicies, boolean count) {
		if(filter == null) {
			filter = new DynamicModelFilter();
		}

		if (applyDefaultPolicies) {
			MultiPolicyResult policyResult = crudSecurityHandler.evaluatePreRules(PolicyRuleType.CAN_ACCESS, clazz);
			policyResult.throwIfFailed();
			filter.getFilterFields().addAll(crudSecurityHandler.getFilterFields(clazz));
		}

		crudHelper.validateAndFillFilterFieldMetadata(filter.getFilterFields(), clazz);

		List<IndexHooks> indexHooksList = crudHelper.getHooks(IndexHooks.class, clazz);

		if(indexHooksList != null && !indexHooksList.isEmpty()) {
			for(IndexHooks<ID, Entity> indexHooks : indexHooksList) {
				hooks.getPreHooks().add(0, indexHooks::preIndex);
				hooks.getOnHooks().add(0, indexHooks::onIndex);
				hooks.getPostHooks().add(0, indexHooks::postIndex);
			}
		}

		CrudCache cache = null;

		if(fromCache) {
			cache = crudHelper.getEntityCache(clazz);
		}

		for(CRUDPreIndexHook<ID, Entity> preHook : hooks.getPreHooks()) {
			preHook.run(filter);
		}

		String cacheKey = filter.getCacheKey();
		if(count) {
			cacheKey = "count_" + cacheKey;
		}


		DynamicModelFilter finalFilter = filter;
		PagingDTO<Entity> result = (PagingDTO<Entity>) CacheUtils.getObjectAndCache(() -> crudReadHandlerProxy.indexTransactional(finalFilter, clazz, hooks.getOnHooks(), persistCopy, applyDefaultPolicies, count), cacheKey, cache);

		for(CRUDPostIndexHook<ID, Entity> postHook : hooks.getPostHooks()) {
			postHook.run(filter, result);
		}

		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> PagingDTO<Entity> indexTransactional(DynamicModelFilter filter, Class<Entity> clazz,
																											 List<CRUDOnIndexHook<ID, Entity>> onHooks,
																											 Boolean persistCopy, boolean applyDefaultPolicies, boolean count) {
		PagingDTO<Entity> result;
		if(!count) {
			long total;
			List<Entity> data;
			boolean hasMore;

			if(filter.getLimit() != null) {
				filter.setLimit(filter.getLimit() + 1);
				data = crudHelper.getEntities(filter, clazz, persistCopy);
				if (applyDefaultPolicies) {
					for (Entity entity : data) {
						MultiPolicyResult policyResult = crudSecurityHandler.evaluatePostRules(entity, PolicyRuleType.CAN_ACCESS , clazz);
						policyResult.throwIfFailed();
					}
				}
				hasMore = data.size() == filter.getLimit();
				filter.setLimit(filter.getLimit() - 1);
				int start = filter.getStart() == null ? 0 : filter.getStart();
				if(hasMore) {
					data.remove(data.size() - 1);
				} else {
					crudHelper.setTotalToPagingCache(clazz, filter, data.size() + start);
				}

				Long cachedTotal = crudHelper.getTotalFromPagingCache(clazz, filter);
				if(cachedTotal != null) {
					hasMore = false;
					total = cachedTotal;
				} else {
					total = data.size() + start;
				}
			} else {
				data = crudHelper.getEntities(filter, clazz, persistCopy);
				hasMore = false;
				total = data.size();
				crudHelper.setTotalToPagingCache(clazz, filter, total);

			}

			result = new PagingDTO<>(new PagingRO(filter.getStart(), filter.getLimit(), total, hasMore), data);
		} else {
			long total = crudHelper.getEntitiesCount(filter, clazz);
			result = new PagingDTO<>(new PagingRO(0, 0, total), null);
			crudHelper.setTotalToPagingCache(clazz, filter, total);
		}

		for(CRUDOnIndexHook<ID, Entity> onHook : onHooks) {
			onHook.run(filter, result);
		}

		return result;
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity showByInternal(DynamicModelFilter filter, Class<Entity> clazz,
			HooksDTO<CRUDPreShowByHook<ID, Entity>, CRUDOnShowByHook<ID, Entity>, CRUDPostShowByHook<ID, Entity>> hooks, boolean fromCache, Boolean persistCopy, ShowByMode mode) {

		if(filter == null) {
			filter = new DynamicModelFilter();
		}

		crudHelper.validateAndFillFilterFieldMetadata(filter.getFilterFields(), clazz);
		List<ShowByHooks> showByHooksList = crudHelper.getHooks(ShowByHooks.class, clazz);

		if(showByHooksList != null && !showByHooksList.isEmpty()) {
			for(ShowByHooks<ID, Entity> showByHooks : showByHooksList) {
				hooks.getPreHooks().add(0, showByHooks::preShowBy);
				hooks.getOnHooks().add(0, showByHooks::onShowBy);
				hooks.getPostHooks().add(0, showByHooks::postShowBy);
			}
		}

		for(CRUDPreShowByHook<ID, Entity> preHook : hooks.getPreHooks()) {
			preHook.run(filter);
		}

		CrudCache cache = null;
		if(fromCache) {
			cache = crudHelper.getEntityCache(clazz);
		}

		DynamicModelFilter finalFilter = filter;
		Entity entity = (Entity) CacheUtils.getObjectAndCache(() -> crudReadHandlerProxy.showByTransactional(finalFilter, clazz, hooks.getOnHooks(), persistCopy, mode), "showBy_" + filter.hashCode(), cache);

		for(CRUDPostShowByHook<ID, Entity> postHook : hooks.getPostHooks()) {
			postHook.run(entity);
		}

		return entity;
	}

	@Override
	@Transactional(readOnly = true)
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity showByTransactional(DynamicModelFilter filter, Class<Entity> clazz, List<CRUDOnShowByHook<ID, Entity>> onHooks,
			Boolean persistCopy,
			ShowByMode mode) {
		List<Entity> entities = crudHelper.getEntities(filter, clazz, persistCopy);
		Entity entity = null;
		switch(mode) {
			case THROW_EXCEPTION:
				if(entities.size() > 1) {
					throw new CrudReadException("Received a non unique result");
				}
				entity = entities.size() > 0 ? entities.get(0) : null;

				break;
			case RETURN_FIRST:
				if(entities.size() > 0) {
					entity = entities.get(0);
				}
				break;
			case RETURN_RANDOM:
				if(entities.size() > 0) {
					entity = entities.get(random.nextInt(entities.size()));
				}

				break;
		}

		for(CRUDOnShowByHook<ID, Entity> onHook : onHooks) {
			onHook.run(entity);
		}

		return entity;
	}

	@Override
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity showInternal(ID id, Class<Entity> clazz,
			HooksDTO<CRUDPreShowHook<ID, Entity>, CRUDOnShowHook<ID, Entity>, CRUDPostShowHook<ID, Entity>> hooks, boolean fromCache, Boolean persistCopy) {

		List<ShowHooks> showHooksList = crudHelper.getHooks(ShowHooks.class, clazz);

		if(showHooksList != null && !showHooksList.isEmpty()) {
			for(ShowHooks<ID, Entity> showHooks : showHooksList) {
				hooks.getPreHooks().add(0, showHooks::preShow);
				hooks.getOnHooks().add(0, showHooks::onShow);
				hooks.getPostHooks().add(0, showHooks::postShow);
			}
		}

		for(CRUDPreShowHook<ID, Entity> preHook : hooks.getPreHooks()) {
			preHook.run(id);
		}

		CrudCache cache = null;
		if(fromCache) {
			cache = crudHelper.getEntityCache(clazz);
		}

		Entity entity = (Entity) CacheUtils.getObjectAndCache(() -> crudReadHandlerProxy.showTransactional(id, clazz, hooks.getOnHooks(), persistCopy), BaseCrudEntity.Companion.getCacheKey(clazz, id), cache);

		for(CRUDPostShowHook<ID, Entity> postHook : hooks.getPostHooks()) {
			postHook.run(entity);
		}

		return entity;
	}

	@Override
	@Transactional(readOnly = true)
	public <ID extends Serializable, Entity extends BaseCrudEntity<ID>> Entity showTransactional(ID id, Class<Entity> clazz, List<CRUDOnShowHook<ID, Entity>> onHooks, Boolean persistCopy) {
		Entity entity = crudHelper.getEntityById(id, clazz, persistCopy);

		for(CRUDOnShowHook<ID, Entity> onHook : onHooks) {
			onHook.run(entity);
		}

		return entity;
	}
}
