package com.vanillax.televisionbingecalculator.app.TBC.adapters


import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.Util.BindingAdapter.BaseDataBindingAdapter
import com.vanillax.televisionbingecalculator.app.Util.BindingAdapter.DataBoundViewHolder
import com.vanillax.televisionbingecalculator.app.databinding.CastListingBinding
import com.vanillax.televisionbingecalculator.app.viewmodel.CastViewModelItem
import java.util.*

/**
 * Created by mitchross on 5/21/17.
 */

class CastListRecyclerAdapter : BaseDataBindingAdapter<CastListingBinding>() {
    internal var castList: MutableList<CastViewModelItem>

    init {

        castList = ArrayList()
    }

    fun setCastList(castResponse: com.vanillax.televisionbingecalculator.app.kotlin.network.response.CastResponse) {
        castList.clear()
        if (castResponse.castList != null) {
            for (c in castResponse.castList!!) {
                castList.add(CastViewModelItem(c))
            }
        }

        notifyDataSetChanged()
    }

    override fun bindItem(holder: DataBoundViewHolder<CastListingBinding>, position: Int, payloads: List<Any>) {
        holder.binding.viewModel = castList[position]
    }

    override fun getItemCount(): Int {
        return castList.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.cast_listing
    }
}
