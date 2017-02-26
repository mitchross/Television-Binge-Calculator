package com.vanillax.televisionbingecalculator.app.model;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by mitchross on 2/14/17.
 */

public class ShowViewHolder extends RecyclerView.ViewHolder
{
	private final ViewDataBinding binding;

	public ShowViewHolder( ViewDataBinding binding )
	{
			super(binding.getRoot());
			this.binding = binding;
	}

	public void bind(Object obj)
	{
		binding.setVariable( com.vanillax.televisionbingecalculator.app.BR.landingViewModel, obj);
		binding.executePendingBindings();
	}

}
