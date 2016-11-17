package com.vanillax.televisionbingecalculator.app.TBC.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vanillax.televisionbingecalculator.app.R;
import com.vanillax.televisionbingecalculator.app.ServerAPI.GuideBoxResponse.GuideBoxAvailableContentResponse;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by mitchross on 12/9/14.
 */
public class StreamingSourceRecyclerAdapter extends RecyclerView.Adapter<StreamingSourceRecyclerAdapter.MyViewHolder>
{
    public  List<GuideBoxAvailableContentResponse.StreamSource>  streamingList;
    private Context context;
    private int rowLayout;

	public static int lastSelectedPostion = 0;



    protected static class MyViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.streaming_source)
        ImageView streamingSourceImg;

        Context context;

        public MyViewHolder( Context context,View view ) {
            super(view);
            ButterKnife.inject(this, view);
            this.context = context;


        }
    }

        public StreamingSourceRecyclerAdapter( List<GuideBoxAvailableContentResponse.StreamSource> streamingList, int rowLayout, Context context)
        {
            this.streamingList = streamingList;
            this.rowLayout = rowLayout;
            this.context = context;
        }

	public void setSelected(int position)
	{
		lastSelectedPostion = position;
	}

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new MyViewHolder(viewGroup.getContext(), v );
    }

    @Override
    public void onBindViewHolder(MyViewHolder myView, int position)
    {
        String streamingIconName = streamingList.get( position ).sourceDisplayName.toLowerCase();

        if( streamingIconName.contains( "netflix" ))
        {
            myView.streamingSourceImg.setImageResource( R.drawable.amazon );
        }
        if ( streamingIconName.contains( "hulu" ))
        {
            myView.streamingSourceImg.setImageResource( R.drawable.amazon );

        }
        if ( streamingIconName.contains( "amazon" ))
        {
            myView.streamingSourceImg.setImageResource( R.drawable.amazon );
        }
        if ( streamingIconName.contains( "vudu" ))
        {
            myView.streamingSourceImg.setImageResource( R.drawable.amazon );
        }


    }



    @Override
    public int getItemCount()
    {
        return streamingList.size() ;
    }


}
