package com.vanillax.televisionbingecalculator.app.viewmodel;

import androidx.annotation.NonNull;

/**
 * Created by mitchross on 2/4/17.
 */

public interface LifeCycle
{
	interface View {}

	interface  ViewModel {
		void onViewResumed();
		void onViewDetached();
		void onViewAttached( @NonNull LifeCycle.View viewCallback );
	}

}
