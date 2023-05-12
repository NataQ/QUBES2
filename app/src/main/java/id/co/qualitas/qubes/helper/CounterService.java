package id.co.qualitas.qubes.helper;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

public class CounterService  extends Service {

    private Intent intent;
    public static final String BROADCAST_ACTION = "com.javacodegeeks.android.androidtimerexample.MainActivity";

    private Handler handler = new Handler();
    private long initial_time;
    long timeInMilliseconds = 0L;

    @Override
    public void onCreate() {
        super.onCreate();
        initial_time = SystemClock.uptimeMillis();
        intent = new Intent(BROADCAST_ACTION);
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second

    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 1000); // 1 seconds
        }
    };

    private void DisplayLoggingInfo() {

        timeInMilliseconds = SystemClock.uptimeMillis() - initial_time;

        int timer = (int) timeInMilliseconds / 1000;
        intent.putExtra("time", timer);
        sendBroadcast(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(sendUpdatesToUI);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


}

