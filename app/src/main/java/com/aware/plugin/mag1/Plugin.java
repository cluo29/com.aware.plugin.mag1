package com.aware.plugin.mag1;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.plugin.mag1.Provider.Mag1_Data;
import com.aware.providers.Magnetometer_Provider.Magnetometer_Data;
import com.aware.providers.Rotation_Provider.Rotation_Data;
import com.aware.utils.Aware_Plugin;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


public class Plugin extends Aware_Plugin {
    public static final String ACTION_AWARE_PLUGIN_MAG1 = "ACTION_AWARE_PLUGIN_MAG1";

    public static final String EXTRA_M_VALUES_0 ="M_VALUES_0";
    public static final String EXTRA_M_VALUES_1 ="M_VALUES_1";
    public static final String EXTRA_M_VALUES_2 ="M_VALUES_2";
    public static final String EXTRA_R_VALUES_0 ="R_VALUES_0";
    public static final String EXTRA_R_VALUES_1 ="R_VALUES_1";
    public static final String EXTRA_R_VALUES_2 ="R_VALUES_2";
    private static double m_0 = 0;
    private static double m_1 = 0;
    private static double m_2 = 0;
    private static double r_0 = 0;
    private static double r_1 = 0;
    private static double r_2 = 0;
    private static TextView Text_m_0;
    private static TextView Text_m_1;
    private static TextView Text_m_2;
    private static TextView Text_r_0;
    private static TextView Text_r_1;
    private static TextView Text_r_2;
    public static ContextProducer context_producer;


    public Thread motion_thread = new Thread(){
        public void run(){
            while(true){

                Cursor Acceleration = getApplicationContext().getContentResolver().query(Magnetometer_Data.CONTENT_URI, null, null, null, Magnetometer_Data.TIMESTAMP + " DESC LIMIT 1");
                if(Acceleration!=null && Acceleration.moveToFirst()){
                    m_0 = Acceleration.getDouble(Acceleration.getColumnIndex(Magnetometer_Data.VALUES_0));
                    m_1 = Acceleration.getDouble(Acceleration.getColumnIndex(Magnetometer_Data.VALUES_1));
                    m_2 = Acceleration.getDouble(Acceleration.getColumnIndex(Magnetometer_Data.VALUES_2));
                }
                if( Acceleration != null && ! Acceleration.isClosed() ) {
                    Acceleration.close();
                }
                Cursor gyro = getApplicationContext().getContentResolver().query(Rotation_Data.CONTENT_URI, null, null, null, Rotation_Data.TIMESTAMP + " DESC LIMIT 1");

                if(gyro!=null && gyro.moveToFirst()){

                    r_0 = gyro.getDouble(gyro.getColumnIndex(Rotation_Data.VALUES_0));
                    r_1 = gyro.getDouble(gyro.getColumnIndex(Rotation_Data.VALUES_1));
                    r_2 = gyro.getDouble(gyro.getColumnIndex(Rotation_Data.VALUES_2));
                }
                if( gyro != null && ! gyro.isClosed() ) {
                    gyro.close();
                }
                ContentValues data = new ContentValues();
                //require Provider.java here
                data.put(Mag1_Data.TIMESTAMP, System.currentTimeMillis());
                data.put(Mag1_Data.DEVICE_ID, Aware.getSetting(getApplicationContext(), Aware_Preferences.DEVICE_ID));
                data.put(Mag1_Data.M_VALUES_0,m_0);
                data.put(Mag1_Data.M_VALUES_1,m_1);
                data.put(Mag1_Data.M_VALUES_2,m_2);
                data.put(Mag1_Data.R_VALUES_0,r_0);
                data.put(Mag1_Data.R_VALUES_1,r_1);
                data.put(Mag1_Data.R_VALUES_2,r_2);

                getContentResolver().insert(Mag1_Data.CONTENT_URI, data);
                //Share context
                context_producer.onContext();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };




    @Override
    public void onCreate() {
        super.onCreate();
        TAG = "AWARE::Motion Meter";
        DEBUG = true;

        Intent aware = new Intent(this, Aware.class);
        startService(aware);
        CONTEXT_PRODUCER = new ContextProducer() {
            @Override
            public void onContext() {
                Intent context_motion_meter = new Intent();
                context_motion_meter.setAction(ACTION_AWARE_PLUGIN_MAG1);
                //take them out of AWARE providers
                context_motion_meter.putExtra(EXTRA_M_VALUES_0, m_0);
                context_motion_meter.putExtra(EXTRA_M_VALUES_1, m_1);
                context_motion_meter.putExtra(EXTRA_M_VALUES_2, m_2);
                context_motion_meter.putExtra(EXTRA_R_VALUES_0, r_0);
                context_motion_meter.putExtra(EXTRA_R_VALUES_1, r_1);
                context_motion_meter.putExtra(EXTRA_R_VALUES_2, r_2);
                sendBroadcast(context_motion_meter);
            }
        };
        context_producer = CONTEXT_PRODUCER;
        motion_thread.start();
    }
    public void onDestroy() {
        super.onDestroy();
    }
}