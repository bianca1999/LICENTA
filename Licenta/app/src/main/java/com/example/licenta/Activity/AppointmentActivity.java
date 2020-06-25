package com.example.licenta.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import android.widget.TextView;

import com.example.licenta.R;


public class AppointmentActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private TextView appointmentDate;
    private Button continua;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        calendarView=findViewById(R.id.calendarView);
        appointmentDate=findViewById(R.id.data);
        continua=findViewById(R.id.continua);

        final String patientId=getIntent().getStringExtra("patient_id");
        final String doctorId=getIntent().getStringExtra("doctor_id");


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date =dayOfMonth + " . 0" + month + " . " + year;
                appointmentDate.setText(date);

            }
        });
        continua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AppointmentActivity.this, ConfirmAppointmentActivity.class);
                intent.putExtra("data",appointmentDate.getText().toString());
                intent.putExtra("doctor_id",doctorId);
                intent.putExtra("patient_id",patientId);
                startActivity(intent);
            }
        });
    }
}
