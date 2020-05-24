package com.gowda.sachin.myapplication;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class Joystick {
    public interface OnMoveListener{
        public void onMove(float angle, float radius);
    }

    private ImageView joystickImg;
    private ImageView joystickBackground;
    private Context context;
    private int joystickBackgroundCenterX;
    private int joystickBackgroundCenterY;
    private float joystickImgWidth;
    private float joystickImgHeight;
    private int joystickInitCenterX;
    private int joystickInitCenterY;
    private float maxTravel;
    public OnMoveListener onMoveCall;


    private float getRadius(float x, float y){
        float radius;
        return (float)Math.sqrt(Math.pow((this.joystickBackgroundCenterX - (x+this.joystickImgWidth/2)),2)
                + Math.pow((this.joystickBackgroundCenterY - (y+this.joystickImgHeight/2)),2));
    }

    private float getAngle(float x, float y, boolean toDegrees){
        double angle;
        angle = Math.atan2((double)((y+this.joystickImgHeight/2) - this.joystickBackgroundCenterY),
                (double)((x+this.joystickImgWidth/2) - this.joystickBackgroundCenterX));
        if(toDegrees) return (float)Math.toDegrees(angle);
        return (float)angle;
    }
    private void setOnTouchListener(final Joystick joy){
        joy.joystickImg.setOnTouchListener(new View.OnTouchListener() {
            float dX, dY;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                float x;
                float y;
                float radT;
                double radian;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d("Tag", "View X: "+ view.getX()+ " View Y: " + view.getY());
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        // Future Scope - start a thread that gradually decreases the value of radius
                        // & angle and calls onMove for the values
                        view.animate()
                                .x(joy.getJoystickBackgroundCenterX() - joy.getJoystickImgWidth()/2)
                                .y(joy.getJoystickBackgroundCenterY() - joy.getJoystickImgHeight()/2)
                                .setDuration(50)
                                .start();
                        joy.onMoveCall.onMove(0, 0);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x = event.getRawX()+dX;
                        y = event.getRawY()+dY;
                        radT = joy.getRadius(x,y);
                        radian = (double) getAngle(event.getRawX()+dX,event.getRawY()+dY, false);
                        if(radT>joy.maxTravel) radT = joy.maxTravel;
                        view.animate()
                                .x((radT*(float)Math.cos(radian))
                                        + joy.joystickBackgroundCenterX - joy.joystickImgWidth/2)
                                .y((radT*(float)Math.sin(radian))
                                        + joy.joystickBackgroundCenterY -joy.joystickImgHeight/2)
                                .setDuration(0)
                                .start();
                        joy.onMoveCall.onMove((float)(-Math.toDegrees(radian)), (radT/joy.maxTravel));
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }
    public void setOnMoveListener(OnMoveListener onMoveCall){
        this.onMoveCall = onMoveCall;
        this.setOnTouchListener(this);
    }

    Joystick(ImageView joystickImg, ImageView joystickBackground, Context context)
    {
        this.context=context;
        this.joystickBackground=joystickBackground;
        this.joystickImg=joystickImg;
    }
    public void setJoystickX(float x){
        Log.d("Tag", "Set joystick X: " + x);
        this.joystickImg.setX(x-this.joystickImgWidth/2);
    }
    public void setJoystickY(float y){
        Log.d("Tag", "Set joystick Y: " + y);
        this.joystickImg.setY(y-this.joystickImgHeight/2);
    }
    public void setJoystickImgWidth(float x){
        this.joystickImgWidth = x;
        Log.d("Tag", "Joystick Width set: " + this.joystickImgWidth);
    }
    public void setJoystickImgHeight(float y){
        this.joystickImgHeight = y;
        Log.d("Tag", "Joystick Height set: " + this.joystickImgHeight);
    }
    public float getJoystickImgWidth(){
        return this.joystickImgWidth;
    }
    public float getJoystickImgHeight(){
        return this.joystickImgHeight;
    }
    public void setJoystickBackgroundPosition(){
        this.setJoystickBackgroundPosition(this);
    }
    private void setJoystickBackgroundCenter(int x, int y){
        this.joystickBackgroundCenterX = x;
        this.joystickBackgroundCenterY = y;
    }
    public int getJoystickBackgroundCenterX(){
        return  this.joystickBackgroundCenterX;
    }
    public int getJoystickBackgroundCenterY(){
        return  this.joystickBackgroundCenterY;
    }
    public void setMaxTravel(float r){
        this.maxTravel = r;
    }
    private void setJoystickBackgroundPosition(final Joystick joy){
        joy.joystickBackground.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                joy.setJoystickBackgroundCenter(
                        ((int)joy.joystickBackground.getX() + (joy.joystickBackground.getWidth())/2),
                        ((int)joy.joystickBackground.getY() + (joy.joystickBackground.getHeight())/2)
                        );
                joy.setJoystickImgWidth(joy.joystickImg.getWidth());
                joy.setJoystickImgHeight(joy.joystickImg.getHeight());
                joy.setMaxTravel(joy.joystickBackground.getHeight()/2);
                Log.d("Tag", "Joystick Back Center X: "+ joy.getJoystickBackgroundCenterX() +
                        "   Joystick Back Center Y: "+joy.getJoystickBackgroundCenterY());

                Log.d("Tag", "Joystick width: "+ joy.getJoystickImgWidth() +
                        "   Joystick Height Y: "+joy.getJoystickImgHeight());

                joy.setJoystickX(joy.getJoystickBackgroundCenterX());
                joy.setJoystickY(joy.getJoystickBackgroundCenterY());
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    joystickBackground.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                else{
                    joystickBackground.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

    }
}
