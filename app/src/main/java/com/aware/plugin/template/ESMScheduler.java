package com.aware.plugin.template;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import org.json.JSONException;


import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.ui.PermissionsHandler;
import com.aware.utils.Aware_Plugin;
import com.aware.utils.Scheduler;
import com.aware.ESM;

public class ESMScheduler extends Aware_Plugin {

    @Override
    public void onCreate() {
        super.onCreate();

        TAG = "GOODMORNING";
        DEBUG = Aware.getSetting(this, Aware_Preferences.DEBUG_FLAG).equals("true");

        if (DEBUG) Log.d(TAG, "Good Morning plugin running");
        Aware.setSetting(this, Aware_Preferences.STATUS_ESM, true); //we will need the ESMs
        Aware.startESM(this); //ask AWARE to start ESM , Aware_Preferences.STATUS_ESM

        scheduleMorningQuestionnaire(); //see further below

        Aware.setSetting(this, Settings.STATUS_PLUGIN_ESMSCHEDULER, true);
        Aware.startPlugin(this, "com.aware.plugin.goodmorning");
    }


    //This function gets called every 5 minutes by AWARE to make sure this plugin is still running.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        boolean permissions_ok = true;
        for (String p : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                permissions_ok = false;
                break;
            }
        }

        if (permissions_ok) {
            //Check if the user has toggled the debug messages
            DEBUG = Aware.getSetting(this, Aware_Preferences.DEBUG_FLAG).equals("true");

            //Initialize our plugin's settings
            Aware.setSetting(this, Settings.STATUS_PLUGIN_ESMSCHEDULER, true);

        } else {
            Intent permissions = new Intent(this, PermissionsHandler.class);
            permissions.putExtra(PermissionsHandler.EXTRA_REQUIRED_PERMISSIONS, REQUIRED_PERMISSIONS);
            permissions.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(permissions);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (DEBUG) Log.d(TAG, "Good Morning plugin terminating.");
        Aware.stopESM(this); //turn off ESM for our plugin Aware_Preferences.STATUS_ESM
        Aware.setSetting(this, Settings.STATUS_PLUGIN_ESMSCHEDULER, false);
        Aware.stopPlugin(this, "com.aware.plugin.goodmorning");
    }

    private void scheduleMorningQuestionnaire() {
        try {

            String esm_goodmorning = "[{'esm': {" +
                    "'esm_type': "+ ESM.TYPE_ESM_QUICK_ANSWERS+"," +
                    "'esm_title': 'Sleep quality'," +
                    "'esm_instructions': 'Slept well?'," +
                    "'esm_quick_answers': ['Yes','No']," +
                    "'esm_expiration_threshold': 0," +
                    "'esm_trigger': 'goodmorning_check'" +
                    "}}]";

            Scheduler.Schedule schedule = new Scheduler.Schedule("morning_question");
            schedule.addHour(14); //we want this schedule every day at 8
            schedule.addMinute(30);
            schedule.setActionType(Scheduler.ACTION_TYPE_BROADCAST); //we are doing a broadcast
            schedule.setActionClass(ESM.ACTION_AWARE_QUEUE_ESM); //with this action
            schedule.addActionExtra(ESM.EXTRA_ESM, esm_goodmorning); //and this extra
            Scheduler.saveSchedule(getApplicationContext(), schedule);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
