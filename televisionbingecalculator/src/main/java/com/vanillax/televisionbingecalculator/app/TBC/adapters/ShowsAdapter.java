package com.vanillax.televisionbingecalculator.app.TBC.adapters;

import android.support.annotation.NonNull;

import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.ShowPosterListing;
import com.vanillax.televisionbingecalculator.app.Util.BindingAdapter.BaseDataBindingAdapter;
import com.vanillax.televisionbingecalculator.app.Util.BindingAdapter.DataBoundViewHolder;
import com.vanillax.televisionbingecalculator.app.databinding.ShowCardBinding;
import com.vanillax.televisionbingecalculator.app.view.LandingActivityMain;
import com.vanillax.televisionbingecalculator.app.viewmodel.LandingActivityViewModel;
import com.vanillax.televisionbingecalculator.app.viewmodel.ShowPosterViewModelItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchross on 2/13/17.
 */

public class ShowsAdapter extends BaseDataBindingAdapter<ShowCardBinding>
{
	private List<ShowPosterViewModelItem> showsViewModelItems;
	private LandingActivityViewModel.LandingActivityViewCallback listener;



	public ShowsAdapter(  )
	{
		showsViewModelItems = new ArrayList<>(  );
	}

	public void setListener( LandingActivityViewModel.LandingActivityViewCallback listener)
	{
		this.listener = listener;
	}


	public void setShowsViewModelItems( @NonNull List<ShowPosterListing> showPosterListings, LandingActivityMain.SearchType searchType)
	{
		showsViewModelItems.clear();
		for(ShowPosterListing listing : showPosterListings)
		{
			this.showsViewModelItems.add( new ShowPosterViewModelItem( listing, searchType ) );
		}

		notifyDataSetChanged();
	}



	@Override
	protected void bindItem( DataBoundViewHolder<ShowCardBinding> holder, int position, List<Object> payloads )
	{
		holder.binding.setViewModel( showsViewModelItems.get( position ));
		holder.binding.setListener( listener );
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
