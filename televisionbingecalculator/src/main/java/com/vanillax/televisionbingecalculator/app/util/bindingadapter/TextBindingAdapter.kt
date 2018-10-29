package com.vanillax.televisionbingecalculator.app.util.bindingadapter

import android.databinding.BindingAdapter
import android.support.annotation.StringRes
import android.widget.TextView

@BindingAdapter("android:text_helper")
fun setText(textView: TextView, bindingTextHelper: BindingTextHelper?) {
    if (bindingTextHelper == null || bindingTextHelper.stringResId == 0) {
        textView.text = ""
    } else {
        textView.text = textView.context.getString(bindingTextHelper.stringResId, *bindingTextHelper.stringArgs)
    }
}


class BindingTextHelper(@param:StringRes
                        val stringResId: Int, vararg args: Any) {

    val stringArgs: Array<out Any> = args


}

