package liormic.com.onewheater.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import liormic.com.onewheater.R;
import liormic.com.onewheater.wheater.Hour;

/**
 * Created by Liorm on 6/6/2016.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder>{

     private Hour mHours[];

    public HourAdapter(Hour[] hour){
        mHours = hour;
    }
    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_list_item,parent,false);
        HourViewHolder viewHolder = new HourViewHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
     holder.bindHour(mHours[position]);
    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTimeLabel;
        public TextView mSummaryLabel;
        public TextView mTemperatureLabel;
        public ImageView mIconImageView;

        public HourViewHolder(View itemView) {
            super(itemView);

            mTimeLabel = (TextView) itemView.findViewById(R.id.timeLabel1);
            mSummaryLabel = (TextView) itemView.findViewById(R.id.summaryLabel);
            mTemperatureLabel = (TextView) itemView.findViewById(R.id.temperatureLabel);
            mIconImageView = (ImageView) itemView.findViewById(R.id.iconimageview);
            itemView.setOnClickListener(this);
        }
        public void bindHour(Hour hour) {

            mTimeLabel.setText(hour.getHour());
            mSummaryLabel.setText(hour.getSummary());
            mTemperatureLabel.setText(hour.getTemperature() + "");
            mIconImageView.setImageResource(hour.getIconId());
        }

        @Override
        public void onClick(View v) {

        }
    }


}
