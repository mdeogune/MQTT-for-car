package com.example.hppc.driver;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.app.admin.SystemUpdatePolicy;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.UserManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hppc.driver.SoundReceiver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends Activity {


    private final List blockedKeys = new ArrayList(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP));

    SharedPreferences sharedPreferences;
    SoundReceiver mReceiver;
    EditText pass;
    TextView batt;              // battery  text box
    boolean currentFocus;       // To keep track of activity's window focus
    boolean isPaused;           // To keep track of activity's foreground/background status
    Handler collapseNotificationHandler;
    private Button lockTaskButton;
    public static String Pass="MUKESH";     // pass word to unlock kiosk mode
    private PackageManager mPackageManager;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mAdminComponentName;

    int lp=1;
    TextView tv;
    Location lastKnownLoc;

        final Handler mHandler = new Handler()
        {
            @Override
            public void handleMessage(final Message msg)
            {
                Log.e("Listening Message","lll");
                if (0 < msg.what)
                {
                Log.e("Dff","Dd");
                }
                else
                {
                    if (msg.getData() != null)
                    {
                        String s = msg.getData().getString("Text");
                        sharedPreferences = getSharedPreferences("busno", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                        editor1.putString("fuel", s);

                        editor1.apply();
                        Log.e("s",s);

                    }
                }
            }
        };






    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Context ctx = getApplicationContext();
            /** this gives us the time for the first trigger.  */
            Calendar cal = Calendar.getInstance();
            AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
            long interval = 1000*60*60*24;
            Intent serviceIntent = new Intent(ctx,LocationService.class);
                        // make sure you **don't** use *PendingIntent.getBroadcast*, it wouldn't work
            PendingIntent servicePendingIntent =
                    PendingIntent.getService(ctx,
                            1, // integer constant used to identify the service
                            serviceIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT);  // FLAG to avoid creating a second service if there's already one running
                        // there are other options like setInexactRepeating, check the docs
            am.setRepeating(
                    AlarmManager.RTC_WAKEUP,//type of alarm. This one will wake up the device when it goes off, but there are others, check the docs
                    cal.getTimeInMillis(),
                    interval,
                    servicePendingIntent
            );


            super.onCreate(savedInstanceState);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_main);


            /*      for     status      bar     lock         */
                WindowManager manager = ((WindowManager) getSystemService(Context.WINDOW_SERVICE));
                WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
                localLayoutParams.gravity = Gravity.TOP;
                localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
                localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                int resId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
                int result = 0;
                if (resId > 0) {
                    result = ctx.getResources().getDimensionPixelSize(resId);
                } else {
                    // Use Fallback size:
                    result = 60; // 60px Fallback
                }
                localLayoutParams.height = result;
                localLayoutParams.format = PixelFormat.RGB_565;
//                localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                customViewGroup view = new customViewGroup(ctx);
                manager.addView(view, localLayoutParams);

            /*   -------   */

            batt=(TextView)findViewById(R.id.battery);
            batteryLevel();


            SharedPreferences  sharedPreferences = getSharedPreferences("State", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("State","ON");
        editor.apply();





            /* k    i   o   s   k*/

//                    mDevicePolicyManager = (DevicePolicyManager)
//                            getSystemService(Context.DEVICE_POLICY_SERVICE);
                    pass=(EditText)findViewById(R.id.editText2) ;           // Setup stop lock task button
                    lockTaskButton = (Button) findViewById(R.id.button2);
                    lockTaskButton.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                        @Override
                        public void onClick(View v) {
                            if (pass.getText().toString().equals(Pass)){
                                ActivityManager am = (ActivityManager) getSystemService(
                                        Context.ACTIVITY_SERVICE);

                                if (am.getLockTaskModeState() ==
                                        ActivityManager.LOCK_TASK_MODE_LOCKED) {
                                    stopLockTask();
                                }
                            }
                        }
                    });
