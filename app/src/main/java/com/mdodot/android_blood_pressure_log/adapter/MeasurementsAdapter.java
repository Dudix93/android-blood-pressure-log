package com.mdodot.android_blood_pressure_log.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mdodot.android_blood_pressure_log.R;
import com.mdodot.android_blood_pressure_log.activity.MeasurementDetailsActivity;
import com.mdodot.android_blood_pressure_log.entity.MeasurementEntity;
import com.mdodot.android_blood_pressure_log.fragment.MeasurementsListFragment;

import java.io.Serializable;
import java.util.EventListener;
import java.util.List;

public class MeasurementsAdapter extends RecyclerView.Adapter<MeasurementsAdapter.MeasurementViewHolder> {

    private List<MeasurementEntity> measurementsList;
    private Context mContext;
    private MeasurementsListFragment measurementsListFragment;

    public MeasurementsAdapter(List<MeasurementEntity> measurementsList, Context context, MeasurementsListFragment measurementsListFragment) {
        this.measurementsList = measurementsList;
        this.mContext = context;
        this.measurementsListFragment = measurementsListFragment;
    }

    public static class MeasurementViewHolder extends RecyclerView.ViewHolder {
        CardView measurementCardView;
        TextView systolicTextView;
        TextView diastolicTextView;
        TextView pulseTextView;
        TextView dateTextView;
        TextView timeTextView;

        public MeasurementViewHolder(View view) {
            super(view);
            this.measurementCardView = view.findViewById(R.id.measurement_list_item);
            this.systolicTextView = view.findViewById(R.id.meastrement_systolic);
            this.diastolicTextView = view.findViewById(R.id.meastrement_diastolic);
            this.pulseTextView = view.findViewById(R.id.meastrement_pulse);
            this.dateTextView = view.findViewById(R.id.measurement_date);
            this.timeTextView = view.findViewById(R.id.meastrement_time);
        }

        public CardView getMeasurementCardView() { return this.measurementCardView; }
        public TextView getSystolicTextView() { return this.systolicTextView; }
        public TextView getDiastolicTextView() { return this.diastolicTextView; }
        public TextView getPulseTextView() { return this.pulseTextView; }
        public TextView getDateTextView() { return this.dateTextView; }
        public TextView getTimeTextView() { return this.timeTextView; }
    }

    @Override
    public MeasurementViewHolder onCreateViewHolder(ViewGroup viewGroup, int viwType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_measurement, viewGroup, false);
        return new MeasurementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MeasurementViewHolder viewHolder, final int position) {
        viewHolder.getSystolicTextView().setText(String.valueOf(this.measurementsList.get(position).getSystolic()));
        viewHolder.getDiastolicTextView().setText(String.valueOf(this.measurementsList.get(position).getDiastolic()));
        viewHolder.getPulseTextView().setText(String.valueOf(this.measurementsList.get(position).getPulse()));
        viewHolder.getDateTextView().setText(this.measurementsList.get(position).getDate());
        viewHolder.getTimeTextView().setText(this.measurementsList.get(position).getTime());

        viewHolder.getMeasurementCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MeasurementDetailsActivity.class);
                intent.putExtra("measurement", measurementsList.get(position));
                measurementsListFragment.launchMeasurementDetailsActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.measurementsList.size();
    }

}
