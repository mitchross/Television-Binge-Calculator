package com.vanillax.televisionbingecalculator.app.Kotlin.adapters

import com.vanillax.televisionbingecalculator.app.Kotlin.network.response.ShowPosterListing
import com.vanillax.televisionbingecalculator.app.Kotlin.viewmodels.LandingActivityViewModel
import com.vanillax.televisionbingecalculator.app.Kotlin.viewmodels.ShowPosterViewModelItem
import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.Util.BindingAdapter.BaseDataBindingAdapter
import com.vanillax.televisionbingecalculator.app.Util.BindingAdapter.DataBoundViewHolder
import com.vanillax.televisionbingecalculator.app.databinding.ShowCardItemBinding
import java.util.*

/**
 * Created by mitchross on 2/13/17.
 */

class ShowsAdapter( private var listener: LandingActivityViewModel.LandingActivityViewModelInterface) : BaseDataBindingAdapter<ShowCardItemBinding>() {


    private val showsViewModelItems: MutableList<ShowPosterViewModelItem>

    init {
        showsViewModelItems = ArrayList()
    }

    fun setListener(listener: com.vanillax.televisionbingecalculator.app.Kotlin.viewmodels.LandingActivityViewModel.LandingActivityViewModelInterface) {
        this.listener = listener
    }


    fun setShowsViewModelItems(showPosterListings: List<ShowPosterListing>) {
        showsViewModelItems.clear()
        for (listing in showPosterListings) {
            this.showsViewModelItems.add(ShowPosterViewModelItem(listing, listener))
        }

        notifyDataSetChanged()
    }



    override fun bindItem(holder: DataBoundViewHolder<ShowCardItemBinding>?, position: Int, payloads: MutableList<Any>?) {

        if (holder != null) {
            holder.binding.viewModel = showsViewModelItems[position]
            holder.binding.listener = listener
        }
    }

    override fun getItemCount(): Int {
        return showsViewModelItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.show_card_item
    }
}
