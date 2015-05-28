package com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitch on 6/14/14.
 */
public class ImageType implements Parcelable
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

	public ImageType()
	{
	}

	private ImageType( Parcel in )
	{
		this.posterUrl = in.readString();
		this.bannerUrl = in.readString();
		this.fanart = in.readString();
	}

	public static final Parcelable.Creator<ImageType> CREATOR = new Parcelable.Creator<ImageType>()
	{
		public ImageType createFromParcel( Parcel source )
		{
			return new ImageType( source );
		}

		public ImageType[] newArray( int size )
		{
			return new ImageType[ size ];
		}
	};
}
