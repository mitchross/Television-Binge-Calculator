package com.vanillax.televisionbingecalculator.app.TBC.adapters;

import android.support.annotation.NonNull;

import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.ShowPosterListing;
import com.vanillax.televisionbingecalculator.app.Util.BindingAdapter.BaseDataBindingAdapter;
import com.vanillax.televisionbingecalculator.app.Util.BindingAdapter.DataBoundViewHolder;
import com.vanillax.televisionbingecalculator.app.databinding.ShowCardBinding;
import com.vanillax.televisionbingecalculator.app.viewmodel.ShowPosterViewModelItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchross on 2/13/17.
 */

public class ShowsAdapter extends BaseDataBindingAdapter<ShowCardBinding>
{
	private List<ShowPosterViewModelItem> showsViewModelItems;





	public ShowsAdapter(  )
	{
		showsViewModelItems = new ArrayList<>(  );
	}


	public void setShowsViewModelItems( @NonNull List<ShowPosterListing> showPosterListings)
	{
		showsViewModelItems.clear();
		for(ShowPosterListing listing : showPosterListings)
		{
			this.showsViewModelItems.add( new ShowPosterViewModelItem( listing ) );
		}

		notifyDataSetChanged();
	}


	@Override
	protected void bindItem( DataBoundViewHolder<ShowCardBinding> holder, int position, List<Object> payloads )
	{
		holder.binding.setViewModel( showsViewModelItems.get( position ));
	}

	@Override
	public int getItemCount()
	{
		return showsViewModelItems.size();
	}

	@Override
	public int getItemViewType( int position )
	{
		return R.layout.show_card;
	}
}
