package com.example.kr2rpois;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener, TimePickerDialog.OnTimeSetListener {

    final String LOG_TAG = "myLogs";

    Button btnAdd, btnRead, btnClear, btnTime;
    EditText etHeading, etText;
    DBHelper dbHelper;

    private NotifyAdapter notifyAdapter;
    RecyclerView recyclerView;
    private List<DataBD> dataBDList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etHeading = findViewById(R.id.editHeading);
        etText = findViewById(R.id.editText);

        btnTime = findViewById(R.id.buttonTIme);
        btnTime.setOnClickListener(this);

        dbHelper = new DBHelper(this, this);

        initRecyclerView();
        getDataFromBD();
        loadData();

    }


    public void getDataFromBD() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dataBDList.clear();
        Cursor c = db.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (c.moveToFirst()) {

            int idColIndex = c.getColumnIndex(DBHelper.KEY_ID);
            int headingColIndex = c.getColumnIndex(DBHelper.KEY_HEADING);
            int textColIndex = c.getColumnIndex(DBHelper.KEY_TEXT);
            int oursColIndex = c.getColumnIndex(DBHelper.KEY_TIME_OURS);
            int minsColIndex = c.getColumnIndex(DBHelper.KEY_TIME_MINS);


            do {
                DataBD dataBD = new DataBD();
                dataBD.setId(Integer.parseInt(c.getString(idColIndex)));
                dataBD.setHeading(c.getString(headingColIndex));
                dataBD.setText(c.getString(textColIndex));
                dataBD.setOurs(Integer.parseInt(c.getString(oursColIndex)));
                dataBD.setMins(Integer.parseInt((c.getString(minsColIndex))));

                dataBDList.add(dataBD);
            } while (c.moveToNext());
        }
        db.close();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        notifyAdapter = new NotifyAdapter(this, this,alarmManager);
        recyclerView.setAdapter(notifyAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadData() {
        Collection<DataBD> dataBDS = dataBDList;
        notifyAdapter.clearItems();
        notifyAdapter.notifyDataSetChanged();
        notifyAdapter.setItems(dataBDS);

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        String heading = etHeading.getText().toString();
        String text = etText.getText().toString();


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        switch (v.getId()) {
            case R.id.btnAdd:

                cv.put(DBHelper.KEY_HEADING, heading);
                cv.put(DBHelper.KEY_TEXT, text);
                cv.put(DBHelper.KEY_TIME_OURS, ours);
                cv.put(DBHelper.KEY_TIME_MINS, mins);
                long a = db.insert(DBHelper.TABLE_CONTACTS, null, cv);

                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, ours);
                c.set(Calendar.MINUTE, mins);
                c.set(Calendar.SECOND, 0);

                getDataFromBD();
                loadData();
                startAlarm(c, heading, text, (int) a);
                break;

            case R.id.btnRead:

                Cursor cursor = db.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

                if (cursor.moveToFirst()) {

                    int idColIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int headingColIndex = cursor.getColumnIndex(DBHelper.KEY_HEADING);
                    int textColIndex = cursor.getColumnIndex(DBHelper.KEY_TEXT);

                    do {
                        Log.d(LOG_TAG,
                                "ID = " + cursor.getInt(idColIndex) +
                                        ", head = " + cursor.getString(headingColIndex) +
                                        ", text = " + cursor.getString(textColIndex));

                    } while (cursor.moveToNext());
                } else
                    Log.d(LOG_TAG, "0 rows");

                cursor.close();

                break;

            case R.id.btnClear:
                Log.d(LOG_TAG, "--- Clear mytable: ---");
                int clearCount = db.delete(DBHelper.TABLE_CONTACTS, null, null);
                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                break;

            case R.id.buttonTIme:
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                break;
        }
        dbHelper.close();
    }

    private int mins;
    private int ours;

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mins = minute;
        ours = hourOfDay;
    }


    private void startAlarm(Calendar c, String heading, String text, int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("Heading", heading);
        intent.putExtra("Text", text);
        intent.putExtra("ID", id);
        Log.d(LOG_TAG, "id последнего (по идее) = " + id);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }


    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.cancel(pendingIntent);

    }
}