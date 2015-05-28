package com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by mitch on 6/8/14.
 */
public class Seasons implements Parcelable
{
	@SerializedName( "season"  )
	public int seasonList;

	@SerializedName( "episodes" )
	public ArrayList<Episodes> episodesList;

	public int getSeasonEpisodeCount()
	{
		return episodesList.size();
	}


	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel( Parcel dest, int flags )
	{
		dest.writeInt( this.seasonList );
		dest.writeSerializable( this.episodesList );
	}

	public Seasons()
	{
	}

	private Seasons( Parcel in )
	{
		this.seasonList = in.readInt();
		this.episodesList = (ArrayList<Episodes>) in.readSerializable();
	}

	public static final Parcelable.Creator<Seasons> CREATOR = new Parcelable.Creator<Seasons>()
	{
		public Seasons createFromParcel( Parcel source )
		{
			return new Seasons( source );
		}

		public Seasons[] newArray( int size )
		{
			return new Seasons[ size ];
		}
	};
}
