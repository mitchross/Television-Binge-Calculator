package com.vanillax.televisionbingecalculator.app.tbc.adapters

import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.kotlin.network.response.Offer
import com.vanillax.televisionbingecalculator.app.util.bindingadapter.BaseDataBindingAdapter
import com.vanillax.televisionbingecalculator.app.util.bindingadapter.DataBoundViewHolder
import com.vanillax.televisionbingecalculator.app.viewmodel.StreamingSourceViewModelItem
import java.util.*

/**
 * Created by mitchross on 12/9/14.
 */
class StreamingSourceRecyclerAdapter : BaseDataBindingAdapter<com.vanillax.televisionbingecalculator.app.databinding.StreamingSourceBinding>() {

    private val streamingSourceViewModelItems: MutableList<StreamingSourceViewModelItem>

    init {
        streamingSourceViewModelItems = ArrayList()
    }

    fun setStreamingSourceViewModelItems(movieOffers: List<Offer>) {
        var netflix = false
        var hulu = false
        var vudu = false
        var hbo = false
        var amazon = false


        streamingSourceViewModelItems.clear()
        for (offer in movieOffers) {

            if (offer.providerId != 0) {

                when (offer.providerId) {
                    Offer.Streamtype.NETFLIX.value -> if (!netflix) {
                        this.streamingSourceViewModelItems.add(StreamingSourceViewModelItem(StreamingSourceViewModelItem.NETFLIX))
                        netflix = true
                    }
                    Offer.Streamtype.HULU.value -> if (!hulu) {
                        this.streamingSourceViewModelItems.add(StreamingSourceViewModelItem(StreamingSourceViewModelItem.HULU))
                        hulu = true
                    }
                    Offer.Streamtype.VUDU.value -> if (!vudu) {
                        this.streamingSourceViewModelItems.add(StreamingSourceViewModelItem(StreamingSourceViewModelItem.VUDU))
                        vudu = true
                    }
                    Offer.Streamtype.HBO.value -> if (!hbo) {
                        this.streamingSourceViewModelItems.add(StreamingSourceViewModelItem(StreamingSourceViewModelItem.HBO))
                        hbo = true
                    }
                    Offer.Streamtype.AMAZON.value -> if (!amazon) {
                        this.streamingSourceViewModelItems.add(StreamingSourceViewModelItem(StreamingSourceViewModelItem.AMAZON))
                        amazon = true
                    }
                }
            }
        }
        notifyDataSetChanged()

    }



    override fun bindItem(holder: DataBoundViewHolder<com.vanillax.televisionbingecalculator.app.databinding.StreamingSourceBinding>, position: Int, payloads: List<Any>) {
        holder.binding.viewModel = streamingSourceViewModelItems[position]
    }

    override fun getItemCount(): Int {
        return streamingSourceViewModelItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.streaming_source

    }


}
