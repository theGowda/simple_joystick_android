package com.gowda.sachin.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageView joystick;
    ImageView joystickBackground;
    TextView radiusValue;
    TextView angleValue;

    String ipAddr = "192.168.0.106";
    int port = 1111;

    int[] joystickBackgroundLocation = new int[2];
    int joystickBackgroundCenterX;
    int joystickBackgroundCenterY;
    int joystickCenterX;
    int joystickCenterY;

    Joystick joystickObj;
    UdpClientHandler udp1 = new UdpClientHandler(ipAddr, port);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joystick = (ImageView)findViewById(R.id.joystick);
        joystickBackground = (ImageView)findViewById(R.id.joystick_background);

        radiusValue = (TextView)findViewById(R.id.radiusValue);
        angleValue = (TextView)findViewById(R.id.angleValue);

        joystickObj = new Joystick(joystick, joystickBackground, getApplicationContext());

        udp1.start();
        joystickObj.setJoystickBackgroundPosition();
        joystickObj.setOnMoveListener(new Joystick.OnMoveListener() {
            @Override
            public void onMove(float angle, float radius) {
                //String radi = String.format("%.3f", radius);
                String msg = String.format("%.3f", radius) + "," + String.format("%.3f", angle);;
                udp1.setMessage(msg);
                Log.d("Tag", "Radius: " + radius + "    Angle: " + angle);
                radiusValue.setText(String.valueOf(radius));
                angleValue.setText(String.valueOf(angle));
            }
        });

    }

    public void onStart(){
        super.onStart();
        Log.d("Tag","Activity Start");
    }
    public void onPause(){
        super.onPause();
        Log.d("Tag","Activity Pause");
    }
    public void onResume(){
        super.onResume();
        if(!udp1.isAlive())
        {
            udp1 = new UdpClientHandler(ipAddr, port);
            udp1.setMessage("LIGHTOFF");
            udp1.start();
        }
        Log.d("Tag","Activity Resume");
    }
    public void onDestroy(){
        //udp1 = new UdpClientHandler(ipAddr, port);
        //udp1.setMessage("LIGHTOFF");
        //udp1.start();

        Log.d("Tag","Activity Destroy");
        super.onDestroy();
    }
    public void onStop(){
        udp1.threadExit();
        super.onStop();
        Log.d("Tag","Activity Stopped");
    }
}
