package com.chiemy.nbrouter;

import android.app.Application;
import com.chiemy.lib.router.NBRouter;

public class NbApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NBRouter.instance().init();
    }
}
