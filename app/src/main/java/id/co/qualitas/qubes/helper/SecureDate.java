package id.co.qualitas.qubes.helper;

import android.os.SystemClock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.model.User;

public class SecureDate extends BaseFragment {

    private Date mServerDate;

    private long mElapsedRealtime;

    private ArrayList<User> attend = new ArrayList<>();

    private static final SecureDate INSTANCE = new SecureDate();

    private int atzChecked = 0;
    private int atChecked = 0;

    public static SecureDate getInstance() {
        return INSTANCE;
    }

    public long getmElapsedRealtime() {
        return mElapsedRealtime;
    }

    public Date getDate() {
        Date current = mServerDate;
        if (current == null) {

            if(atzChecked == 1 && atChecked == 1){
                current = Calendar.getInstance(Locale.getDefault()).getTime();
            }
        } else {
            current.setTime(current.getTime()
                    + (SystemClock.elapsedRealtime() - mElapsedRealtime));

            mElapsedRealtime = SystemClock.elapsedRealtime();
        }
        return current;
    }

    public boolean isSyncDate() {
        return mServerDate != null;
    }

    public void initServerDate(final Date pServerDate) {
        mElapsedRealtime = SystemClock.elapsedRealtime();
        mServerDate = pServerDate;
    }
    public void initServerDate(final Date pServerDate, long elapse) {
        mElapsedRealtime = elapse;
        mServerDate = pServerDate;
    }

    public void initAttend(final ArrayList<User> attends, int atz, int at){
        attend = attends;
        atzChecked = atz;
        atChecked = at;
    }
}