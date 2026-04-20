package com.shrikantelectronics.sync_app_settings;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Service that keeps running SyncAdapter in background.
 * <p/>
 * Created by Kursulla on 07/09/15.
 */
public class SyncAdapterService extends Service {
    private static       SyncAdapter syncAdapter     = null;
    // Object to use as a thread-safe lock
    private static final Object      syncAdapterLock = new Object();

    public SyncAdapterService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (syncAdapter == null) {
            syncAdapter = new SyncAdapter(getApplicationContext(),  true);
        }
    }

    /**
     * Return an object that allows the system to invoke the sync adapter.
     */
    @Override
    public IBinder onBind(Intent arg0) {
    /*
     * Get the object that allows external processes
     * to call onPerformSync(). The object is created
     * in the base class code when the SyncAdapter
     * constructors call super()
     */
        return syncAdapter.getSyncAdapterBinder();
    }
}
