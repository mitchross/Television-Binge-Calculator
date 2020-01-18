package com.vanillax.televisionbingecalculator.app.tbc.adapters

import androidx.recyclerview.widget.DiffUtil
import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.util.bindingadapter.BaseDataBindingAdapter
import com.vanillax.televisionbingecalculator.app.util.bindingadapter.DataBoundViewHolder
import com.vanillax.televisionbingecalculator.app.databinding.CastListingBinding
import com.vanillax.televisionbingecalculator.app.util.bindingadapter.BaseDataBindingListAdapter
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

class CastListRecyclerAdapter2 : BaseDataBindingListAdapter<CastViewModelItem>(DiffUtilItemCallback()) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    class DiffUtilItemCallback: DiffUtil.ItemCallback<CastViewModelItem>() {

        override fun areItemsTheSame(oldItem: CastViewModelItem,
                                     newItem: CastViewModelItem): Boolean {
            return oldItem.castName == newItem.castName
        }

        override fun areContentsTheSame(oldItem: CastViewModelItem,
                                        newItem: CastViewModelItem): Boolean {
            return true
        }
    }
}
