package com.example.schiver.projectdua;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.schiver.projectdua.Model.ConnectedDevice;
import com.example.schiver.projectdua.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        myTimer = new CountDownTimer(0,0) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        };
        setDefault();
        //Lamp
        lampStatus = rootView.findViewById(R.id.textView13);
        lampLabelText1 = rootView.findViewById(R.id.textView14);
        lampLabelText2 = rootView.findViewById(R.id.textView15);
        lampLabelText3 = rootView.findViewById(R.id.textView21);
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
}