//                    // Set Default COSU policy
//                    mAdminComponentName = DeviceAdminReceiver.getComponentName(this);
//                    mDevicePolicyManager = (DevicePolicyManager) getSystemService(
//                            Context.DEVICE_POLICY_SERVICE);
//                    mPackageManager = getPackageManager();
//                    if(mDevicePolicyManager.isDeviceOwnerApp(getPackageName())){
//                        setDefaultCosuPolicies(true);
//                    }
//                    else {
//                        Toast.makeText(getApplicationContext(),
//                                R.string.not_device_owner,Toast.LENGTH_SHORT)
//                                .show();
//                    }
//
//
//


        BroadcastReceiver mMessageReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Get extra data included in the Intent
                String message = intent.getStringExtra("Status");
                if (message.equals("Please Enable Location")) {
                    if (message != null) {
                        tv = (TextView) findViewById(R.id.tv1);
                        tv.setText(message);
                    }
                } else {

                    Bundle b = intent.getBundleExtra("Location");
                    lastKnownLoc = b.getParcelable("Location");
                    try {
                        if (lastKnownLoc != null) {
                            if (message != null) {
                                tv = (TextView) findViewById(R.id.tv1);
                                tv.setText(message);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("err", e.toString());
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    mMessageReceiver, new IntentFilter("GPSLocationUpdates"));


            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == 1) {
                Toast.makeText(this, "Location Access already Allowed", Toast.LENGTH_SHORT).show();
                lp = 1;
                Log.e("permission","llowed");

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
        }
        catch (Exception e)
        {
            Log.e("MAIN ONCREATE Error",e.toString());
        }

    }




    @Override
    protected void onResume() {

        isPaused = false;
        try {
            super.onResume();
            SharedPreferences sharedPreferences = getSharedPreferences("State", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("State", "ON");

            Log.e("UI ", "Resumed");

            editor.commit();
        }catch (Exception e)
        {
            Log.e("MAIN ONRESUME Error",e.toString());
        }
    }

    private void batteryLevel() {
        try {
            BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    context.unregisterReceiver(this);
                    int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    int level = -1;
                    if (rawlevel >= 0 && scale > 0) {
                        level = (rawlevel * 100) / scale;
                    }
                    batt.setText("Battery Level Remaining: " + level + "%");
                }
            };
            IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            registerReceiver(batteryLevelReceiver, batteryLevelFilter);
        }
        catch (Exception e)
        {
            Log.e("MAIN ONRESUME ERROR :",e.toString());
        }
    }

    @Override
    protected void onStop() {
        try {
            super.onStop();
            SharedPreferences sharedPreferences = getSharedPreferences("State", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("State", "OFF");

            Log.e("UI ", "Stopped");
            editor.commit();
        }
        catch (Exception e)
        {
            Log.e("MAIN ONSTOP ERROR :",e.toString());
        }
    }
//    @Override
//    protected void onStart() {
//        try {
//            super.onStart();
//
//            // start lock task mode if its not already active
//            if (mDevicePolicyManager.isLockTaskPermitted(this.getPackageName())) {
//                ActivityManager am = (ActivityManager) getSystemService(
//                        Context.ACTIVITY_SERVICE);
//                if (am.getLockTaskModeState() ==
//                        ActivityManager.LOCK_TASK_MODE_NONE) {
//                    startLockTask();
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            Log.e("MAIN ONSTART ERROR :",e.toString());
//        }
//    }


    public void onClickN()
    {
        Log.e("alarm Set","");
    }



    @Override
        public void onRequestPermissionsResult ( int requestCode,
        String permissions[], int[] grantResults){

        try {
            switch (requestCode) {
                case 1: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        lp = 1;


                        Toast.makeText(this, "Location Access Allowed", Toast.LENGTH_SHORT).show();

                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.

                    } else {
                        Toast.makeText(this, "Please Allow Location Access", Toast.LENGTH_SHORT).show();

                        lp = 0;
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    return;
                }

                // other 'case' lines to check for other
                // permissions this app might request
            }
        }
        catch (Exception e)
        {
            Log.e("rrre",e.toString());
        }
    }


    public void b1(View view)
    {
        EditText et = null;
        try {
            lp=1;

            // mReceiver.start();
            Log.e("ffff", lp + "");
            if (lp == 1) {
                Log.e("", lp + "");
                et = (EditText) findViewById(R.id.et1);
                if (et.getText() != null) {
                    SharedPreferences spf = getSharedPreferences("busno", Context.MODE_PRIVATE);
                    if (!(spf.getString("busno", "10000").equals("10000"))) {
                        stopService(new Intent(this, LocationService.class));
                        Log.e("Service", "Stopped for busno. " + spf.getString("busno", "10000"));

                    }
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("busno", et.getText().toString());
                    editor.putFloat("maxspeed", 0.0f);
                    editor.commit();
                    // onClickN();
                    startService(new Intent(this, LocationService.class));
                    Log.e("Service", "Started for busno. " + spf.getString("busno", "10000"));

                    Button button = (Button) findViewById(R.id.b1);
                    TextView tv = (TextView) findViewById(R.id.tv1);
                    tv.setText("Sending Location of Bus to Server..1.......");
                }
            }
            if (lp == 0) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == 1) {
                    Log.e("", lp + "");
                    et = (EditText) findViewById(R.id.et1);
                    SharedPreferences spf = getSharedPreferences("busno", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = spf.edit();
                    if (et.getText() != null) {
                        editor.putString("busno", et.getText().toString());
                        editor.commit();
                        startService(new Intent(this, LocationService.class));
                        Button button = (Button) findViewById(R.id.b1);
                        TextView tv = (TextView) findViewById(R.id.tv1);
                        tv.setText("Sending Location of Bus to Server...2......");
                    }
                    lp = 1;

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                }
            }
        }
        catch (Exception e)
        {
            Log.e("rrre",e.toString());
        }
        
        

        

    }



