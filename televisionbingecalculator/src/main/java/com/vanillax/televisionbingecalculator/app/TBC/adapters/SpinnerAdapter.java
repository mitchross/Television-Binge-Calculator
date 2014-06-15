package com.vanillax.televisionbingecalculator.app.TBC.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vanillax.televisionbingecalculator.app.R;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by mitch on 6/15/14.
 */
public class SpinnerAdapter extends ArrayAdapter<String>
{

	@InjectView( R.id.spinner_row_image )
	ImageView spinnerRowImage;

	@InjectView( R.id.spinner_row_text )
	TextView spinnerRowText;


	Context context;
	private List<String> spinnerImageList;
	private List<String> myRowTextList;


	public SpinnerAdapter(Context context, int textViewResourceId,
						   List<String> myRowTextList, List<String> iconUrlList) {
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

	public View getCustomView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View row = inflater.inflate( R.layout.spinnerrow, parent, false);
		spinnerRowText = (TextView) row.findViewById( R.id.spinner_row_text );
		spinnerRowText.setText( myRowTextList.get( position ).toString() );
		spinnerRowImage = (ImageView) row.findViewById( R.id.spinner_row_image );
		Picasso.with( getContext() ).load( spinnerImageList.get( position ) ).into( spinnerRowImage );



		return row;
	}
}
