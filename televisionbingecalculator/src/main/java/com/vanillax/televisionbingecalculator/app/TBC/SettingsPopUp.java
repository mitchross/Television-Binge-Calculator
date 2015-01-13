package com.vanillax.televisionbingecalculator.app.TBC;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.vanillax.televisionbingecalculator.app.R;

import butterknife.ButterKnife;

/**
 * Created by mitchross on 1/11/15.
 */
public class SettingsPopUp extends DialogFragment
{
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), 0 );
		dialog.setContentView(R.layout.settings_popup);
		ButterKnife.inject( this, dialog.getWindow().getDecorView() ); //only if you use ButterKnife library

		return dialog;
	}


}
