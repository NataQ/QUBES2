package id.co.qualitas.qubes.helper;

import android.app.Application;
import android.os.Handler;
import android.os.SystemClock;

import java.text.SimpleDateFormat;

public class App extends Application {

    public static App appInstance;
    private SimpleDateFormat dateFormat;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private StringBuilder timer;

    public StringBuilder getTimer() {
        return timer;
    }

    public void setTimer(StringBuilder timer) {
        this.timer = timer;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appInstance = this;

        dateFormat = new SimpleDateFormat("mm:ss");
    }

    public void afficher() {
        timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

        updatedTime = timeSwapBuff + timeInMilliseconds;

        int secs = (int) (updatedTime / 1000);
        int mins = secs / 60;
        secs = secs % 60;

        timer.append("");
        timer.append(mins);
        timer.append(":");
        timer.append(String.format("%02d", secs));
       // Toast.makeText(getBaseContext(),mins).show();
           /* timerValue.setText("" + mins + ":"
                    + String.format("%02d", secs));*/
        customHandler.postDelayed((Runnable) this, 0);
      /*
        handler.postDelayed(runnable,1000);*/
    }

    public void startTimer() {
        runnable.run();
    }

    public void stopTimer() {
        handler.removeCallbacks(runnable);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {

        public void run() {

        }
    };

}