//
//    private void setDefaultCosuPolicies(boolean active){
//        try {
//            // set user restrictions
//            setUserRestriction(UserManager.DISALLOW_SAFE_BOOT, active);
//            setUserRestriction(UserManager.DISALLOW_FACTORY_RESET, active);
//            setUserRestriction(UserManager.DISALLOW_ADD_USER, active);
//            setUserRestriction(UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA, active);
//            setUserRestriction(UserManager.DISALLOW_ADJUST_VOLUME, active);
//
//            // disable keyguard and status bar
//            mDevicePolicyManager.setKeyguardDisabled(mAdminComponentName, active);
//            mDevicePolicyManager.setStatusBarDisabled(mAdminComponentName, active);
//
//            // enable STAY_ON_WHILE_PLUGGED_IN
//            enableStayOnWhilePluggedIn(active);
//
//            // set system update policy
//            if (active) {
//                mDevicePolicyManager.setSystemUpdatePolicy(mAdminComponentName,
//                        SystemUpdatePolicy.createWindowedInstallPolicy(60, 120));
//            } else {
//                mDevicePolicyManager.setSystemUpdatePolicy(mAdminComponentName,
//                        null);
//            }
//
//            // set this Activity as a lock task package
//
//            mDevicePolicyManager.setLockTaskPackages(mAdminComponentName,
//                    active ? new String[]{getPackageName()} : new String[]{});
//
//            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MAIN);
//            intentFilter.addCategory(Intent.CATEGORY_HOME);
//            intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
//
//            if (active) {
//                // set Cosu activity as home intent receiver so that it is started
//                // on reboot
//                mDevicePolicyManager.addPersistentPreferredActivity(
//                        mAdminComponentName, intentFilter, new ComponentName(
//                                getPackageName(), MainActivity.class.getName()));
//            } else {
//                mDevicePolicyManager.clearPackagePersistentPreferredActivities(
//                        mAdminComponentName, getPackageName());
//            }
//        }
//        catch (Exception e)
//        {
//            Log.e("MAIN setDefaultCosuPolicies",e.toString());
//        }
//    }

