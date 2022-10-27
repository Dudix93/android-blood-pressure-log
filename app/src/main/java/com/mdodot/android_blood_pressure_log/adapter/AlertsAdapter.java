package com.mdodot.android_blood_pressure_log.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mdodot.android_blood_pressure_log.OnAlertLongClickCallback;
import com.mdodot.android_blood_pressure_log.R;
import com.mdodot.android_blood_pressure_log.entity.AlertEntity;

import java.util.List;

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.AlertViewHolder> {

    private List<AlertEntity> alertsList;
    private Context mContext;
    private OnAlertLongClickCallback listener;

    public AlertsAdapter(List<AlertEntity> alertsList, Context context) {
        this.alertsList = alertsList;
        this.mContext = context;
    }

    public static class AlertViewHolder extends RecyclerView.ViewHolder {
        private CardView alertCardView;
        private TextView alertTimeTextView;
        private TextView alertNoteTextView;
        private TextView alertMondayTextView;
        private TextView alertTuesdayTextView;
        private TextView alertWednesdayTextView;
        private TextView alertThursdayTextView;
        private TextView alertFridayTextView;
        private TextView alertSaturdayTextView;
        private TextView alertSundayTextView;

        public AlertViewHolder(View view) {
            super(view);
            this.alertCardView = view.findViewById(R.id.alert_list_item);
            this.alertTimeTextView = view.findViewById(R.id.alert_time);
            this.alertNoteTextView = view.findViewById(R.id.alert_note);
            this.alertMondayTextView = view.findViewById(R.id.alert_monday);
            this.alertTuesdayTextView = view.findViewById(R.id.alert_tuesday);
            this.alertWednesdayTextView = view.findViewById(R.id.alert_wednesday);
            this.alertThursdayTextView = view.findViewById(R.id.alert_thursday);
            this.alertFridayTextView = view.findViewById(R.id.alert_friday);
            this.alertSaturdayTextView = view.findViewById(R.id.alert_saturday);
            this.alertSundayTextView = view.findViewById(R.id.alert_sunday);

        }

        public CardView getAlertCardView() { return this.alertCardView; }
        public TextView getAlertTimeTextView() { return this.alertTimeTextView; }
        public TextView getAlertNoteTextView() { return this.alertNoteTextView; }
        public TextView getAlertMondayTextView() { return this.alertMondayTextView; }
        public TextView getAlertTuesdayTextView() { return this.alertTuesdayTextView; }
        public TextView getAlertWednesdayTextView() { return this.alertWednesdayTextView; }
        public TextView getAlertThursdayTextView() { return this.alertThursdayTextView; }
        public TextView getAlertFridayTextView() { return this.alertFridayTextView; }
        public TextView getAlertSaturdayTextView() { return this.alertSaturdayTextView; }
        public TextView getAlertSundayTextView() { return this.alertSundayTextView; }

    }

    @Override
    public AlertViewHolder onCreateViewHolder(ViewGroup viewGroup, int viwType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_alert, viewGroup, false);
        return new AlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlertViewHolder viewHolder, final int position) {
        viewHolder.getAlertTimeTextView().setText(alertsList.get(position).getTime());
        viewHolder.getAlertNoteTextView().setText(alertsList.get(position).getNote());
        markSelectedDays(viewHolder, position);
        viewHolder.getAlertCardView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.openAlertEditDeleteDialog(alertsList.get(position));
                }
                return true;
            }
        });
        viewHolder.getAlertCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.dismissAlertEditDeleteMenu();
                }
            }
        });
    }

    public void setListener(OnAlertLongClickCallback listener)    {
        this.listener = listener;
    }

    public void markSelectedDays(AlertViewHolder viewHolder, int position) {
        int selectedColor = mContext.getResources().getColor(R.color.ABD9FF);
        if (alertsList.get(position).isMonday()) viewHolder.getAlertMondayTextView().setTextColor(selectedColor);
        if (alertsList.get(position).isTuesday()) viewHolder.getAlertTuesdayTextView().setTextColor(selectedColor);
        if (alertsList.get(position).isWednesday()) viewHolder.getAlertWednesdayTextView().setTextColor(selectedColor);
        if (alertsList.get(position).isThursday()) viewHolder.getAlertThursdayTextView().setTextColor(selectedColor);
        if (alertsList.get(position).isFriday()) viewHolder.getAlertFridayTextView().setTextColor(selectedColor);
        if (alertsList.get(position).isSaturday()) viewHolder.getAlertSaturdayTextView().setTextColor(selectedColor);
        if (alertsList.get(position).isSunday()) viewHolder.getAlertSundayTextView().setTextColor(selectedColor);
    }

    @Override
    public int getItemCount() {
        return this.alertsList.size();
    }
}
