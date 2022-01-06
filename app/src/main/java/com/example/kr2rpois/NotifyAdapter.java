package com.example.kr2rpois;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.NotifyViewHolder> {
    private List<DataBD> dataBDList = new ArrayList<>();
    Context context;
    MainActivity mainActivity;
    AlarmManager alarmManager;

    public NotifyAdapter(Context context, MainActivity mainActivity, AlarmManager alarmManager) {
        this.context = context;
        this.mainActivity = mainActivity;
        this.alarmManager = alarmManager;
    }

    class NotifyViewHolder extends RecyclerView.ViewHolder {
        private TextView mainTextTV, timeTV, headingTV;
        private Button buttonDelete;

        @SuppressLint("SetTextI18n")
        public void bind(DataBD dataBD) {
            headingTV.setText(dataBD.getHeading());
            mainTextTV.setText(dataBD.getText());
            timeTV.setText(dataBD.getOurs() + ":" + dataBD.getMins());
        }

        public NotifyViewHolder(View itemView) {
            super(itemView);
            headingTV = itemView.findViewById(R.id.textView3);
            mainTextTV = itemView.findViewById(R.id.textView4);
            timeTV = itemView.findViewById(R.id.textView5);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    removeAt(getAdapterPosition());
                }
            });

        }
    }


    public void setItems(Collection<DataBD> data) {
        dataBDList.addAll(data);
        notifyDataSetChanged();
    }

    public void notifyData() {
        notifyDataSetChanged();

    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearItems() {
        dataBDList.clear();
        notifyDataSetChanged();
    }

    @Override
    public NotifyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_layout, parent, false);
        return new NotifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotifyViewHolder holder, int position) {
        holder.bind(dataBDList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataBDList.size();
    }

    public void removeAt(int position) {

        DBHelper dbHelper = new DBHelper(mainActivity, context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID + "=" + dataBDList.get(position).getId(), null);

        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, dataBDList.get(position).getId(), intent, 0);
        alarmManager.cancel(pendingIntent);

        dataBDList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataBDList.size());
    }

}
