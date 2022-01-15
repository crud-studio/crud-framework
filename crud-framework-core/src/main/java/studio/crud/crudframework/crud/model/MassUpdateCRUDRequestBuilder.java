package studio.crud.crudframework.crud.model;

import studio.crud.crudframework.crud.dataaccess.model.DataAccessorDTO;
import studio.crud.crudframework.crud.hooks.HooksDTO;

/**
 * {@inheritDoc}
 */
public class MassUpdateCRUDRequestBuilder<PreHook, OnHook, PostHook, ReturnType> extends CRUDRequestBuilder<PreHook, OnHook, PostHook, ReturnType> {

	private MassUpdateCRUDExecutor<PreHook, OnHook, PostHook, ReturnType> onExecute;

	private Boolean persistCopy = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReturnType execute() {
		return this.onExecute.execute(new HooksDTO<>(preHooks, onHooks, postHooks), persistCopy, accessorDTO);
	}

	public MassUpdateCRUDRequestBuilder(MassUpdateCRUDExecutor<PreHook, OnHook, PostHook, ReturnType> onExecute) {
		this.onExecute = onExecute;
	}

	public MassUpdateCRUDRequestBuilder<PreHook, OnHook, PostHook, ReturnType> persistCopy() {
		persistCopy = true;
		return this;
	}

	public interface MassUpdateCRUDExecutor<PreHook, OnHook, PostHook, EntityType> {

		EntityType execute(HooksDTO<PreHook, OnHook, PostHook> hooksDTO, Boolean persistCopy, DataAccessorDTO accessorDTO);
	}
}