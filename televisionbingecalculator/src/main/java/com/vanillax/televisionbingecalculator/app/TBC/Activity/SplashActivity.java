package com.vanillax.televisionbingecalculator.app.TBC.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.widget.EditText;

import com.vanillax.televisionbingecalculator.app.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity
{

	@InjectView( R.id.default_view_edit_text )
	EditText editText;



	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.homescreen_landing_view );
		ButterKnife.inject( this );

		if ( android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP )
		{
			setupWindowAnimations();
		}


	}

	@OnClick( R.id.default_view_edit_text )
	protected void onTap()
	{
		Intent i = new Intent( SplashActivity.this, LandingActivityMain.class );
		startActivity(i);
	}

	@TargetApi( Build.VERSION_CODES.LOLLIPOP )
	private void setupWindowAnimations()
	{
		Transition slide = null;

		slide = TransitionInflater.from(this).inflateTransition( R.transition.activity_slide);

		getWindow().setExitTransition(slide);
	}

}
