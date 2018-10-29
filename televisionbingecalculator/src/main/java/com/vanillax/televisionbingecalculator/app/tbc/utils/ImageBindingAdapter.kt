package com.vanillax.televisionbingecalculator.app.tbc.utils

import android.databinding.BindingAdapter
import android.view.View

/**
 * Created by mitchross on 3/5/17.
 */


@BindingAdapter("android:visibility")
fun setVisibility(view: View, value: Boolean?) {
    view.visibility = if (value!!) View.VISIBLE else View.GONE
}
