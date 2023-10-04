package id.co.qualitas.qubes.application;

import android.app.Application;
import android.content.Context;

import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.Utils;

public class App extends Application {

    @Override
    public void onCreate() {
        SessionManagerQubes.init(this);
        Utils.init(this);
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
