package com.vanillax.televisionbingecalculator.app.viewmodel;

/**
 * Created by mitchross on 2/4/17.
 */

public class SplashScreenViewModel extends BaseViewModel
{

	public interface SplashScreenViewModelCallback extends LifeCycle.View{
		void onEditTextTapped();
	}

	protected SplashScreenViewModelCallback getViewCallback()
	{
		return (SplashScreenViewModelCallback) super.getViewCallback();
	}

	public void onEditTextTapped()
	{
		if ( hasViewCallback() )
		{
			getViewCallback().onEditTextTapped();
		}
	}

}
