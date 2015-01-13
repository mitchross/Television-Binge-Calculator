package com.vanillax.televisionbingecalculator.app.TBC.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.TBC.BaseActivity;
import com.vanillax.televisionbingecalculator.app.TBC.Utils.CalculatorUtils;
import com.vanillax.televisionbingecalculator.app.TBC.Utils.IntentHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ShowDetailsActivity extends BaseActivity {

	@InjectView(R.id.poster_image)
	ImageView posterImage;

	@InjectView(R.id.seasons_count)
	TextView seasonsCountTextView;

	@InjectView(R.id.episode_total)
	TextView episdoeCountTextView;

	@InjectView( R.id.episode_length )
	TextView episodeRunTime;

	@InjectView(R.id.binge_time)
	TextView bingTimeText;


	protected String numberSeasons;
	public String episodeCount;
	public int runtime;
	protected String bingeTime;
	protected String imageUrl;
	protected String title;




	@OnClick( R.id.fine_tune_link )
	 protected void showFineTune()
	{
		final Dialog dialog = new Dialog( this );
		dialog.setContentView( R.layout.settings_popup );


		Button dialogButton = (Button) dialog.findViewById( R.id.settings_done );
		final EditText openingCreditsTime = (EditText) dialog.findViewById( R.id.opening_credits_edittext );
		final EditText closingCreditsTime = (EditText) dialog.findViewById( R.id.closing_credits_edittext );
		final CheckBox hasCommercialsCheckBox = (CheckBox) dialog.findViewById( R.id.commercial_checkbox );


		dialogButton.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				int openCreditTime = openingCreditsTime.getText().toString().isEmpty() ? 0 : Integer.parseInt( openingCreditsTime.getText().toString() );
				int closingCreditTime = closingCreditsTime.getText().toString().isEmpty() ? 0 : Integer.parseInt( closingCreditsTime.getText().toString() );
				boolean hasCommercials = hasCommercialsCheckBox.isChecked();

				String fineTuned = CalculatorUtils.calcFineTuneTime( ShowDetailsActivity.this ,
																	Integer.parseInt( episodeCount ),
																	runtime,
																	openCreditTime  ,
																	closingCreditTime ,
																	hasCommercials  );
				bingTimeText.setText( fineTuned );
				dialog.dismiss();

			}
		} );

		dialog.show();

	}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		ButterKnife.inject( this );

		numberSeasons = getIntent().getStringExtra( IntentHelper.NUMBER_SEASONS );
		episodeCount = getIntent().getStringExtra( IntentHelper.EPISDOE_COUNT );
		runtime = getIntent().getIntExtra( IntentHelper.EPISDOE_RUNTIME , 0  );
		bingeTime = getIntent().getStringExtra( IntentHelper.BINGE_TIME );
		imageUrl = getIntent().getStringExtra( IntentHelper.IMAGE_URL );
		title = getIntent().getStringExtra( IntentHelper.SHOW_TITLE );


		episodeRunTime.setText( "" + runtime );
		seasonsCountTextView.setText( numberSeasons );
		episdoeCountTextView.setText( episodeCount );
		bingTimeText.setText(  bingeTime );

		Picasso.with( getApplicationContext() )
				.load( imageUrl )
				.fit()
				.into( posterImage );

        getSupportActionBar().setTitle( title );



    }

    @Override
    protected int getLayoutResource() {
        return  R.layout.activity_show_details_material_card;
    }

    @Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

}
