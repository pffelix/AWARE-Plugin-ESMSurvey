package com.aware.plugin.esmscheduler;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.json.JSONException;

import java.lang.String;


import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.ui.PermissionsHandler;
import com.aware.utils.Aware_Plugin;
import com.aware.utils.Scheduler;
import com.aware.ESM;

public class Plugin extends Aware_Plugin {

    private static final String esm_question = "[{'esm': {\n" +
            "'esm_title':'ESM_Scale',\n" +
            "'esm_type':6,\n" +
            "'esm_instructions':'To what extend do you agree to the statement?: \n Right now, I feel...?',\n" +
            "'esm_scale_min':-2,\n" +
            "'esm_scale_max':2,\n" +
            "'esm_scale_start':0,\n" +
            "'esm_scale_step':1,\n" +
            "'esm_scale_max_label':'good',\n" +
            "'esm_scale_min_label':'bad',\n" +
            "'esm_expiration_threshold':0,\n" +
            //"'esm_notification_timeout':300,\n" +
            "'esm_trigger':'AWARE Tester'\n" +
            "'esm_submit':'Ok',\n" +

//                    "}}, {'esm': { " +
//                    "'esm_type':6,\n" +
//                    "'esm_title':'ESM Scale',\n" +
//                    "'esm_instructions':'To what extend do you agree to the statement?: \n Right now, I feel stressed?',\n" +
//                    "'esm_scale_min':0,\n" +
//                    "'esm_scale_max':10,\n" +
//                    "'esm_scale_start':0,\n" +
//                    "'esm_scale_max_label':'agree',\n" +
//                    "'esm_scale_min_label':'disagree',\n" +
//                    "'esm_scale_step':1,\n" +
//                    "'esm_submit':'OK',\n" +
//                    "'esm_expiration_threshold':0,\n" +
//                    "'esm_notification_timeout':240,\n" +
//                    "'esm_trigger':'AWARE Tester'\n" +
            "}}]";

    @Override
    public void onCreate() {
        super.onCreate();

        TAG = "Bachelor_Thesis";
        DEBUG = Aware.getSetting(this, Aware_Preferences.DEBUG_FLAG).equals("true");

        if (DEBUG) Log.d(TAG, "Bachelor Thesis plugin running");
        Aware.setSetting(this, Aware_Preferences.STATUS_ESM, true); //we will need the ESMs
        //Aware.startESM(this); //ask AWARE to start ESM , Aware_Preferences.STATUS_ESM
        Aware.startSensor(this, Aware_Preferences.STATUS_ESM);
        schedulerBachelorThesisQuestionnaire(); //see further below

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
        if (DEBUG) Log.d(TAG, "Bachelor Thesis plugin terminating.");
        Aware.stopESM(this); //turn off ESM for our plugin Aware_Preferences.STATUS_ESM
        Aware.setSetting(this, Settings.STATUS_PLUGIN_ESMSCHEDULER, false);
        Aware.stopPlugin(this, "com.aware.plugin.esmscheduler");
    }

