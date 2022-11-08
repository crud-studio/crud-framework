package studio.crud.crudframework.crud.model;

import studio.crud.crudframework.crud.hooks.HooksDTO;

/**
 * {@inheritDoc}
 */
public class UpdateCRUDRequestBuilder<PreHook, OnHook, PostHook, ReturnType> extends CRUDRequestBuilder<PreHook, OnHook, PostHook, ReturnType> {

	private UpdateCRUDExecutor<PreHook, OnHook, PostHook, ReturnType> onExecute;

	public UpdateCRUDRequestBuilder(UpdateCRUDExecutor<PreHook, OnHook, PostHook, ReturnType> onExecute) {
		this.onExecute = onExecute;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReturnType execute() {
		return this.onExecute.execute(new UpdateRequestContext<>(new HooksDTO<>(preHooks, onHooks, postHooks), accessorDTO, applyDefaultPolicies));
	}

	public interface UpdateCRUDExecutor<PreHook, OnHook, PostHook, EntityType> {

		EntityType execute(UpdateRequestContext<PreHook, OnHook, PostHook, EntityType> context);
	}
}
