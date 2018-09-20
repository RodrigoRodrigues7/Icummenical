package com.example.android.icummenical.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.example.android.icummenical.R;

import java.text.DateFormat;
import java.util.Calendar;

public class CadastrarEventoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private ImageView imgDatePicker, imgTimePicker;
    private EditText edtData, edtHorario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_evento);

        imgDatePicker = findViewById(R.id.imgView_datePicker);
        imgTimePicker = findViewById(R.id.imgView_timePicker);
        edtHorario = findViewById(R.id.edtHorario);
        edtData = findViewById(R.id.edtData);

        imgDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialog = new DatePickerFragment();
                dialog.show(getSupportFragmentManager(), "Date Picker");

            }
        });

        imgTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialog = new TimePickerFragment();
                dialog.show(getSupportFragmentManager(), "TimePicker");

            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.YEAR, year);
        calendar.set(calendar.MONTH, month);
        calendar.set(calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        edtData.setText(currentDate);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        edtHorario.setText(hourOfDay + ":" + minute);
    }



}
