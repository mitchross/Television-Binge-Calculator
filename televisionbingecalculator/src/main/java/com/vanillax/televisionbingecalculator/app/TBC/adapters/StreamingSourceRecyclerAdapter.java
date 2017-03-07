package com.vanillax.televisionbingecalculator.app.TBC.adapters;

import android.support.annotation.NonNull;

import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.GuideBoxResponse.GuideBoxAvailableContentResponse;
import com.vanillax.televisionbingecalculator.app.Util.BindingAdapter.BaseDataBindingAdapter;
import com.vanillax.televisionbingecalculator.app.Util.BindingAdapter.DataBoundViewHolder;
import com.vanillax.televisionbingecalculator.app.databinding.StreamingSourceBinding;
import com.vanillax.televisionbingecalculator.app.viewmodel.StreamingSourceViewModelItem;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchross on 12/9/14.
 */
public class StreamingSourceRecyclerAdapter extends BaseDataBindingAdapter<StreamingSourceBinding>
{

   private List<StreamingSourceViewModelItem> streamingSourceViewModelItems;


    public StreamingSourceRecyclerAdapter()
    {
        streamingSourceViewModelItems = new ArrayList<>(  );
    }

    public void setStreamingSourceViewModelItems( @NonNull List<GuideBoxAvailableContentResponse.StreamSource> streamSources )
    {
        streamingSourceViewModelItems.clear();
        for ( GuideBoxAvailableContentResponse.StreamSource s : streamSources )
        {
            this.streamingSourceViewModelItems.add( new StreamingSourceViewModelItem( s ) );
        }

        notifyDataSetChanged();
    }

    @Override
    protected void bindItem( DataBoundViewHolder<StreamingSourceBinding> holder, int position, List<Object> payloads )
    {
        holder.binding.setViewModel( streamingSourceViewModelItems.get( position ) );
    }

    @Override
    public int getItemCount()
    {
        return streamingSourceViewModelItems.size();
    }

    @Override
    public int getItemViewType( int position )
    {
        return R.layout.streaming_source;
    }




}
