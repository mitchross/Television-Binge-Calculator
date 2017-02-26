package com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by mitch on 6/8/14.
 */
public class SeasonsItem implements Parcelable
{
	@SerializedName( "season"  )
	private int seasonNumber;

	@SerializedName( "episodes" )
	private ArrayList<EpisodesItem> episodesItemList;

	public int getSeasonEpisodeCount()
	{
		return episodesItemList.size();
	}

	public int getSeasonNumber()
	{
		return seasonNumber;
	}


	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel( Parcel dest, int flags )
	{
		dest.writeInt( this.seasonNumber );
		dest.writeSerializable( this.episodesItemList );
	}

	public SeasonsItem()
	{
	}

	private SeasonsItem( Parcel in )
	{
		this.seasonNumber = in.readInt();
		this.episodesItemList = (ArrayList<EpisodesItem>) in.readSerializable();
	}

	public static final Parcelable.Creator<SeasonsItem> CREATOR = new Parcelable.Creator<SeasonsItem>()
	{
		public SeasonsItem createFromParcel( Parcel source )
		{
			return new SeasonsItem( source );
		}

		public SeasonsItem[] newArray( int size )
		{
			return new SeasonsItem[ size ];
		}
	};
}
