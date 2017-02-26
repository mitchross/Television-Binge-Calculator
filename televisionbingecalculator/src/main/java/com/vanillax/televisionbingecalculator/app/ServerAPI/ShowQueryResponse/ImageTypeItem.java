package com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitch on 6/14/14.
 */
public class ImageTypeItem implements Parcelable
{
	@SerializedName( "poster" )
	public String posterUrl;

	@SerializedName( "banner" )
	public String bannerUrl;

	@SerializedName( "fanart" )
	public String fanart;


	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel( Parcel dest, int flags )
	{
		dest.writeString( this.posterUrl );
		dest.writeString( this.bannerUrl );
		dest.writeString( this.fanart );
	}

	public ImageTypeItem()
	{
	}

	private ImageTypeItem( Parcel in )
	{
		this.posterUrl = in.readString();
		this.bannerUrl = in.readString();
		this.fanart = in.readString();
	}

	public static final Parcelable.Creator<ImageTypeItem> CREATOR = new Parcelable.Creator<ImageTypeItem>()
	{
		public ImageTypeItem createFromParcel( Parcel source )
		{
			return new ImageTypeItem( source );
		}

		public ImageTypeItem[] newArray( int size )
		{
			return new ImageTypeItem[ size ];
		}
	};
}
