package com.example.schiver.projectdua;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Calendar;
import java.util.Date;

public class BathroomFragment extends Fragment {
    TextView motionValue;
    RadioGroup motionSensorCondition;
    TextView lampStatus,lampLabelText1,lampLabelText2,lampLabelText3;
    RadioButton radioChoice;
    FirebaseDatabase myDb;
    DatabaseReference dbRef;
    FirebaseDatabase myDbCondition;
    DatabaseReference dbConditionRef;
    CountDownTimer myTimer;
    int myCounter = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.bathroom_fragment,container,false);
        motionValue = rootView.findViewById(R.id.textView9);
        motionSensorCondition = rootView.findViewById(R.id.radio_group);
        motionSensorCondition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FirebaseDatabase motionSensorDb = FirebaseDatabase.getInstance();
                final DatabaseReference dbRefMotion = motionSensorDb.getReference("SeThings-Sensors/Bathroom/");
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
        myTimer = new CountDownTimer(0,0) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        };
        lampStatus = rootView.findViewById(R.id.textView13);
        lampLabelText1 = rootView.findViewById(R.id.textView14);
        lampLabelText2 = rootView.findViewById(R.id.textView15);
        lampLabelText3 = rootView.findViewById(R.id.textView21);

        setDefault();
        //Lamp

        return rootView;
    }
    public  String remoteMotionSensor(View view){
        int selectedChoice = motionSensorCondition.getCheckedRadioButtonId();
        radioChoice = view.findViewById(selectedChoice);
        return "SENSOR_MOTION_VAL_"+radioChoice.getText().toString().toUpperCase();
    }

    public void setDefault(){
        lampLabelText1.setText("");
        lampLabelText2.setText("");
        lampLabelText3.setText("");

    }

    @Override
    public void onResume() {
        super.onResume();
        myDb = FirebaseDatabase.getInstance();
        dbRef = myDb.getReference("SeThings-Config/Bathroom/Dev-02");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myTimer.cancel();
                final ConfigData myConfig = dataSnapshot.getValue(ConfigData.class);
                if(!myConfig.getDeviceCondition().equals("#")){
                    // Dilakukan Config
                    myDbCondition = FirebaseDatabase.getInstance();
                    dbConditionRef = myDbCondition.getReference("SeThings-Detail_Config/Bathroom/Dev-02");
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
                                            updateConfigOff("Bathroom","Dev-02");
                                            //myCounter = 0;
                                        }
                                    }.start();
                                    //mTimer = true;
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
                                    //mTimer = false;
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
                                            updateConfigOff("Bathroom","Dev-02");
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
                                DatabaseReference dbConnectRef = connectDB.getReference("SeThings-Sensors/Bathroom/");
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
                                                        updateConfigOff("Bathroom","Dev-02");
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

                    updateDataUsage("Bathroom","Dev-02",20,myCounter);
                    updateConfigOff("Bathroom","Dev-02");
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
                        updateConfigOff("Bathroom","Dev-02");
                        myCounter = (int)(difference/1000);
                        Toast.makeText(getContext(),"Lamp Done",Toast.LENGTH_SHORT).show();
                    }
                },null);

    }
}
