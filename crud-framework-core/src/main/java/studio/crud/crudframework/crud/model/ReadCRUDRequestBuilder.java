package studio.crud.crudframework.crud.model;

import studio.crud.crudframework.crud.hooks.HooksDTO;

/**
 * {@inheritDoc}
 */
public class ReadCRUDRequestBuilder<PreHook, OnHook, PostHook, ReturnType> extends CRUDRequestBuilder<PreHook, OnHook, PostHook, ReturnType> {

	private boolean fromCache = false;

	private ReadCRUDExecutor<PreHook, OnHook, PostHook, ReturnType> onExecute;

	private ReadCRUDExecutor<PreHook, OnHook, PostHook, Long> onCount;

	private Boolean persistCopy = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReturnType execute() {
		return this.onExecute.execute(new ReadRequestContext<>(new HooksDTO<>(preHooks, onHooks, postHooks), fromCache, persistCopy, accessorDTO, applyDefaultPolicies));
	}

	public long count() {
		return this.onCount.execute(new ReadRequestContext<>(new HooksDTO<>(preHooks, onHooks, postHooks), fromCache, persistCopy, accessorDTO, applyDefaultPolicies));
	}

	public ReadCRUDRequestBuilder(ReadCRUDExecutor<PreHook, OnHook, PostHook, ReturnType> onExecute,
			ReadCRUDExecutor<PreHook, OnHook, PostHook, Long> onCount) {
		this.onExecute = onExecute;
		this.onCount = onCount;
	}

	/**
	 * Denotes the request should be fetched from cache
	 */

	public ReadCRUDRequestBuilder<PreHook, OnHook, PostHook, ReturnType> fromCache() {
		fromCache = true;
		return this;
	}

	public ReadCRUDRequestBuilder<PreHook, OnHook, PostHook, ReturnType> persistCopy() {
		persistCopy = true;
		return this;
	}

	public ReadCRUDRequestBuilder<PreHook, OnHook, PostHook, ReturnType> dontPersistCopy() {
		persistCopy = false;
		return this;
	}

	public interface ReadCRUDExecutor<PreHook, OnHook, PostHook, EntityType> {

		EntityType execute(ReadRequestContext<PreHook, OnHook, PostHook, EntityType> context);
	}
}