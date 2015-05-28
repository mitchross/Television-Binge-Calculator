package com.vanillax.televisionbingecalculator.app.TBC.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vanillax.televisionbingecalculator.app.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by mitch on 6/15/14.
 */
public class ListViewAdapter extends ArrayAdapter<String>
{




	Context context;
	private List<String> spinnerImageList;
	private List<String> myRowTextList;


	public ListViewAdapter( Context context, int textViewResourceId,
							List<String> myRowTextList, List<String> iconUrlList ) {
		super(context, textViewResourceId, myRowTextList);
		this.context = context;
		this.spinnerImageList = iconUrlList;
		this.myRowTextList = myRowTextList;

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View view, ViewGroup parent) {

		ViewHolder myView;

		if ( view == null )
		{
			view = LayoutInflater.from ( getContext() ).inflate( R.layout.spinnerrow , parent , false );
			myView = new ViewHolder( view );
			view.setTag( myView );
		}
		else
		{
			myView = (ViewHolder)view.getTag();
		}


		myView.spinnerRowText.setText( myRowTextList.get( position ).toString() );
		Glide.with( getContext() ).load( spinnerImageList.get( position ) ).into( myView.spinnerRowImage );

		return view;
	}

	protected static class ViewHolder
	{
		@InjectView( R.id.spinner_row_image )
		ImageView spinnerRowImage;

		@InjectView( R.id.spinner_row_text )
		TextView spinnerRowText;

		public ViewHolder ( View view )
		{
			ButterKnife.inject( this, view );
		}

	}
}
