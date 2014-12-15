package com.vanillax.televisionbingecalculator.app.TBC.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vanillax.televisionbingecalculator.app.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by mitchross on 12/9/14.
 */
public class ShowRecyclerAdapter extends RecyclerView.Adapter<ShowRecyclerAdapter.MyViewHolder>
{
    private List<String> showsListings;
    private List<String> showImageUrls;
    private Context context;
    private int rowLayout;
    private OnShowClickListener listener;

    public interface OnShowClickListener
    {
        public void onShowClicked( int showPosition );
    }

    protected static class MyViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.spinner_row_image)
        ImageView spinnerRowImage;

        @InjectView(R.id.spinner_row_text)
        TextView spinnerRowText;

        public MyViewHolder(View view , final OnShowClickListener listener) {
            super(view);
            ButterKnife.inject(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( listener != null )
                    {
                        listener.onShowClicked( getPosition());
                    }
                }
            });
        }
    }

        public ShowRecyclerAdapter(List<String> showsListings, List<String> showImageUrls, int rowLayout, Context context, OnShowClickListener listener )
        {
            this.showsListings = showsListings;
            this.showImageUrls = showImageUrls;
            this.rowLayout = rowLayout;
            this.context = context;
            this.listener = listener;
        }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new MyViewHolder(v , listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myView, int position)
    {
        myView.spinnerRowText.setText( showsListings.get( position ).toString() );
        Picasso.with(context).load( showImageUrls.get( position ) ).into(myView.spinnerRowImage);
    }



    @Override
    public int getItemCount() {
        return showsListings == null ? 0 : showsListings.size() ;
    }


}
