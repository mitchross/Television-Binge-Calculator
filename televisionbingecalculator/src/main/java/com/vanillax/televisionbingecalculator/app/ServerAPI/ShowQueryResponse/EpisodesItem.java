package com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitch on 6/8/14.
 */
public class EpisodesItem implements Parcelable
{
	@SerializedName( "season" )
	public int season;

	@SerializedName( "number" )
	public int number;


	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel( Parcel dest, int flags )
	{
		dest.writeInt( this.season );
		dest.writeInt( this.number );
	}

	public EpisodesItem()
	{
	}

	private EpisodesItem( Parcel in )
	{
		this.season = in.readInt();
		this.number = in.readInt();
	}

	public static final Parcelable.Creator<EpisodesItem> CREATOR = new Parcelable.Creator<EpisodesItem>()
	{
		public EpisodesItem createFromParcel( Parcel source )
		{
			return new EpisodesItem( source );
		}

		public EpisodesItem[] newArray( int size )
		{
			return new EpisodesItem[ size ];
		}
	};
}
