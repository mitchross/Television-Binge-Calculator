package com.vanillax.televisionbingecalculator.app.tbc.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vanillax.televisionbingecalculator.app.R;

/**
 * Created by mitchross on 2/27/17.
 */

public class GlideBindingAdapter
{
	@BindingAdapter( {"imageUrl"} )
	public static void loadImage( ImageView view, String url)
	{
		Glide.with( view.getContext() )
				.load( url )
				.apply( new RequestOptions()
				.placeholder(  R.drawable.tv_icon  )
				.centerCrop()
				.dontAnimate()
				.error( view.getContext().getResources().getDrawable( R.drawable.tv_icon ) ) )
				.into(view);
	}



	@BindingAdapter({"localImageUrl"})
	public static void setImageViewResource(ImageView imageView, int resource) {
		imageView.setImageResource(resource);
	}



}
