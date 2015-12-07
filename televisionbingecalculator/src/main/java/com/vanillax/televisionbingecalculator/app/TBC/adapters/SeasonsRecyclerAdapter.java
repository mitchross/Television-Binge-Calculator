package com.vanillax.televisionbingecalculator.app.TBC.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.vanillax.televisionbingecalculator.app.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by mitchross on 12/9/14.
 */
public class SeasonsRecyclerAdapter extends RecyclerView.Adapter<SeasonsRecyclerAdapter.MyViewHolder>
{
    public  List<Integer> seasonsList;
    private Context context;
    private int rowLayout;
    private OnShowClickListener listener;

	public static int lastSelectedPostion = 0;


    public interface OnShowClickListener
    {
        public void onSeasonsClicked( int showPosition );
    }

    protected static class MyViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.seasons_adapter_row_season_number)
		CheckBox seasonNumberCheckBox;

		@OnCheckedChanged( R.id.seasons_adapter_row_season_number )
		public void onCheck(boolean checked)
		{
			if ( checked )
			{
				lastSelectedPostion = getAdapterPosition();
				seasonNumberCheckBox.setTextColor( context.getResources().getColor( R.color.material_blue ) );

			}
			else
			{
				seasonNumberCheckBox.setTextColor( context.getResources().getColor( R.color.material_gray_600 ) );
			}
		}



        @OnClick( R.id.seasons_adapter_row_season_number )
        protected void onSeasonTapped()
        {
            listener.onSeasonsClicked( getAdapterPosition() );
        }

        OnShowClickListener listener;
        Context context;

        public MyViewHolder( Context context,View view , final OnShowClickListener listener) {
            super(view);
            ButterKnife.inject(this, view);
            this.listener = listener;
            this.context = context;


        }
    }

        public SeasonsRecyclerAdapter( List<Integer> seasonsList, int rowLayout, Context context, OnShowClickListener listener )
        {
            this.seasonsList = seasonsList;
            this.rowLayout = rowLayout;
            this.context = context;
            this.listener = listener;
        }

	public void setSelected(int position)
	{
		lastSelectedPostion = position;
	}

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new MyViewHolder(viewGroup.getContext(), v , listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myView, int position)
    {
		myView.seasonNumberCheckBox.setChecked( position == lastSelectedPostion  );
        myView.seasonNumberCheckBox.setText( seasonsList.get( position ).toString() );
    }



    @Override
    public int getItemCount()
    {
        return seasonsList == null ? 0 : seasonsList.size() ;
    }


}
