package com.vanillax.televisionbingecalculator.app.viewmodel

import com.vanillax.televisionbingecalculator.app.R

/**
 * Created by mitchross on 3/5/17.
 */

class StreamingSourceViewModelItem(internal var streamSourceMovie: String?) {


    //		if( streamSource !=null)
    //		{
    //			streamingIconName = streamSource.sourceDisplayName.toLowerCase();
    //		}
    val streamingIcon: Int
        get() {
            var streamingIconName = ""
            if (streamSourceMovie != null || !streamSourceMovie!!.isEmpty()) {
                streamingIconName = streamSourceMovie!!.toLowerCase()
            }


            if (streamingIconName.contains(NETFLIX)) {

                return R.drawable.netflix
            }
            if (streamingIconName.contains(HULU)) {
                return R.drawable.hulu
            }
            if (streamingIconName.contains(AMAZON)) {
                return R.drawable.amazon
            }
            if (streamingIconName.contains(VUDU)) {
                return R.drawable.vudu
            }
            return if (streamingIconName.contains(HBO)) {
                R.drawable.hbo
            } else {
                0
            }

        }

    companion object {
        val NETFLIX = "netflix"
        val HULU = "hulu"
        val AMAZON = "amazon"
        val VUDU = "vudu"
        val HBO = "hbo"
    }
}
