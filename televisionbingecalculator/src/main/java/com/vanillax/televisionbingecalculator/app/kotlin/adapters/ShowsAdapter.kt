package com.vanillax.televisionbingecalculator.app.kotlin.adapters

import androidx.databinding.ViewDataBinding
import com.vanillax.televisionbingecalculator.app.R

import com.vanillax.televisionbingecalculator.app.kotlin.network.response.ShowPosterListing

import com.vanillax.televisionbingecalculator.app.kotlin.viewmodels.PosterThumbnailViewModel
import com.vanillax.televisionbingecalculator.app.util.bindingadapter.BaseDataBindingAdapter
import com.vanillax.televisionbingecalculator.app.util.bindingadapter.DataBoundViewHolder
import java.util.*

/**
 * Created by mitchross on 2/13/17.
 */

class ShowsAdapter(private val callback: (id: Int, url: String, title: String) -> Unit) : BaseDataBindingAdapter<ViewDataBinding>() {

    private val posterThumbnailViewModels: MutableList<PosterThumbnailViewModel> = arrayListOf()
    private var showPosterListings: List<ShowPosterListing> = emptyList()

    fun setShowsViewModelItems(showPosterListings: List<ShowPosterListing>) {

        if (this.showPosterListings == showPosterListings) {
            return
        }

        posterThumbnailViewModels.clear()
        for (listing in showPosterListings) {
            this.posterThumbnailViewModels.add(PosterThumbnailViewModel(listing, callback))
        }

        notifyDataSetChanged()

        this.showPosterListings = showPosterListings
    }

    override fun bindItem(holder: DataBoundViewHolder<ViewDataBinding>?, position: Int, payloads: MutableList<Any>?) {

        if (holder != null) {
            holder.binding.setVariable(BR.viewModel, posterThumbnailViewModels[position])
        }
    }

    override fun getItemCount(): Int {
        return posterThumbnailViewModels.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.poster_thumbnail_item
    }
}
