package com.example.schiver.projectdua;

import android.app.AlarmManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schiver.projectdua.Model.ConfigData;
import com.example.schiver.projectdua.Model.ConfigDetailData;
import com.example.schiver.projectdua.Model.ConnectedDevice;
import com.example.schiver.projectdua.Model.DeviceUsageData;
import com.example.schiver.projectdua.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


public class LivingroomFragment extends Fragment {
    TextView tempValue,motionValue;
    TextView lampStatus, televisionStatus, acStatus, fanStatus;
    TextView lampLabelText1,lampLabelText2,lampLabelText3;
    TextView televisionLabelText1, televisionLabelText2, televisionLabelText3;
    TextView acLabelText1, acLabelText2, acLabelText3;
    TextView fanLabelText1, fanLabelText2, fanLabelText3;
    SeekBar temperatureSensor;
    RadioGroup motionSensorCondition;
    RadioButton radioChoice;
    FirebaseDatabase myDb;
    DatabaseReference dbRef;
    FirebaseDatabase myDb2;
    DatabaseReference dbRef2;
    FirebaseDatabase myDb3;
    DatabaseReference dbRef3;
    FirebaseDatabase myDb4;
    DatabaseReference dbRef4;

    FirebaseDatabase myDbCondition;
    DatabaseReference dbConditionRef;
    CountDownTimer myTimer,myTimer2,myTimer3,myTimer4,myTimerLampOn;
    Date testDate;
    View rootView2;
    float valueNow;
    boolean mTimer = true;
    int myCounter = 0 , myCounter2 = 0 , myCounter3 = 0, myCounter4 = 0;
    int a = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.livingroom_fragment,container,false);
        rootView2 = rootView;
        myTimer = new CountDownTimer(0,0) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        };
        myTimerLampOn = new CountDownTimer(0,0) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        };

        myTimer2 = new CountDownTimer(0,0) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        };
        myTimer3 = new CountDownTimer(0,0) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        };
        myTimer4 = new CountDownTimer(0,0) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        };
        // Sensor
        temperatureSensor = rootView.findViewById(R.id.seekBar);
        tempValue = rootView.findViewById(R.id.textView5);
        temperatureSensor.setMax(35);
        tempValue.setText(String.valueOf(valueNow).replaceAll(".0*$", " "));
        temperatureSensor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valueNow = progress;
                tempValue.setText(String.valueOf(valueNow).replaceAll(".0*$", ""));
                temperatureUpdate(valueNow);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        motionValue = rootView.findViewById(R.id.textView9);
        motionSensorCondition = rootView.findViewById(R.id.radio_group);
        motionSensorCondition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FirebaseDatabase motionSensorDb = FirebaseDatabase.getInstance();
                final DatabaseReference dbRefMotion = motionSensorDb.getReference("SeThings-Sensors/Livingroom/");
                dbRefMotion.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dbRefMotion.child("Dev-01").setValue(new ConnectedDevice(remoteMotionSensor(rootView)));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        //Lamp
        lampStatus = rootView.findViewById(R.id.textView13);
        lampLabelText1 = rootView.findViewById(R.id.textView14);
        lampLabelText2 = rootView.findViewById(R.id.textView15);
        lampLabelText3 = rootView.findViewById(R.id.textView21);

        // Television
        televisionStatus = rootView.findViewById(R.id.textView18);
        televisionLabelText1 = rootView.findViewById(R.id.textView19);
        televisionLabelText2 = rootView.findViewById(R.id.time_start);
        televisionLabelText3 = rootView.findViewById(R.id.time_end);

        //PC
        acStatus = rootView.findViewById(R.id.textView24);
        acLabelText1 = rootView.findViewById(R.id.textView26);
        acLabelText2 = rootView.findViewById(R.id.time_start4);
        acLabelText3 = rootView.findViewById(R.id.time_start5);

        //Fan
        fanStatus = rootView.findViewById(R.id.textView30);
        fanLabelText1= rootView.findViewById(R.id.textView31);
        fanLabelText2 = rootView.findViewById(R.id.time_start6);
        fanLabelText3 = rootView.findViewById(R.id.time_start7);

        setDefault();


        return rootView;
    }

    public  String remoteMotionSensor(View view){
        int selectedChoice = motionSensorCondition.getCheckedRadioButtonId();
        radioChoice = view.findViewById(selectedChoice);
        return "SENSOR_MOTION_VAL_"+radioChoice.getText().toString().toUpperCase();
    }

    public String remoteTempSensor(){
        return "SENSOR_TEMP_VAL_"+tempValue.getText().toString().toUpperCase();
    }

    public void temperatureUpdate(float value ){
        FirebaseDatabase myTempSensorDb = FirebaseDatabase.getInstance();
        final DatabaseReference myTempSensorDbRef = myTempSensorDb.getReference("SeThings-Sensors/Livingroom");
        myTempSensorDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myTempSensorDbRef.child("Dev-02").setValue(new ConnectedDevice(remoteTempSensor()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Fix
        myDb = FirebaseDatabase.getInstance();
        dbRef = myDb.getReference("SeThings-Config/Livingroom/Dev-03");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myTimer.cancel();
                final ConfigData myConfig = dataSnapshot.getValue(ConfigData.class);
                if(!myConfig.getDeviceCondition().equals("#")){
                    // Dilakukan Config
                    myDbCondition = FirebaseDatabase.getInstance();
                    dbConditionRef = myDbCondition.getReference("SeThings-Detail_Config/Livingroom/Dev-03");
                    dbConditionRef.addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            myTimer.cancel();
                            // Cek koneksi sensor
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            final String currentDateandTime = sdf.format(new Date());
                            final ConfigDetailData myDetailConfig = dataSnapshot.getValue(ConfigDetailData.class);
                            if(myConfig.getGetDeviceConditionConnected().equals("#")){
                                if(myDetailConfig.getDevCondition().equals("TIMER")){
                                    Toast.makeText(getContext(),myDetailConfig.getDevCondition(),Toast.LENGTH_SHORT).show();
                                    // Jalankan fungsi timer
                                     myTimer = new CountDownTimer(Integer.parseInt(myDetailConfig.getDevConditionTimerDuration())*1000,1000){
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            //long millis = millisUntilFinished % 1000;
                                            long second = (millisUntilFinished / 1000) % 60;
                                            long minute = (millisUntilFinished / (1000 * 60)) % 60;
                                            long hour = (millisUntilFinished / (1000 * 60 * 60)) % 24;
                                            String displayTime = "  "+hour+" Hour "+minute+" Min "+second+" Sec";
                                            lampStatus.setText("On");
                                            lampLabelText1.setVisibility(View.VISIBLE);
                                            lampLabelText1.setText(displayTime);
                                            myCounter+=1;
                                        }
                                        @Override
                                        public void onFinish() {
                                            lampStatus.setText("Off");
                                            lampLabelText1.setVisibility(View.GONE);
                                            //updateDataUsage("Livingroom","Dev-03",20,myCounter);
                                            updateConfigOff("Livingroom","Dev-03");
                                            //myCounter = 0;
                                        }
                                    }.start();
                                    mTimer = true;
                                }else if(myDetailConfig.getDevCondition().equals("SCHEDULED")){
                                    //String time = "23:21";
                                    String displayTime = "Set at "+myDetailConfig.getDevConditionStartScheduled()+" to "+myDetailConfig.getDevConditionEndScheduled();
                                    lampLabelText1.setVisibility(View.VISIBLE);
                                    lampLabelText1.setText(displayTime);

                                    String timeStart = myDetailConfig.getDevConditionStartScheduled();
                                    String [] seperatedStart = timeStart.split(":");
                                    int hour_x = Integer.parseInt(seperatedStart[0]);
                                    int minutes_x = Integer.parseInt(seperatedStart[1]);
                                    Calendar calendarStart = Calendar.getInstance();
                                    calendarStart.set(
                                            calendarStart.get(Calendar.YEAR),
                                            calendarStart.get(Calendar.MONTH),
                                            calendarStart.get(Calendar.DAY_OF_MONTH),
                                            hour_x,
                                            minutes_x,
                                            0
                                    );
                                    String timeEnd = myDetailConfig.getDevConditionEndScheduled();
                                    String [] seperatedEnd = timeEnd.split(":");
                                    int hour_y = Integer.parseInt(seperatedEnd[0]);
                                    int minutes_y = Integer.parseInt(seperatedEnd[1]);
                                    Calendar calendarEnd = Calendar.getInstance();
                                    calendarEnd.set(
                                            calendarStart.get(Calendar.YEAR),
                                            calendarStart.get(Calendar.MONTH),
                                            calendarStart.get(Calendar.DAY_OF_MONTH),
                                            hour_y,
                                            minutes_y,
                                            0
                                    );

                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    String printCalendar = sdf2.format(calendarStart.getTime());
                                    String printCalendar2 = sdf2.format(calendarEnd.getTime());
                                    Toast.makeText(getContext(),printCalendar,Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getContext(),printCalendar2,Toast.LENGTH_SHORT).show();
                                    setEventStart(calendarStart);
                                    try {
                                        setEventEnd(calendarEnd, myDetailConfig.getDevConditionStartScheduled(),myDetailConfig.getDevConditionEndScheduled());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }else if(myDetailConfig.getDevCondition().equals("SWITCH_OFF")){
                                    // Update Data
                                    mTimer = false;
                                    updateDataUsage("Livingroom","Dev-03",20,myCounter);
                                    //updateConfigOff("Livingroom","Dev-03");
                                    myCounter = 0;
                                }else if(myDetailConfig.getDevCondition().equals("SWITCH_ON")){
                                    Toast.makeText(getContext(),"Switch on",Toast.LENGTH_SHORT).show();
                                    myTimer = null;
                                    myTimer = new CountDownTimer(43200*1000,1000){
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            //long millis = millisUntilFinished % 1000;
                                            long second = (myCounter) % 60;
                                            long minute = ((myCounter) / (1 * 60)) % 60;
                                            long hour = ((myCounter) / (1 * 60 * 60)) % 24;
                                            //String displayTime = "  "+hour+" Hour "+minute+" Min "+second+" Sec";
                                            String displayTime = "H : "+hour +" M : "+minute+" S : "+second;
                                            lampStatus.setText("On");
                                            lampLabelText1.setVisibility(View.VISIBLE);
                                            lampLabelText1.setText(displayTime);
                                            myCounter+=1;
                                        }
                                        @Override
                                        public void onFinish() {
                                            lampStatus.setText("Off");
                                            lampLabelText1.setVisibility(View.GONE);
                                            //updateDataUsage("Livingroom","Dev-03",20,myCounter);
                                            updateConfigOff("Livingroom","Dev-03");
                                            //myCounter = 0;
                                        }
                                    };
                                    myTimer.start();
                                }
                                // Buat ambil kondisi TIMER dan SCHEDULED
                            }else{
                                // ada sensor
                                //Toast.makeText(getContext(),myDetailConfig.getDevCondition(),Toast.LENGTH_LONG).show();
                                // Buat kondisi sensor -> ambil ke database sensor
                                FirebaseDatabase connectDB = FirebaseDatabase.getInstance();
                                DatabaseReference dbConnectRef = connectDB.getReference("SeThings-Sensors/Livingroom/");
                                dbConnectRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        ConnectedDevice mySensor = dataSnapshot.child(myConfig.getGetDeviceConditionConnected()).getValue(ConnectedDevice.class);
                                        //Toast.makeText(getContext(),myConfig.getDeviceCondition(),Toast.LENGTH_LONG).show();
                                        if(myDetailConfig.getDevCondition().equals(mySensor.getSensorValue())){
                                            if(myDetailConfig.getDevSubCondition().equals("TIMER")){
                                                //Toast.makeText(getContext(),"Sama",Toast.LENGTH_SHORT).show();
                                                myTimer = new CountDownTimer(Integer.parseInt(myDetailConfig.getDevSubConditioTimerDuration())*1000,1000){
                                                    @Override
                                                    public void onTick(long millisUntilFinished) {
                                                        //long millis = millisUntilFinished % 1000;
                                                        long second = (millisUntilFinished / 1000) % 60;
                                                        long minute = (millisUntilFinished / (1000 * 60)) % 60;
                                                        long hour = (millisUntilFinished / (1000 * 60 * 60)) % 24;
                                                        String displayTime = "  "+hour+" Hour "+minute+" Min "+second+" Sec";
                                                        lampStatus.setText("On");
                                                        lampLabelText1.setVisibility(View.VISIBLE);
                                                        lampLabelText1.setText(displayTime);
                                                        myCounter+=1;
                                                    }
                                                    @Override
                                                    public void onFinish() {
                                                        lampStatus.setText("Off");
                                                        lampLabelText1.setVisibility(View.GONE);
                                                        //updateDataUsage("Livingroom","Dev-03",20,myCounter);
                                                        updateConfigOff("Livingroom","Dev-03");
                                                        //myCounter = 0;
                                                    }
                                                };
                                                myTimer.start();
                                            }
                                            //Toast.makeText(getContext(),"Sama",Toast.LENGTH_SHORT).show();
                                            // Get the timer
                                            // Get Sub condition
                                        }else{
                                            //Toast.makeText(getContext(),"Sensor OFF",Toast.LENGTH_SHORT).show();
                                        }
                                        //Toast.makeText(getContext(),mySensor.getSensorValue(),Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                            //myTimer.cancel();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    //myTimer.start();
                    myTimer.cancel();
                    myTimerLampOn.cancel();
                    updateDataUsage("Livingroom","Dev-03",20,myCounter);
                    updateConfigOff("Livingroom","Dev-03");
                    //Toast.makeText(getContext(),"Counter : "+myCounter,Toast.LENGTH_SHORT).show();
                    //updateDataUsage("Livingroom","Dev-03",20,myCounter);
                    //updateConfigOff("Livingroom","Dev-03");
                    // Tidak dilakukan config
                    lampStatus.setText("Off");
                    lampLabelText1.setVisibility(View.GONE);
                    myCounter = 0;
                    //Toast.makeText(getContext(),"Tidak ada Konfig",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Fix
        myDb2 = FirebaseDatabase.getInstance();
        dbRef2 = myDb2.getReference("SeThings-Config/Livingroom/Dev-04");
        dbRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myTimer2.cancel();
                final ConfigData myConfig = dataSnapshot.getValue(ConfigData.class);
                if(!myConfig.getDeviceCondition().equals("#")){
                    // Dilakukan Config
                    myDbCondition = FirebaseDatabase.getInstance();
                    dbConditionRef = myDbCondition.getReference("SeThings-Detail_Config/Livingroom/Dev-04");
                    dbConditionRef.addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            myTimer2.cancel();
                            // Cek koneksi sensor
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            final String currentDateandTime = sdf.format(new Date());
                            final ConfigDetailData myDetailConfig = dataSnapshot.getValue(ConfigDetailData.class);
                            if(myConfig.getGetDeviceConditionConnected().equals("#")){
                                if(myDetailConfig.getDevCondition().equals("TIMER")){
                                    Toast.makeText(getContext(),myDetailConfig.getDevCondition(),Toast.LENGTH_SHORT).show();
                                    // Jalankan fungsi timer
                                    myTimer2 = new CountDownTimer(Integer.parseInt(myDetailConfig.getDevConditionTimerDuration())*1000,1000){
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            //long millis = millisUntilFinished % 1000;
                                            long second = (millisUntilFinished / 1000) % 60;
                                            long minute = (millisUntilFinished / (1000 * 60)) % 60;
                                            long hour = (millisUntilFinished / (1000 * 60 * 60)) % 24;
                                            String displayTime = "  "+hour+" Hour "+minute+" Min "+second+" Sec";
                                            televisionStatus.setText("On");
                                            televisionLabelText1.setVisibility(View.VISIBLE);
                                            televisionLabelText1.setText(displayTime);
                                            myCounter2+=1;
                                        }
                                        @Override
                                        public void onFinish() {
                                            televisionLabelText1.setText("Off");
                                            televisionLabelText1.setVisibility(View.GONE);
                                            //updateDataUsage("Livingroom","Dev-04",75,myCounter2);
                                            updateConfigOff("Livingroom","Dev-04");
                                            //myCounter2 = 0;
                                        }
                                    }.start();
                                    mTimer = true;
                                }else if(myDetailConfig.getDevCondition().equals("SCHEDULED")){
                                    //String time = "23:21";
                                    String displayTime = "Set at "+myDetailConfig.getDevConditionStartScheduled()+" to "+myDetailConfig.getDevConditionEndScheduled();
                                    televisionLabelText1.setVisibility(View.VISIBLE);
                                    televisionLabelText1.setText(displayTime);

                                    String timeStart = myDetailConfig.getDevConditionStartScheduled();
                                    String [] seperatedStart = timeStart.split(":");
                                    int hour_x = Integer.parseInt(seperatedStart[0]);
                                    int minutes_x = Integer.parseInt(seperatedStart[1]);
                                    Calendar calendarStart = Calendar.getInstance();
                                    calendarStart.set(
                                            calendarStart.get(Calendar.YEAR),
                                            calendarStart.get(Calendar.MONTH),
                                            calendarStart.get(Calendar.DAY_OF_MONTH),
                                            hour_x,
                                            minutes_x,
                                            0
                                    );
                                    String timeEnd = myDetailConfig.getDevConditionEndScheduled();
                                    String [] seperatedEnd = timeEnd.split(":");
                                    int hour_y = Integer.parseInt(seperatedEnd[0]);
                                    int minutes_y = Integer.parseInt(seperatedEnd[1]);
                                    Calendar calendarEnd = Calendar.getInstance();
                                    calendarEnd.set(
                                            calendarStart.get(Calendar.YEAR),
                                            calendarStart.get(Calendar.MONTH),
                                            calendarStart.get(Calendar.DAY_OF_MONTH),
                                            hour_y,
                                            minutes_y,
                                            0
                                    );

                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    String printCalendar = sdf2.format(calendarStart.getTime());
                                    String printCalendar2 = sdf2.format(calendarEnd.getTime());
                                    Toast.makeText(getContext(),printCalendar,Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getContext(),printCalendar2,Toast.LENGTH_SHORT).show();
                                    setEventStart2(calendarStart);
                                    try {
                                        setEventEnd2(calendarEnd,myDetailConfig.getDevConditionStartScheduled(),myDetailConfig.getDevConditionEndScheduled());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }else if(myDetailConfig.getDevCondition().equals("SWITCH_OFF")){
                                    // Update Data
                                    mTimer = false;
                                    updateDataUsage("Livingroom","Dev-03",20,myCounter);
                                    //updateConfigOff("Livingroom","Dev-03");
                                    myCounter = 0;
                                }else if(myDetailConfig.getDevCondition().equals("SWITCH_ON")){
                                    Toast.makeText(getContext(),"Switch on",Toast.LENGTH_SHORT).show();
                                    myTimer2 = new CountDownTimer(43200*1000,1000){
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            //long millis = millisUntilFinished % 1000;
                                            long second = (myCounter2) % 60;
                                            long minute = ((myCounter2) / (1 * 60)) % 60;
                                            long hour = ((myCounter2) / (1 * 60 * 60)) % 24;
                                            //String displayTime = "  "+hour+" Hour "+minute+" Min "+second+" Sec";
                                            String displayTime = "H : "+hour +" M : "+minute+" S : "+second;
                                            televisionStatus.setText("On");
                                            televisionLabelText1.setVisibility(View.VISIBLE);
                                            televisionLabelText1.setText(displayTime);
                                            myCounter2+=1;
                                        }
                                        @Override
                                        public void onFinish() {
                                            televisionStatus.setText("Off");
                                            televisionLabelText1.setVisibility(View.GONE);
                                            //updateDataUsage("Livingroom","Dev-04",75,myCounter2);
                                            updateConfigOff("Livingroom","Dev-03");
                                            //myCounter2 = 0;
                                        }
                                    }.start();
                                }
                                // Buat ambil kondisi TIMER dan SCHEDULED
                            }else{
                                // ada sensor
                                //Toast.makeText(getContext(),myDetailConfig.getDevCondition(),Toast.LENGTH_LONG).show();
                                // Buat kondisi sensor -> ambil ke database sensor
                                FirebaseDatabase connectDB = FirebaseDatabase.getInstance();
                                DatabaseReference dbConnectRef = connectDB.getReference("SeThings-Sensors/Livingroom/");
                                dbConnectRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        ConnectedDevice mySensor = dataSnapshot.child(myConfig.getGetDeviceConditionConnected()).getValue(ConnectedDevice.class);
                                        //Toast.makeText(getContext(),myConfig.getDeviceCondition(),Toast.LENGTH_LONG).show();
                                        if(myDetailConfig.getDevCondition().equals(mySensor.getSensorValue())){
                                            if(myDetailConfig.getDevSubCondition().equals("TIMER")){
                                                //Toast.makeText(getContext(),"Sama",Toast.LENGTH_SHORT).show();
                                                myTimer2 = new CountDownTimer(Integer.parseInt(myDetailConfig.getDevSubConditioTimerDuration())*1000,1000){
                                                    @Override
                                                    public void onTick(long millisUntilFinished) {
                                                        //long millis = millisUntilFinished % 1000;
                                                        long second = (millisUntilFinished / 1000) % 60;
                                                        long minute = (millisUntilFinished / (1000 * 60)) % 60;
                                                        long hour = (millisUntilFinished / (1000 * 60 * 60)) % 24;
                                                        String displayTime = "  "+hour+" Hour "+minute+" Min "+second+" Sec";
                                                        televisionStatus.setText("On");
                                                        televisionLabelText1.setVisibility(View.VISIBLE);
                                                        televisionLabelText1.setText(displayTime);
                                                        myCounter2+=1;
                                                    }
                                                    @Override
                                                    public void onFinish() {
                                                        televisionStatus.setText("Off");
                                                        televisionLabelText1.setVisibility(View.GONE);
                                                        //updateDataUsage("Livingroom","Dev-04",75,myCounter2);
                                                        updateConfigOff("Livingroom","Dev-04");
                                                        //myCounter2 = 0;
                                                    }
                                                };
                                                myTimer2.start();
                                            }
                                            //Toast.makeText(getContext(),"Sama",Toast.LENGTH_SHORT).show();
                                            // Get the timer
                                            // Get Sub condition
                                        }else{
                                            //Toast.makeText(getContext(),"Sensor OFF",Toast.LENGTH_SHORT).show();
                                        }
                                        //Toast.makeText(getContext(),mySensor.getSensorValue(),Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    myTimer2.cancel();
                    updateDataUsage("Livingroom","Dev-04",75,myCounter2);
                    updateConfigOff("Livingroom","Dev-04");
                    //Toast.makeText(getContext(),"Counter : "+myCounter,Toast.LENGTH_SHORT).show();
                    //updateDataUsage("Livingroom","Dev-03",20,myCounter);
                    //updateConfigOff("Livingroom","Dev-03");
                    // Tidak dilakukan config
                    televisionStatus.setText("Off");
                    televisionLabelText1.setVisibility(View.GONE);
                    myCounter2 = 0;
                    //Toast.makeText(getContext(),"Tidak ada Konfig",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Fix
        myDb3 = FirebaseDatabase.getInstance();
        dbRef3 = myDb3.getReference("SeThings-Config/Livingroom/Dev-05");
        dbRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myTimer3.cancel();
                final ConfigData myConfig = dataSnapshot.getValue(ConfigData.class);
                if(!myConfig.getDeviceCondition().equals("#")){
                    // Dilakukan Config
                    myDbCondition = FirebaseDatabase.getInstance();
                    dbConditionRef = myDbCondition.getReference("SeThings-Detail_Config/Livingroom/Dev-05");
                    dbConditionRef.addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Cek koneksi sensor
                            myTimer3.cancel();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            final String currentDateandTime = sdf.format(new Date());
                            final ConfigDetailData myDetailConfig = dataSnapshot.getValue(ConfigDetailData.class);
                            if(myConfig.getGetDeviceConditionConnected().equals("#")){
                                if(myDetailConfig.getDevCondition().equals("TIMER")){
                                    Toast.makeText(getContext(),myDetailConfig.getDevCondition(),Toast.LENGTH_SHORT).show();
                                    // Jalankan fungsi timer
                                    myTimer3 = new CountDownTimer(Integer.parseInt(myDetailConfig.getDevConditionTimerDuration())*1000,1000){
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            //long millis = millisUntilFinished % 1000;
                                            long second = (millisUntilFinished / 1000) % 60;
                                            long minute = (millisUntilFinished / (1000 * 60)) % 60;
                                            long hour = (millisUntilFinished / (1000 * 60 * 60)) % 24;
                                            String displayTime = "  "+hour+" Hour "+minute+" Min "+second+" Sec";
                                            acStatus.setText("On");
                                            acLabelText1.setVisibility(View.VISIBLE);
                                            acLabelText1.setText(displayTime);
                                            myCounter3+=1;
                                        }
                                        @Override
                                        public void onFinish() {
                                            televisionLabelText1.setText("Off");
                                            televisionLabelText1.setVisibility(View.GONE);
                                            //updateDataUsage("Livingroom","Dev-04",75,myCounter2);
                                            updateConfigOff("Livingroom","Dev-05");
                                            //myCounter2 = 0;
                                        }
                                    }.start();
                                    mTimer = true;
                                }else if(myDetailConfig.getDevCondition().equals("SCHEDULED")){
                                    //String time = "23:21";
                                    String displayTime = "Set at "+myDetailConfig.getDevConditionStartScheduled()+" to "+myDetailConfig.getDevConditionEndScheduled();
                                    acLabelText1.setVisibility(View.VISIBLE);
                                    acLabelText1.setText(displayTime);

                                    String timeStart = myDetailConfig.getDevConditionStartScheduled();
                                    String [] seperatedStart = timeStart.split(":");
                                    int hour_x = Integer.parseInt(seperatedStart[0]);
                                    int minutes_x = Integer.parseInt(seperatedStart[1]);
                                    Calendar calendarStart = Calendar.getInstance();
                                    calendarStart.set(
                                            calendarStart.get(Calendar.YEAR),
                                            calendarStart.get(Calendar.MONTH),
                                            calendarStart.get(Calendar.DAY_OF_MONTH),
                                            hour_x,
                                            minutes_x,
                                            0
                                    );
                                    String timeEnd = myDetailConfig.getDevConditionEndScheduled();
                                    String [] seperatedEnd = timeEnd.split(":");
                                    int hour_y = Integer.parseInt(seperatedEnd[0]);
                                    int minutes_y = Integer.parseInt(seperatedEnd[1]);
                                    Calendar calendarEnd = Calendar.getInstance();
                                    calendarEnd.set(
                                            calendarStart.get(Calendar.YEAR),
                                            calendarStart.get(Calendar.MONTH),
                                            calendarStart.get(Calendar.DAY_OF_MONTH),
                                            hour_y,
                                            minutes_y,
                                            0
                                    );

                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    String printCalendar = sdf2.format(calendarStart.getTime());
                                    String printCalendar2 = sdf2.format(calendarEnd.getTime());
                                    Toast.makeText(getContext(),printCalendar,Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getContext(),printCalendar2,Toast.LENGTH_SHORT).show();
                                    setEventStart3(calendarStart);
                                    try {
                                        setEventEnd3(calendarEnd,myDetailConfig.getDevConditionStartScheduled(),myDetailConfig.getDevConditionEndScheduled());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }else if(myDetailConfig.getDevCondition().equals("SWITCH_OFF")){
                                    // Update Data
                                    mTimer = false;
                                    updateDataUsage("Livingroom","Dev-03",20,myCounter);
                                    //updateConfigOff("Livingroom","Dev-03");
                                    myCounter = 0;
                                }else if(myDetailConfig.getDevCondition().equals("SWITCH_ON")){
                                    Toast.makeText(getContext(),"Switch on",Toast.LENGTH_SHORT).show();
                                    myTimer3 = new CountDownTimer(43200*1000,1000){
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            //long millis = millisUntilFinished % 1000;
                                            long second = (myCounter3) % 60;
                                            long minute = ((myCounter3) / (1 * 60)) % 60;
                                            long hour = ((myCounter3) / (1 * 60 * 60)) % 24;
                                            //String displayTime = "  "+hour+" Hour "+minute+" Min "+second+" Sec";
                                            String displayTime = "H : "+hour +" M : "+minute+" S : "+second;
                                            acStatus.setText("On");
                                            acLabelText1.setVisibility(View.VISIBLE);
                                            acLabelText1.setText(displayTime);
                                            myCounter3+=1;
                                        }
                                        @Override
                                        public void onFinish() {
                                            acStatus.setText("Off");
                                            acLabelText1.setVisibility(View.GONE);
                                            //updateDataUsage("Livingroom","Dev-04",75,myCounter2);
                                            updateConfigOff("Livingroom","Dev-05");
                                            //myCounter2 = 0;
                                        }
                                    }.start();
                                }
                                // Buat ambil kondisi TIMER dan SCHEDULED
                            }else{
                                // ada sensor
                                //Toast.makeText(getContext(),myDetailConfig.getDevCondition(),Toast.LENGTH_LONG).show();
                                // Buat kondisi sensor -> ambil ke database sensor
                                FirebaseDatabase connectDB = FirebaseDatabase.getInstance();
                                DatabaseReference dbConnectRef = connectDB.getReference("SeThings-Sensors/Livingroom/");
                                dbConnectRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        ConnectedDevice mySensor = dataSnapshot.child(myConfig.getGetDeviceConditionConnected()).getValue(ConnectedDevice.class);
                                        //Toast.makeText(getContext(),myConfig.getDeviceCondition(),Toast.LENGTH_LONG).show();
                                        if(myDetailConfig.getDevCondition().equals(mySensor.getSensorValue())){
                                            if(myDetailConfig.getDevSubCondition().equals("TIMER")){
                                                //Toast.makeText(getContext(),"Sama",Toast.LENGTH_SHORT).show();
                                                myTimer3 = new CountDownTimer(Integer.parseInt(myDetailConfig.getDevSubConditioTimerDuration())*1000,1000){
                                                    @Override
                                                    public void onTick(long millisUntilFinished) {
                                                        //long millis = millisUntilFinished % 1000;
                                                        long second = (millisUntilFinished / 1000) % 60;
                                                        long minute = (millisUntilFinished / (1000 * 60)) % 60;
                                                        long hour = (millisUntilFinished / (1000 * 60 * 60)) % 24;
                                                        String displayTime = "  "+hour+" Hour "+minute+" Min "+second+" Sec";
                                                        acStatus.setText("On");
                                                        acLabelText1.setVisibility(View.VISIBLE);
                                                        acLabelText1.setText(displayTime);
                                                        myCounter3+=1;
                                                    }
                                                    @Override
                                                    public void onFinish() {
                                                        acStatus.setText("Off");
                                                        acLabelText1.setVisibility(View.GONE);
                                                        //updateDataUsage("Livingroom","Dev-04",75,myCounter2);
                                                        updateConfigOff("Livingroom","Dev-05");
                                                        //myCounter2 = 0;
                                                    }
                                                };
                                                myTimer3.start();
                                            }
                                            //Toast.makeText(getContext(),"Sama",Toast.LENGTH_SHORT).show();
                                            // Get the timer
                                            // Get Sub condition
                                        }else{
                                            //Toast.makeText(getContext(),"Sensor OFF",Toast.LENGTH_SHORT).show();
                                        }
                                        //Toast.makeText(getContext(),mySensor.getSensorValue(),Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    myTimer3.cancel();
                    updateDataUsage("Livingroom","Dev-05",450,myCounter3);
                    updateConfigOff("Livingroom","Dev-05");
                    //Toast.makeText(getContext(),"Counter : "+myCounter,Toast.LENGTH_SHORT).show();
                    //updateDataUsage("Livingroom","Dev-03",20,myCounter);
                    //updateConfigOff("Livingroom","Dev-03");
                    // Tidak dilakukan config
                    acStatus.setText("Off");
                    acLabelText1.setVisibility(View.GONE);
                    myCounter3 = 0;
                    //Toast.makeText(getContext(),"Tidak ada Konfig",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Fix
        myDb4 = FirebaseDatabase.getInstance();
        dbRef4 = myDb4.getReference("SeThings-Config/Livingroom/Dev-06");
        dbRef4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myTimer4.cancel();
                final ConfigData myConfig = dataSnapshot.getValue(ConfigData.class);
                if(!myConfig.getDeviceCondition().equals("#")){
                    // Dilakukan Config
                    myDbCondition = FirebaseDatabase.getInstance();
                    dbConditionRef = myDbCondition.getReference("SeThings-Detail_Config/Livingroom/Dev-06");
                    dbConditionRef.addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            myTimer4.cancel();
                            // Cek koneksi sensor
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            final String currentDateandTime = sdf.format(new Date());
                            final ConfigDetailData myDetailConfig = dataSnapshot.getValue(ConfigDetailData.class);
                            if(myConfig.getGetDeviceConditionConnected().equals("#")){
                                if(myDetailConfig.getDevCondition().equals("TIMER")){
                                    Toast.makeText(getContext(),myDetailConfig.getDevCondition(),Toast.LENGTH_SHORT).show();
                                    // Jalankan fungsi timer
                                    myTimer4 = new CountDownTimer(Integer.parseInt(myDetailConfig.getDevConditionTimerDuration())*1000,1000){
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            //long millis = millisUntilFinished % 1000;
                                            long second = (millisUntilFinished / 1000) % 60;
                                            long minute = (millisUntilFinished / (1000 * 60)) % 60;
                                            long hour = (millisUntilFinished / (1000 * 60 * 60)) % 24;
                                            String displayTime = "  "+hour+" Hour "+minute+" Min "+second+" Sec";
                                            fanStatus.setText("On");
                                            fanLabelText1.setVisibility(View.VISIBLE);
                                            fanLabelText1.setText(displayTime);
                                            myCounter4+=1;
                                        }
                                        @Override
                                        public void onFinish() {
                                            fanLabelText1.setText("Off");
                                            fanLabelText1.setVisibility(View.GONE);
                                            //updateDataUsage("Livingroom","Dev-04",75,myCounter2);
                                            updateConfigOff("Livingroom","Dev-06");
                                            //myCounter2 = 0;
                                        }
                                    }.start();
                                    mTimer = true;
                                }else if(myDetailConfig.getDevCondition().equals("SCHEDULED")){
                                    //String time = "23:21";
                                    String displayTime = "Set at "+myDetailConfig.getDevConditionStartScheduled()+" to "+myDetailConfig.getDevConditionEndScheduled();
                                    fanLabelText1.setVisibility(View.VISIBLE);
                                    fanLabelText1.setText(displayTime);

                                    String timeStart = myDetailConfig.getDevConditionStartScheduled();
                                    String [] seperatedStart = timeStart.split(":");
                                    int hour_x = Integer.parseInt(seperatedStart[0]);
                                    int minutes_x = Integer.parseInt(seperatedStart[1]);
                                    Calendar calendarStart = Calendar.getInstance();
                                    calendarStart.set(
                                            calendarStart.get(Calendar.YEAR),
                                            calendarStart.get(Calendar.MONTH),
                                            calendarStart.get(Calendar.DAY_OF_MONTH),
                                            hour_x,
                                            minutes_x,
                                            0
                                    );
                                    String timeEnd = myDetailConfig.getDevConditionEndScheduled();
                                    String [] seperatedEnd = timeEnd.split(":");
                                    int hour_y = Integer.parseInt(seperatedEnd[0]);
                                    int minutes_y = Integer.parseInt(seperatedEnd[1]);
                                    Calendar calendarEnd = Calendar.getInstance();
                                    calendarEnd.set(
                                            calendarStart.get(Calendar.YEAR),
                                            calendarStart.get(Calendar.MONTH),
                                            calendarStart.get(Calendar.DAY_OF_MONTH),
                                            hour_y,
                                            minutes_y,
                                            0
                                    );

                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    String printCalendar = sdf2.format(calendarStart.getTime());
                                    String printCalendar2 = sdf2.format(calendarEnd.getTime());
                                    Toast.makeText(getContext(),printCalendar,Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getContext(),printCalendar2,Toast.LENGTH_SHORT).show();
                                    setEventStart4(calendarStart);
                                    try {
                                        setEventEnd4(calendarEnd,myDetailConfig.getDevConditionStartScheduled(),myDetailConfig.getDevConditionEndScheduled());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }else if(myDetailConfig.getDevCondition().equals("SWITCH_OFF")){
                                    // Update Data
                                    mTimer = false;
                                    updateDataUsage("Livingroom","Dev-03",20,myCounter);
                                    //updateConfigOff("Livingroom","Dev-03");
                                    myCounter = 0;
                                }else if(myDetailConfig.getDevCondition().equals("SWITCH_ON")){
                                    Toast.makeText(getContext(),"Switch on",Toast.LENGTH_SHORT).show();
                                    myTimer4 = new CountDownTimer(43200*1000,1000){
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            //long millis = millisUntilFinished % 1000;
                                            long second = (myCounter4) % 60;
                                            long minute = ((myCounter4) / (1 * 60)) % 60;
                                            long hour = ((myCounter4) / (1 * 60 * 60)) % 24;
                                            //String displayTime = "  "+hour+" Hour "+minute+" Min "+second+" Sec";
                                            String displayTime = "H : "+hour +" M : "+minute+" S : "+second;
                                            fanStatus.setText("On");
                                            fanLabelText1.setVisibility(View.VISIBLE);
                                            fanLabelText1.setText(displayTime);
                                            myCounter4+=1;
                                        }
                                        @Override
                                        public void onFinish() {
                                            fanStatus.setText("Off");
                                            fanLabelText1.setVisibility(View.GONE);
                                            //updateDataUsage("Livingroom","Dev-04",75,myCounter2);
                                            updateConfigOff("Livingroom","Dev-06");
                                            //myCounter2 = 0;
                                        }
                                    }.start();
                                }
                                // Buat ambil kondisi TIMER dan SCHEDULED
                            }else{
                                // ada sensor
                                //Toast.makeText(getContext(),myDetailConfig.getDevCondition(),Toast.LENGTH_LONG).show();
                                // Buat kondisi sensor -> ambil ke database sensor
                                FirebaseDatabase connectDB = FirebaseDatabase.getInstance();
                                DatabaseReference dbConnectRef = connectDB.getReference("SeThings-Sensors/Livingroom/");
                                dbConnectRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        ConnectedDevice mySensor = dataSnapshot.child(myConfig.getGetDeviceConditionConnected()).getValue(ConnectedDevice.class);
                                        //Toast.makeText(getContext(),myConfig.getDeviceCondition(),Toast.LENGTH_LONG).show();
                                        if(myDetailConfig.getDevCondition().equals(mySensor.getSensorValue())){
                                            if(myDetailConfig.getDevSubCondition().equals("TIMER")){
                                                //Toast.makeText(getContext(),"Sama",Toast.LENGTH_SHORT).show();
                                                myTimer4 = new CountDownTimer(Integer.parseInt(myDetailConfig.getDevSubConditioTimerDuration())*1000,1000){
                                                    @Override
                                                    public void onTick(long millisUntilFinished) {
                                                        //long millis = millisUntilFinished % 1000;
                                                        long second = (millisUntilFinished / 1000) % 60;
                                                        long minute = (millisUntilFinished / (1000 * 60)) % 60;
                                                        long hour = (millisUntilFinished / (1000 * 60 * 60)) % 24;
                                                        String displayTime = "  "+hour+" Hour "+minute+" Min "+second+" Sec";
                                                        fanStatus.setText("On");
                                                        fanLabelText1.setVisibility(View.VISIBLE);
                                                        fanLabelText1.setText(displayTime);
                                                        myCounter4+=1;
                                                    }
                                                    @Override
                                                    public void onFinish() {
                                                        fanStatus.setText("Off");
                                                        fanLabelText1.setVisibility(View.GONE);
                                                        //updateDataUsage("Livingroom","Dev-04",75,myCounter2);
                                                        updateConfigOff("Livingroom","Dev-06");
                                                        //myCounter2 = 0;
                                                    }
                                                };
                                                myTimer4.start();
                                            }
                                            //Toast.makeText(getContext(),"Sama",Toast.LENGTH_SHORT).show();
                                            // Get the timer
                                            // Get Sub condition
                                        }else{
                                            //Toast.makeText(getContext(),"Sensor OFF",Toast.LENGTH_SHORT).show();
                                        }
                                        //Toast.makeText(getContext(),mySensor.getSensorValue(),Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    myTimer4.cancel();
                    updateDataUsage("Livingroom","Dev-06",75,myCounter4);
                    updateConfigOff("Livingroom","Dev-06");
                    //Toast.makeText(getContext(),"Counter : "+myCounter,Toast.LENGTH_SHORT).show();
                    //updateDataUsage("Livingroom","Dev-03",20,myCounter);
                    //updateConfigOff("Livingroom","Dev-03");
                    // Tidak dilakukan config
                    fanStatus.setText("Off");
                    fanLabelText1.setVisibility(View.GONE);
                    myCounter4 = 0;
                    //Toast.makeText(getContext(),"Tidak ada Konfig",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void updateDataUsage(String roomName, String deviceId, final float usage, final int duration){
        // Update daily -> tanggal
        FirebaseDatabase myDbUsage = FirebaseDatabase.getInstance();
        final DatabaseReference dbUsageRef = myDbUsage.getReference("SeThings-Device_Usage/"+roomName+"/"+deviceId);
        dbUsageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DeviceUsageData myDeviceUsage = dataSnapshot.getValue(DeviceUsageData.class);
                float usage_to_kwh = (usage * duration) / 3600;
                float deviceUsage = (float) Math.floor(usage_to_kwh*100)/100;
                float total_kwh = (deviceUsage/1000)+myDeviceUsage.getTotalUsage();
                Toast.makeText(getContext(),"Duration : "+duration+" Usage : "+total_kwh,Toast.LENGTH_LONG).show();
                dbUsageRef.child("totalUsage").setValue(total_kwh);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // Update nowUsage
    }
    public void updateConfigOff(String roomName, String deviceId){
        FirebaseDatabase configDb = FirebaseDatabase.getInstance();
        final DatabaseReference configDbRef = configDb.getReference("SeThings-Detail_Config/"+roomName+"/"+deviceId);
        final ConfigDetailData myConditionData = new ConfigDetailData("#",
                            "#",
                            "#",
                            "#",
                            "#",
                            "#",
                            "#",
                            "#"
        );
        configDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                configDbRef.setValue(myConditionData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase generalConfigDb = FirebaseDatabase.getInstance();
        final DatabaseReference generalConfigDbRef = generalConfigDb.getReference("SeThings-Config/"+roomName+"/"+deviceId);
        generalConfigDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                generalConfigDbRef.child("deviceCondition").setValue("#");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Toast.makeText(getContext(),"Duration : MATI ",Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setEventStart(Calendar calendar){
        AlarmManager myAlarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        myAlarm.set(AlarmManager.RTC,calendar.getTimeInMillis(),"alarm",
                new AlarmManager.OnAlarmListener()
                {
                    @Override
                    public void onAlarm() {
                        lampStatus.setText("On");
                        //On alarm code here
                        lampLabelText1.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(),"Lamp Start",Toast.LENGTH_SHORT).show();
                    }
                },null);

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setEventEnd(Calendar calendar , String start, String end) throws ParseException {
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
        final Date date1 = sdfTime.parse(start);
        final Date date2 = sdfTime.parse(end);
        AlarmManager myAlarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        myAlarm.set(AlarmManager.RTC,calendar.getTimeInMillis(),"alarm",
                new AlarmManager.OnAlarmListener()
                {
                    @Override
                    public void onAlarm() {
                        long difference = date2.getTime() - date1.getTime();
                        lampStatus.setText("Off");
                        lampLabelText1.setVisibility(View.GONE);
                        //On alarm code here
                        updateConfigOff("Livingroom","Dev-03");
                        myCounter = (int)(difference/1000);
                        Toast.makeText(getContext(),"Lamp Done",Toast.LENGTH_SHORT).show();
                    }
                },null);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setEventStart2(Calendar calendar){
        AlarmManager myAlarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        myAlarm.set(AlarmManager.RTC,calendar.getTimeInMillis(),"alarm",
                new AlarmManager.OnAlarmListener()
                {
                    @Override
                    public void onAlarm() {
                        televisionStatus.setText("On");
                        //On alarm code here
                        televisionLabelText1.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(),"TV Start",Toast.LENGTH_SHORT).show();
                    }
                },null);

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setEventEnd2(Calendar calendar,final String start, final String end) throws ParseException {
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
        final Date date1 = sdfTime.parse(start);
        final Date date2 = sdfTime.parse(end);
        AlarmManager myAlarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        myAlarm.set(AlarmManager.RTC,calendar.getTimeInMillis(),"alarm",
                new AlarmManager.OnAlarmListener()
                {
                    @Override
                    public void onAlarm() {
                        long difference = date2.getTime() - date1.getTime();
                        //Toast.makeText(getContext(),"Duration : "+String.valueOf(difference),Toast.LENGTH_SHORT).show();
                        televisionStatus.setText("Off");
                        televisionLabelText1.setVisibility(View.GONE);
                        //On alarm code here
                        //updateDataUsage("Livingroom","Dev-04",75,(int)(difference/1000));
                        updateConfigOff("Livingroom","Dev-04");
                        myCounter2 = (int)(difference/1000);
                        Toast.makeText(getContext(),"TV Done",Toast.LENGTH_SHORT).show();
                    }
                },null);


        //updateDataUsage("Livingroom","Dev-04",75,60);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setEventStart3(Calendar calendar){
        AlarmManager myAlarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        myAlarm.set(AlarmManager.RTC,calendar.getTimeInMillis(),"alarm",
                new AlarmManager.OnAlarmListener()
                {
                    @Override
                    public void onAlarm() {
                        acStatus.setText("On");
                        //On alarm code here
                        acLabelText1.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(),"AC Start",Toast.LENGTH_SHORT).show();
                    }
                },null);

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setEventEnd3(Calendar calendar,final String start, final String end) throws ParseException {
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
        final Date date1 = sdfTime.parse(start);
        final Date date2 = sdfTime.parse(end);
        AlarmManager myAlarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        myAlarm.set(AlarmManager.RTC,calendar.getTimeInMillis(),"alarm",
                new AlarmManager.OnAlarmListener()
                {
                    @Override
                    public void onAlarm() {
                        long difference = date2.getTime() - date1.getTime();
                        //Toast.makeText(getContext(),"Duration : "+String.valueOf(difference),Toast.LENGTH_SHORT).show();
                        acStatus.setText("Off");
                        acLabelText1.setVisibility(View.GONE);
                        //On alarm code here
                        //updateDataUsage("Livingroom","Dev-04",75,(int)(difference/1000));
                        updateConfigOff("Livingroom","Dev-05");
                        myCounter3 = (int)(difference/1000);
                        Toast.makeText(getContext(),"AC Done",Toast.LENGTH_SHORT).show();
                    }
                },null);
        //updateDataUsage("Livingroom","Dev-04",75,60);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setEventStart4(Calendar calendar){
        AlarmManager myAlarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        myAlarm.set(AlarmManager.RTC,calendar.getTimeInMillis(),"alarm",
                new AlarmManager.OnAlarmListener()
                {
                    @Override
                    public void onAlarm() {
                        fanStatus.setText("On");
                        //On alarm code here
                        fanLabelText1.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(),"Fan Start",Toast.LENGTH_SHORT).show();
                    }
                },null);

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setEventEnd4(Calendar calendar,final String start, final String end) throws ParseException {
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
        final Date date1 = sdfTime.parse(start);
        final Date date2 = sdfTime.parse(end);
        AlarmManager myAlarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        myAlarm.set(AlarmManager.RTC,calendar.getTimeInMillis(),"alarm",
                new AlarmManager.OnAlarmListener()
                {
                    @Override
                    public void onAlarm() {
                        long difference = date2.getTime() - date1.getTime();
                        //Toast.makeText(getContext(),"Duration : "+String.valueOf(difference),Toast.LENGTH_SHORT).show();
                        fanStatus.setText("Off");
                        fanStatus.setVisibility(View.GONE);
                        //On alarm code here
                        //updateDataUsage("Livingroom","Dev-04",75,(int)(difference/1000));
                        updateConfigOff("Livingroom","Dev-06");
                        myCounter4 = (int)(difference/1000);
                        Toast.makeText(getContext(),"Fan Done",Toast.LENGTH_SHORT).show();
                    }
                },null);
        //updateDataUsage("Livingroom","Dev-04",75,60);
    }

    public void setDefault(){
        lampLabelText1.setText("");
        lampLabelText2.setText("");
        lampLabelText3.setText("");
        televisionLabelText1.setText("");
        televisionLabelText2.setText("");
        televisionLabelText3.setText("");
        acLabelText1.setText("");
        acLabelText2.setText("");
        acLabelText3.setText("");
        fanLabelText1.setText("");
        fanLabelText2.setText("");
        fanLabelText3.setText("");
    }
}
