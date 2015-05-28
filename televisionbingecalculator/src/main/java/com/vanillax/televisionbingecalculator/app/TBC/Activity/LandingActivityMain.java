package com.vanillax.televisionbingecalculator.app.TBC.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryMasterAPI;
import com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse.ShowQueryMasterResponse;
import com.vanillax.televisionbingecalculator.app.TBC.BaseActivity;
import com.vanillax.televisionbingecalculator.app.TBC.ShowManager;
import com.vanillax.televisionbingecalculator.app.TBC.TelevisionBingeCalculator;
import com.vanillax.televisionbingecalculator.app.TBC.Utils.CalculatorUtils;
import com.vanillax.televisionbingecalculator.app.TBC.adapters.ShowRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import roboguice.util.Ln;


public class LandingActivityMain extends BaseActivity implements ShowRecyclerAdapter.OnShowClickListener {



	ShowRecyclerAdapter showRecyclerAdapter;
	ShowQueryMasterResponse myShow;
	List<ShowQueryMasterResponse> myShows;

	int runTime;
	int SeasonCount;
	String imageURL;
	int totalEpisodes = 0;
	int totalBingTime;
	String showTitle;


	@Inject
	ShowQueryMasterAPI showQueryMasterAPI;

	@Inject
	ShowManager showManager;


	@InjectView( R.id.search_field )
	EditText searchField;

	@InjectView( R.id.progress_bar )
	SmoothProgressBar progressBar;

	@InjectView( R.id.default_listview_text )
	TextView defaultText;

	@InjectView( R.id.tv_icon )
	ImageView tvIcon;


	@Optional
	@OnClick( R.id.search_button )
	protected void searchShow()
	{
		String showToSearch = searchField.getText().toString();
		showQueryMasterAPI.queryShow( showToSearch , true , new ShowQueryMasterResponseCallback() );
	}

	@InjectView( R.id.list_view )
    RecyclerView listView;




	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        Crashlytics.start( this );
       // setContentView( R.layout.activity_landing_activity_main );
		TelevisionBingeCalculator.inject( this );
		ButterKnife.inject( this );


		// set up the action listener for the text field
		searchField.setOnEditorActionListener( new EditText.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction( final TextView searchTextView, int actionID, KeyEvent event )
			{
					progressBar.setVisibility( View.VISIBLE );
					Ln.d( "onEditorAction... searchTerm: %s", searchTextView.getText().toString() );
					String showToSearch = searchField.getText().toString();
					showQueryMasterAPI.queryShow( showToSearch, true, new ShowQueryMasterResponseCallback() );
					return true;
			}

		} );

        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setItemAnimator(new DefaultItemAnimator());


    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main_material;
    }

    @Override
	protected void onResume()
	{
		super.onResume();

		if ( myShows == null  )
		{
			defaultText.setVisibility( View.VISIBLE );
			tvIcon.setVisibility( View.VISIBLE );
		}


	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
    public void onShowClicked(int showPosition) {
        ShowQueryMasterResponse selectedShow;
		selectedShow = myShows.get( showPosition );
		showManager.setShow( selectedShow );
		Intent intent = new Intent( this , ShowDetailsActivity.class );
		startActivity( intent );


    }

    public class ShowQueryMasterResponseCallback implements Callback< List<ShowQueryMasterResponse> >
	{

		@Override
		public void success( List<ShowQueryMasterResponse> showQueryMasterResponses, Response response )
		{
			progressBar.setVisibility( View.GONE );

			if ( showQueryMasterResponses !=null  )
			{
				defaultText.setVisibility( View.GONE );
				tvIcon.setVisibility( View.GONE );
			}

			myShows = showQueryMasterResponses;

			ArrayList<String> showTitles = new ArrayList<String>(  );
			ArrayList<String> showPosters =  new ArrayList<String>(  );


			for ( ShowQueryMasterResponse show : showQueryMasterResponses)
			{
				showTitles.add( show.title );
				showPosters.add( CalculatorUtils.getShowPosterThumbnail( show.images.posterUrl ) );

			}

			showRecyclerAdapter = new ShowRecyclerAdapter( showTitles , showPosters , R.layout.spinnerrow , getApplicationContext() , LandingActivityMain.this);

			listView.setAdapter( showRecyclerAdapter );

		}

		@Override
		public void failure( RetrofitError retrofitError )
		{
			progressBar.setVisibility( View.GONE );
			Ln.d("fail");
		}
	}

}