    private void schedulerBachelorThesisQuestionnaire() {
//        String esm_instruction_1 = "a";
//        String esm_instruction_2 = "b";
//        String lang_1 = Locale.getDefault().getLanguage();
//        String lang_2 = Locale.getDefault().getDisplayLanguage();
//        if ("ger".equalsIgnoreCase(lang_1) || "deu".equalsIgnoreCase(lang_1) || "de".equalsIgnoreCase(lang_1) || "German".equalsIgnoreCase(lang_1) || "Deutsch".equalsIgnoreCase(lang_1) || "ger".equalsIgnoreCase(lang_2)|| "deu".equalsIgnoreCase(lang_2)|| "de".equalsIgnoreCase(lang_2) || "German".equalsIgnoreCase(lang_2)|| "Deutsch".equalsIgnoreCase(lang_2)) {
//            esm_instruction_1 = "'esm_instructions':'Inwieweit stimmst du folgender Aussage zu: Gerade ist mir langweilig?'";
//            esm_instruction_2 = "'esm_instructions':'Inwieweit stimmst du folgender Aussage zu: (Gerade bin ich gestresst??)'";
//        } else {
//            esm_instruction_1 = "'esm_instructions':'To what extend do you agree to the statement?: \n Right now, I feel bored?',\n";
//            esm_instruction_2 = "'esm_instructions':'To what extend do you agree to the statement?: \n Right now, I feel stressed?',\n";
//        }

        try {
//            esm_instruction_1= "'esm_instructions': 'test1',\n";
//            esm_instruction_2= "'esm_instructions': 'test2',\n";

            Scheduler.Schedule schedule = new Scheduler.Schedule("schedule_id");
//            schedule.addHour(9); //we want this schedule every day at 9, 12, 15, 18, 21
//            schedule.addHour(10);
//            schedule.addHour(11);
//            schedule.addHour(12);
//            schedule.addHour(13);
//            schedule.addHour(14);
//            schedule.addHour(15);
//            schedule.addHour(16);
//            schedule.addHour(17);
//            schedule.addHour(18);
//            schedule.addHour(19);
//            schedule.addHour(20);
//            schedule.addHour(21);
//            schedule.addHour(22);
//            schedule.addHour(23);
//            schedule.addHour(24);

//            schedule.addMinute(40);
              schedule.addHour(21);
              schedule.addMinute(30)
                    .setActionType(Scheduler.ACTION_TYPE_BROADCAST) //we are doing a broadcast
                    .setActionClass(ESM.ACTION_AWARE_QUEUE_ESM) //with this action
                    .addActionExtra(ESM.EXTRA_ESM, esm_question); //and this extra

            Scheduler.saveSchedule(getApplicationContext(), schedule);

//            Scheduler.Schedule schedule_1 = new Scheduler.Schedule("schedule_id");
//            schedule_1.addHour(16);
//            schedule_1.addMinute(20)
//                    .setActionType(Scheduler.ACTION_TYPE_BROADCAST) //we are doing a broadcast
//                    .setActionClass(ESM.ACTION_AWARE_QUEUE_ESM) //with this action
//                    .addActionExtra(ESM.EXTRA_ESM, esm_questions); //and this extra

//            Scheduler.saveSchedule(getApplicationContext(), schedule_1);
//
//            Scheduler.Schedule schedule_2 = new Scheduler.Schedule("schedule_id");
//            schedule_2.addHour(16);
//            schedule_2.addMinute(30)                    .setActionType(Scheduler.ACTION_TYPE_BROADCAST) //we are doing a broadcast
//                    .setActionClass(ESM.ACTION_AWARE_QUEUE_ESM) //with this action
//                    .addActionExtra(ESM.EXTRA_ESM, esm_questions); //and this extra
//
//            Scheduler.saveSchedule(getApplicationContext(), schedule_2);
//
//            Scheduler.Schedule schedule_3 = new Scheduler.Schedule("schedule_id");
//            schedule_3.addHour(16);
//            schedule_3.addMinute(40)
//                    .setActionType(Scheduler.ACTION_TYPE_BROADCAST) //we are doing a broadcast
//                    .setActionClass(ESM.ACTION_AWARE_QUEUE_ESM) //with this action
//                    .addActionExtra(ESM.EXTRA_ESM, esm_questions); //and this extra
//            Scheduler.saveSchedule(getApplicationContext(), schedule_3);
//
//            Scheduler.Schedule schedule_4 = new Scheduler.Schedule("schedule_id");
//            schedule_4.addHour(16);
//            schedule_4.addMinute(50)
//                    .setActionType(Scheduler.ACTION_TYPE_BROADCAST) //we are doing a broadcast
//                    .setActionClass(ESM.ACTION_AWARE_QUEUE_ESM) //with this action
//                    .addActionExtra(ESM.EXTRA_ESM, esm_questions); //and this extra
//            Scheduler.saveSchedule(getApplicationContext(), schedule_4);
//
//            Scheduler.Schedule schedule_5 = new Scheduler.Schedule("schedule_id");
//            schedule_5.addHour(17);
//            //schedule_5.addMinute(00)
//                    .setActionType(Scheduler.ACTION_TYPE_BROADCAST) //we are doing a broadcast
//                    .setActionClass(ESM.ACTION_AWARE_QUEUE_ESM) //with this action
//                    .addActionExtra(ESM.EXTRA_ESM, esm_questions); //and this extra
//            Scheduler.saveSchedule(getApplicationContext(), schedule_5);

            //to remove
            //Scheduler.removeSchedule(getApplicationContext(), "schedule_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
