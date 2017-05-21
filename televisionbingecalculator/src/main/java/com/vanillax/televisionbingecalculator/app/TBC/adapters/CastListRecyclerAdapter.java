package com.vanillax.televisionbingecalculator.app.TBC.adapters;


import android.support.annotation.NonNull;

import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.Cast;
import com.vanillax.televisionbingecalculator.app.ServerAPI.TV.CastResponse;
import com.vanillax.televisionbingecalculator.app.Util.BindingAdapter.BaseDataBindingAdapter;
import com.vanillax.televisionbingecalculator.app.Util.BindingAdapter.DataBoundViewHolder;
import com.vanillax.televisionbingecalculator.app.databinding.CastListingBinding;
import com.vanillax.televisionbingecalculator.app.viewmodel.CastViewModelItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchross on 5/21/17.
 */

public class CastListRecyclerAdapter extends BaseDataBindingAdapter<CastListingBinding>
{
	List<CastViewModelItem> castList;

	public CastListRecyclerAdapter()
	{
		castList = new ArrayList<>(  );
	}

	public void setCastList( @NonNull
									 CastResponse castResponse )
	{
		castList.clear();
		if ( castResponse.castList !=null )
		{
			for ( Cast c : castResponse.castList)
			{
				castList.add(  new CastViewModelItem( c ) ) ;
			}
		}

		notifyDataSetChanged();
	}

	@Override
	protected void bindItem( DataBoundViewHolder<CastListingBinding> holder, int position, List<Object> payloads )
	{
		holder.binding.setViewModel( castList.get( position ) );
	}

	@Override
	public int getItemCount()
	{
		return castList.size();
	}

	@Override
	public int getItemViewType( int position )
	{
		return R.layout.cast_listing;
	}
}
