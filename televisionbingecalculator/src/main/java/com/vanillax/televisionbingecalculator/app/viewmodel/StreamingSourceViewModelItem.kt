package com.vanillax.televisionbingecalculator.app.viewmodel

import androidx.annotation.DrawableRes
import com.vanillax.televisionbingecalculator.app.R
import com.vanillax.televisionbingecalculator.app.kotlin.items.BaseItem

/**
 * Created by mitchross on 3/5/17.
 */

class StreamingSourceViewModelItem(private var videoStream: VideoStream?): BaseItem(R.layout.streaming_source) {


    //		if( streamSource !=null)
    //		{
    //			streamingIconName = streamSource.sourceDisplayName.toLowerCase();
    //		}
    val streamingIcon: Int
        get() = videoStream?.icon ?: 0

    enum class VideoStream(@DrawableRes val icon: Int) {
        NETFLIX(R.drawable.netflix),
        HULU(R.drawable.hulu),
        AMAZON(R.drawable.amazon),
        VUDO(R.drawable.vudu),
        HBO(R.drawable.hbo)
    }
}
