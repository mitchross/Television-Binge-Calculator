package com.vanillax.televisionbingecalculator.app.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;

import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.TBC.Activity.LandingActivityMain;
import com.vanillax.televisionbingecalculator.app.databinding.HomescreenLandingViewBinding;
import com.vanillax.televisionbingecalculator.app.viewmodel.SplashScreenViewModel;


public class SplashActivity extends AppCompatActivity implements SplashScreenViewModel.SplashScreenViewModelCallback
{

	SplashScreenViewModel viewModel;
	HomescreenLandingViewBinding binding;


	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		viewModel = new SplashScreenViewModel();
		binding = DataBindingUtil.setContentView( this, R.layout.homescreen_landing_view );
		binding.setViewModel( viewModel );


		if ( android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP )
		{
			setupWindowAnimations();
		}

	}

	@Override
	protected void onStart()
	{
		viewModel.onViewAttached( this );
		super.onStart();

	}

	@Override
	protected void onDestroy()
	{
		viewModel.onViewDetached();
		super.onDestroy();

	}

	@Override
	protected void onResume()
	{
		super.onResume();
		viewModel.onViewResumed();
	}

	@TargetApi( Build.VERSION_CODES.LOLLIPOP )
	private void setupWindowAnimations()
	{
		Transition slide = null;

		slide = TransitionInflater.from(this).inflateTransition( R.transition.activity_slide);

		getWindow().setExitTransition(slide);
	}

	@Override
	public void onEditTextTapped()
	{
		Intent i = new Intent( SplashActivity.this, LandingActivityMain.class );
		startActivity(i);
	}
}
