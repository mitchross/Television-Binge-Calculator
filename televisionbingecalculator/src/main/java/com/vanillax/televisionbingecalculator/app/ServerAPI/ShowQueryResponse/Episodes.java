package com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitch on 6/8/14.
 */
public class Episodes implements Parcelable
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

	public Episodes()
	{
	}

	private Episodes( Parcel in )
	{
		this.season = in.readInt();
		this.number = in.readInt();
	}

	public static final Parcelable.Creator<Episodes> CREATOR = new Parcelable.Creator<Episodes>()
	{
		public Episodes createFromParcel( Parcel source )
		{
			return new Episodes( source );
		}

		public Episodes[] newArray( int size )
		{
			return new Episodes[ size ];
		}
	};
}
