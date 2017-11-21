package com.vanillax.televisionbingecalculator.app.TBC.adapters;

import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.movie.Offer;
import com.vanillax.televisionbingecalculator.app.Util.BindingAdapter.BaseDataBindingAdapter;
import com.vanillax.televisionbingecalculator.app.Util.BindingAdapter.DataBoundViewHolder;
import com.vanillax.televisionbingecalculator.app.databinding.StreamingSourceBinding;
import com.vanillax.televisionbingecalculator.app.viewmodel.StreamingSourceViewModelItem;

import java.util.ArrayList;
import java.util.List;

import static com.vanillax.televisionbingecalculator.app.ServerAPI.movie.Offer.AMAZON;
import static com.vanillax.televisionbingecalculator.app.ServerAPI.movie.Offer.HBO;
import static com.vanillax.televisionbingecalculator.app.ServerAPI.movie.Offer.HULU;
import static com.vanillax.televisionbingecalculator.app.ServerAPI.movie.Offer.NETFLIX;
import static com.vanillax.televisionbingecalculator.app.ServerAPI.movie.Offer.VUDU;

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

    public void setStreamingSourceViewModelItems( List<Offer> movieOffers )
    {
        boolean netflix = false;
        boolean hulu = false;
        boolean vudu = false;
        boolean hbo = false;
        boolean amazon = false;


        streamingSourceViewModelItems.clear();
        for ( Offer offer : movieOffers)
        {

            if( offer.providerId != 0 )
            {

                switch ( offer.providerId )
                {
                    case NETFLIX:
                        if ( !netflix )
                        {
                            this.streamingSourceViewModelItems.add( new StreamingSourceViewModelItem( StreamingSourceViewModelItem.NETFLIX ) );
                            netflix = true;
                        }
                        break;
                    case HULU:
                        if (!hulu)
                        {
                            this.streamingSourceViewModelItems.add( new StreamingSourceViewModelItem( StreamingSourceViewModelItem.HULU ) );
                            hulu = true;
                        }
                        break;
                    case VUDU:
                        if(!vudu)
                        {
                            this.streamingSourceViewModelItems.add( new StreamingSourceViewModelItem( StreamingSourceViewModelItem.VUDU ) );
                            vudu = true;
                        }
                        break;
                    case HBO:
                        if (!hbo)
                        {
                            this.streamingSourceViewModelItems.add( new StreamingSourceViewModelItem( StreamingSourceViewModelItem.HBO ) );
                            hbo = true;
                        }
                        break;
                    case AMAZON:
                        if(!amazon)
                        {
                            this.streamingSourceViewModelItems.add( new StreamingSourceViewModelItem( StreamingSourceViewModelItem.AMAZON ) );
                            amazon = true;
                        }
                        break;


                }
            }
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
