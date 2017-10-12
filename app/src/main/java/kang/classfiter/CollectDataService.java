package kang.classfiter;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;

public class CollectDataService extends Service {
    private SensorManager sensorManager;
    private Helper help;
    private int intertval = 30;
    private float[] g_group = new float[intertval];
    private Jdugment expert;

    private MyBinder binder;
    private SensorEventListener listenerAcc = new SensorEventListener() {
        float[] set = new float[3];
        Message msg;
        int count = 0;
        int page = 1;
        int co = 0;

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                set = sensorEvent.values.clone();
                float g = CalculateUnit.Gravity1(set);
                g_group[count++] = g;
                page = help.getPage();
                //UI setting
                msg = new Message();
                if (page == 1) {
                    msg.what = 7;
                    msg.obj = new float[]{set[2], set[0], set[1], g};
                } else if (page == 2) {
                    msg.what = 5;
                    msg.obj = g;
                } else if (page == 3) {

                }
                help.sendMessage(msg);
                co++;
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                if (count > 0)
                                    help.sendEmptyMessage(expert.selfJd(g_group[count - 1], co));
                                else
                                    help.sendEmptyMessage(expert.selfJd(g_group[intertval - 1], co));
                            }
                        }
                ).start();

                if (count >= intertval) {





                /*msg=new Message();
                msg.what=6;
                msg.obj=System.currentTimeMillis();
                 help.sendMessage(msg);*/
                    count = 0;
                    // for(int i=0;i<intertval;i++)g_group[i]=0;

                }
            } else if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private SensorEventListener listenerGyro = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private SensorEventListener listenerMang = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    public CollectDataService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);

    }

    public void init() {
        binder = new MyBinder();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor1 = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor sensor2 = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Sensor sensor3 = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (sensor1 != null)
            sensorManager.registerListener(listenerAcc, sensor1, SensorManager.SENSOR_DELAY_NORMAL);
        if (sensor2 != null)
            sensorManager.registerListener(listenerAcc, sensor2, SensorManager.SENSOR_DELAY_NORMAL);
        if (sensor3 != null)
            sensorManager.registerListener(listenerAcc, sensor3, SensorManager.SENSOR_DELAY_NORMAL);
        expert = new Jdugment();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(listenerMang);
            sensorManager.unregisterListener(listenerGyro);
            sensorManager.unregisterListener(listenerAcc);
            help.sendEmptyMessage(14);
        }

    }

    class MyBinder extends Binder {
        public void sethelper(Helper helper) {
            help = helper;
        }
    }
}
