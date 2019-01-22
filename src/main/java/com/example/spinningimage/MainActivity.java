package com.example.spinningimage;

// Spin an image on Y-axis or simple rotation, with speed controllable by user through swipe gestures

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.GestureDetectorCompat;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class MainActivity extends Activity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private GestureDetectorCompat mDetector;

    // Min x and y axis swipe distance
    private static int MIN_SWIPE_DISTANCE_X = 10;
    private static int MIN_SWIPE_DISTANCE_Y = 10;

    // Max x and y axis swipe distance
    private static int MAX_SWIPE_DISTANCE_X = 1000;
    private static int MAX_SWIPE_DISTANCE_Y = 1000;

    private int delay = 20;

    ToggleButton btnCircularRotation;
    ToggleButton btnAxisRotation;

    private boolean RotationAxis = true;
    private boolean RotationCircular = false;

    private Thread newThread;
    private Runnable runnable;
    private boolean deviceIsActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mDetector = new GestureDetectorCompat(this, this);

        View.OnTouchListener listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mDetector.onTouchEvent(event);
            }
        };

        setContentView(R.layout.activity_main);

        ImageView image = (ImageView) findViewById(R.id.image);
        image.setOnTouchListener(listener);

        YoYo.with(Techniques.Tada)
                .duration(3600)
                .repeat(0)
                .playOn(findViewById(R.id.navigationText));

        btnCircularRotation = (ToggleButton) findViewById(R.id.toggleCircularRotation);
        btnCircularRotation.setBackgroundColor(Color.parseColor("#EEEEEE"));
        btnCircularRotation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    YoYo.with(Techniques.Shake)
                            .duration(2000)
                            .repeat(0)
                            .playOn(findViewById(R.id.toggleCircularRotation));
                    RotationCircular = true;
                    btnAxisRotation.setChecked(false);
                } else {
                    RotationCircular = false;
                    btnCircularRotation.setChecked(false);
                }

            }
        });

        btnAxisRotation = (ToggleButton) findViewById(R.id.toggleAxisRotation);
        btnAxisRotation.setBackgroundColor(Color.parseColor("#EEEEEE"));
        btnAxisRotation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    YoYo.with(Techniques.Shake)
                            .duration(2000)
                            .repeat(0)
                            .playOn(findViewById(R.id.toggleAxisRotation));
                    RotationAxis = true;
                    btnCircularRotation.setChecked(false);
                } else {
                    RotationAxis = false;
                    btnAxisRotation.setChecked(false);
                }

            }
        });


        btnAxisRotation.setChecked(true);       // Initial rotation type

        updateSpeedText();


        deviceIsActive = true;  // Spin the graphic at start

        runnable = new Runnable() {
            @Override
            public void run() {
                int angle = 0;

                ImageView image = (ImageView) findViewById(R.id.image);

                while (angle <= 360) {
                    // Only rotate when the screen is active
                    if (deviceIsActive == true) {
                        if ((RotationCircular == true) && (RotationAxis == false)) {
                            image.setRotation(angle); // Circular rotation
                        } else if ((RotationAxis == true) && (RotationCircular == false)) {
                            image.setRotationY(angle); // Spinning on horizontal axis
                        }

                        // Inc the angle of rotation each time
                        angle = angle + 1;
                        if (angle >= 360) {
                            angle = 0;
                        }
                    }

                    // Sleep the thread for the length of the delay
                    if (delay > 0) {
                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        };

        newThread = new Thread(runnable);
        newThread.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        deviceIsActive = false; // Don't spin the graphic when the device is not active
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        deviceIsActive = true; // Spin the graphic now
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        if (v1 > 0) {
            // Scroll up
        } else {
            // Scroll down
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        // Get swipe delta value in x axis
        float deltaX = e1.getX() - e2.getX();

        // Get swipe delta value in y axis
        float deltaY = e1.getY() - e2.getY();

        // Get absolute value
        float deltaXAbs = Math.abs(deltaX);
        float deltaYAbs = Math.abs(deltaY);
        if ((deltaYAbs >= MIN_SWIPE_DISTANCE_Y) && (deltaYAbs <= MAX_SWIPE_DISTANCE_Y)) {

            if (deltaY > 0)  // Swipe up
            {
                if (delay > 0) {

                    if (delay >= 20) {
                        delay = delay - 10;
                    } else {
                        delay = delay - 1;
                    }
                    updateSpeedText();
                }
            } else   // Swipe down
            {
                if (delay < 10) {
                    delay = delay + 1;
                } else {
                    delay = delay + 10;
                }
                updateSpeedText();
            }
        }

        return true;
    }

    // Update the speed text onscreen with an animation effect
    private void updateSpeedText() {
        final TextView tv = (TextView) findViewById(R.id.speedText);
        tv.setText("Speed : " + delay);

        YoYo.with(Techniques.Swing)
                .duration(2000)
                .repeat(0)
                .playOn(findViewById(R.id.speedText));
    }

}

