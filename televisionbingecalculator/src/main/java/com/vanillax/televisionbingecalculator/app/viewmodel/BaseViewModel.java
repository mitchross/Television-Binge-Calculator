package com.vanillax.televisionbingecalculator.app.viewmodel;

import android.support.annotation.NonNull;

/**
 * Created by mitchross on 2/4/17.
 */

public class BaseViewModel implements LifeCycle.ViewModel
{

	private LifeCycle.View viewCallback;

	@Override
	public void onViewResumed()
	{

	}

	@Override
	public void onViewDetached()
	{
		viewCallback = null;
	}

	@Override
	public void onViewAttached(
			@NonNull LifeCycle.View viewCallback )
	{
		this.viewCallback = viewCallback;
	}

	protected boolean hasViewCallback()
	{
		return viewCallback!=null;
	}

	protected LifeCycle.View getViewCallback() { return viewCallback;}
}