//
//
//    private void setUserRestriction(String restriction, boolean disallow){
//        try {
//            if (disallow) {
//                mDevicePolicyManager.addUserRestriction(mAdminComponentName,
//                        restriction);
//            } else {
//                mDevicePolicyManager.clearUserRestriction(mAdminComponentName,
//                        restriction);
//            }
//        }
//        catch (Exception e)
//        {
//            Log.e("rrre",e.toString());
//        }
//    }
//
//   private void enableStayOnWhilePluggedIn(boolean enabled){
//        try {
//            if (enabled) {
//                mDevicePolicyManager.setGlobalSetting(
//                        mAdminComponentName,
//                        Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
//                        Integer.toString(BatteryManager.BATTERY_PLUGGED_AC
//                                | BatteryManager.BATTERY_PLUGGED_USB
//                                | BatteryManager.BATTERY_PLUGGED_WIRELESS));
//            } else {
//                mDevicePolicyManager.setGlobalSetting(
//                        mAdminComponentName,
//                        Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
//                        "0"
//                );
//            }
//        }
//        catch (Exception e)
//        {
//            Log.e("rrre",e.toString());
//        }
//    }
//

    public void b2(View view) {
        SharedPreferences spf = getSharedPreferences("busno", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putFloat("maxspeed",0.0f);
        editor.putFloat("distance",0.0f);
        editor.apply();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_HOME)) {
            Toast.makeText(this, "You pressed the home button!", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        // nothing to do here
        // … really
    }

//    Long power button press:
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        try {
            super.onWindowFocusChanged(hasFocus);
            if (!hasFocus) {
                currentFocus = hasFocus;
                collapseNow();

                // Close every kind of system dialog
                Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                sendBroadcast(closeDialog);
            }
        }catch (Exception e)
        {
            Log.e("rrre",e.toString());
        }
    }


    public void collapseNow() {
        try {

            // Initialize 'collapseNotificationHandler'
            if (collapseNotificationHandler == null) {
                collapseNotificationHandler = new Handler();
            }

            // If window focus has been lost && activity is not in a paused state
            // Its a valid check because showing of notification panel
            // steals the focus from current activity's window, but does not
            // 'pause' the activity
            if (!currentFocus && !isPaused) {

                // Post a Runnable with some delay - currently set to 300 ms
                collapseNotificationHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        // Use reflection to trigger a method from 'StatusBarManager'

                        Object statusBarService = getSystemService("statusbar");
                        Class<?> statusBarManager = null;

                        try {
                            statusBarManager = Class.forName("android.app.StatusBarManager");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                        Method collapseStatusBar = null;

                        try {

                            // Prior to API 17, the method to call is 'collapse()'
                            // API 17 onwards, the method to call is `collapsePanels()`

                            if (Build.VERSION.SDK_INT > 16) {
                                collapseStatusBar = statusBarManager.getMethod("collapsePanels");
                            } else {
                                collapseStatusBar = statusBarManager.getMethod("collapse");
                            }
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }

                        collapseStatusBar.setAccessible(true);

                        try {
                            collapseStatusBar.invoke(statusBarService);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        // Check if the window focus has been returned
                        // If it hasn't been returned, post this Runnable again
                        // Currently, the delay is 100 ms. You can change this
                        // value to suit your needs.
                        if (!currentFocus && !isPaused) {
                            collapseNotificationHandler.postDelayed(this, 100L);
                        }

                    }
                }, 300L);
            }
        }
        catch (Exception e)
        {
            Log.e("rrre",e.toString());
        }
    }



    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        try {
            if (blockedKeys.contains(event.getKeyCode())) {
                return true;
            } else {
                return super.dispatchKeyEvent(event);
            }
        }
        catch (Exception e)
        {
            Log.e("rrre",e.toString());
            return false;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();


        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
        // Activity's been paused
        isPaused = true;
    }

    public static class customViewGroup extends ViewGroup
    {


        public customViewGroup(Context context) {
            super(context);
        }

            @Override
            protected void onLayout ( boolean changed, int l, int t, int r, int b){
        }

            @Override
            public boolean onInterceptTouchEvent (MotionEvent ev){
            Log.v("customViewGroup", "**********Intercepted");
            return true;
        }

        }

    }





