

package com.aware.plugin.esmscheduler;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.json.JSONException;

//import java.util.Locale;
import java.lang.String;
import java.util.Calendar;

import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.ui.PermissionsHandler;
import com.aware.utils.Aware_Plugin;
import com.aware.utils.Scheduler;
import com.aware.ESM;

public class Plugin extends Aware_Plugin {

    @Override
    public void onCreate() {
        super.onCreate();

        TAG = "GOODMORNING";
        DEBUG = Aware.getSetting(this, Aware_Preferences.DEBUG_FLAG).equals("true");

        if (DEBUG) Log.d(TAG, "Good Morning plugin running");
        //Aware.setSetting(this, Aware_Preferences.STATUS_ESM, true); //we will need the ESMs
        //Aware.startESM(this); //ask AWARE to start ESM , Aware_Preferences.STATUS_ESM

        demographicsQuestionnaire();
        scheduleQuestionnaire(); //see further below

        Aware.setSetting(this, Settings.STATUS_PLUGIN_ESMSCHEDULER, true);
        Aware.startPlugin(this, "com.aware.plugin.esmscheduler");
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
        Aware.stopPlugin(this, "com.aware.plugin.esmscheduler");
    }

    private void scheduleQuestionnaire() {

        try {
            String esm_questions = "[{'esm': {\n" +
                "'esm_type':6,\n" +
                "'esm_title':'Please answer following question',\n" +
                "'esm_instructions':'Right now, I feel…?',\n" +
                "'esm_scale_start':0,\n" +
                "'esm_scale_min':-2,\n" +
                "'esm_scale_max':2,\n" +
                "'esm_scale_max_label':'stressed',\n" +
                "'esm_scale_min_label':'bored',\n" +
                "'esm_scale_step':1,\n" +
                "'esm_expiration_threshold':0,\n" +
                "'esm_notification_timeout':300,\n" +
                "'esm_submit':'Submit',\n" +
                "'esm_trigger':'AWARE Tester'\n" +
                "}}]";

            Scheduler.Schedule schedule = new Scheduler.Schedule("esm_questions");
            schedule.setInterval(5);
            schedule.setActionType(Scheduler.ACTION_TYPE_BROADCAST); //we are doing a broadcast
            schedule.setActionClass(ESM.ACTION_AWARE_QUEUE_ESM); //with this action
            schedule.addActionExtra(ESM.EXTRA_ESM, esm_questions); //and this extra

            Scheduler.saveSchedule(getApplicationContext(), schedule);

            //to remove
            //Scheduler.removeSchedule(getApplicationContext(), "schedule_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void demographicsQuestionnaire() {

        try {

            String demographic_questions = "[{'esm': {\n" +
                "'esm_type':2,\n" +
                "'esm_title':'Demographic questions (1/7)',\n" +
                "'esm_instructions':'What is your gender?',\n" +
                "'esm_radios':['Male','Female'],\n" +
                "'esm_submit':'Next',\n" +
                "'esm_expiration_threshold':0,\n" +
                "'esm_trigger':'AWARE Tester'\n" +
                "}}, {'esm': { " +
                "'esm_type':2,\n" +
                "'esm_title':'Demographic questions (2/7)',\n" +
                "'esm_instructions':'What is your age?',\n" +
                "'esm_radios':['18-24 Years','25-34 Years','35-44 Years','45-54 Years','55+ Years'],\n" +
                "'esm_submit':'Next',\n" +
                "'esm_expiration_threshold':0,\n" +
                "'esm_trigger':'AWARE Tester'\n" +
                "}}, {'esm': { " +
                "'esm_type':2,\n" +
                "'esm_title':'Demographic questions (3/7)',\n" +
                "'esm_instructions':'What is your ethnicity?',\n" +
                "'esm_radios':['White','Asian','Hispanic','African American','Other'],\n" +
                "'esm_submit':'Next',\n" +
                "'esm_expiration_threshold':0,\n" +
                "'esm_trigger':'AWARE Tester'\n" +
                "}}, {'esm': { " +
                "'esm_type':2,\n" +
                "'esm_title':'Demographic questions (4/7)',\n" +
                "'esm_instructions':'In what area do you live?',\n" +
                "'esm_radios':['Urban','Rural','Other'],\n" +
                "'esm_submit':'Next',\n" +
                "'esm_expiration_threshold':0,\n" +
                "'esm_trigger':'AWARE Tester'\n" +
                "}}, {'esm': { " +
                "'esm_type':2,\n" +
                "'esm_title':'Demographic questions (5/7)',\n" +
                "'esm_instructions':'What is your marital status?',\n" +
                "'esm_radios':['Single','Living with Partner','Married','Widowed','Divorced/Separated','Other'],\n" +
                "'esm_submit':'Next',\n" +
                "'esm_expiration_threshold':0,\n" +
                "'esm_trigger':'AWARE Tester'\n" +
                "}}, {'esm': { " +
                "'esm_type':2,\n" +
                "'esm_title':'Demographic Questions (6/7)',\n" +
                "'esm_instructions':'What is the highest level of education you have completed?',\n" +
                "'esm_radios':['University','Secondary school','Middle school','Primary school','Other'],\n" +
                "'esm_submit':'Next',\n" +
                "'esm_expiration_threshold':0,\n" +
                "'esm_trigger':'AWARE Tester'\n" +
                "}}, {'esm': { " +
                "'esm_type':2,\n" +
                "'esm_title':'Demographic Questions (7/7)',\n" +
                "'esm_instructions':'Which of the following best describes your employment status?',\n" +
                "'esm_radios':['Student','Employed (full-time/part-time)','Unemployed/homemaker','Retired','Other'],\n" +
                "'esm_submit':'Submit',\n" +
                "'esm_expiration_threshold':0,\n" +
                "'esm_trigger':'AWARE Tester'\n" +
                "}}]";


            Scheduler.Schedule schedule_demographic = new Scheduler.Schedule("demographic_questions");
            //schedule_demographic.setInterval(10000);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, 1);
            schedule_demographic.setTimer(cal);
            schedule_demographic.setActionType(Scheduler.ACTION_TYPE_BROADCAST); //we are doing a broadcast
            schedule_demographic.setActionClass(ESM.ACTION_AWARE_QUEUE_ESM); //with this action
            schedule_demographic.addActionExtra(ESM.EXTRA_ESM, demographic_questions); //and this extra

            Scheduler.saveSchedule(getApplicationContext(), schedule_demographic);

            //to remove
            //Scheduler.removeSchedule(getApplicationContext(), "schedule_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
