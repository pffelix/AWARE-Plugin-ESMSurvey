package com.aware.plugin.esm_survey;

        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.support.v4.content.ContextCompat;
        import android.util.Log;

        import org.json.JSONException;

//import java.util.Locale;
        import java.lang.String;
        import java.util.Calendar;
        import java.util.Date;


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

        feedbackQuestionnaire(); //Position 2 (Alternative:1)
        initialQuestionnaire(); //3 (Alternative:2)
        esmQuestionnaire(); //1 (Alternative:3)

        Aware.setSetting(this, Settings.STATUS_PLUGIN_ESMSURVEY, true);
        Aware.startPlugin(this, "com.aware.plugin.esm_survey");
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
            Aware.setSetting(this, Settings.STATUS_PLUGIN_ESMSURVEY, true);

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
        Aware.setSetting(this, Settings.STATUS_PLUGIN_ESMSURVEY, false);
        Aware.stopPlugin(this, "com.aware.plugin.esm_survey");
    }


    private void initialQuestionnaire() {
        try {
//            Date dNow_demographic = new Date( ); // Instantiate a Date object
//            Calendar cal_demographic = Calendar.getInstance();
//            cal_demographic.setTime(dNow_demographic);
//            cal_demographic.add(Calendar.HOUR_OF_DAY,3); //DATE, MINUTE
//            String string_date = "test";
            String initial_questions = "[{'esm':{" +
                    "'esm_type':2," +
                    "'esm_title':'Demographic questions (1/5)'," +
                    "'esm_instructions':'What is your gender?'," +
                    "'esm_radios':['Male','Female']," +
                    "'esm_submit':'Next'," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_trigger':'demographic_gender'" +
                    "}},{'esm':{" +
                    "'esm_type':2," +
                    "'esm_title':'Demographic questions (2/5)'," +
                    "'esm_instructions':'What is your age?'," +
                    "'esm_radios':['18-24 years','25-29 years','30-34 years','35-44 years','45-54 years','55+ years']," +
                    "'esm_submit':'Next'," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_trigger':'demographic_age'" +
                    "}},{'esm':{" +
                    "'esm_type':2," +
                    "'esm_title':'Demographic questions (3/5)'," +
                    "'esm_instructions':'What is your marital status?'," +
                    "'esm_radios':['Single','Partner, not living together','Partner, living together','Married','Widowed','Divorced/Separated','Other']," +
                    "'esm_submit':'Next'," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_trigger':'demographic_marital_status'" +
                    "}},{'esm':{" +
                    "'esm_type':2," +
                    "'esm_title':'Demographic questions (4/5)'," +
                    "'esm_instructions':'What is the highest level of education you have completed?'," +
                    "'esm_radios':['Ph.D.','Graduate/Master','Undergraduate/Bachelor','Secondary school','Middle school','Main school','Primary school','Other']," +
                    "'esm_submit':'Next'," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_trigger':'demographic_education'" +
                    "}},{'esm':{" +
                    "'esm_type':2," +
                    "'esm_title':'Demographic questions (5/5)'," +
                    "'esm_instructions':'What is your occupation?'," +
                    "'esm_radios':['Student','Intern','Apprentice','Employed for wages','Self-employed','Unemployed','Homemaker','Civil servant','Military','Retired','Unable to work','Other']," +
                    "'esm_submit':'Next'," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_trigger':'demographic_occupation'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your activities in the next 7 days (1/3)'," +
                    "'esm_instructions':'What is your workload in the next 7 days?'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'much to do'," +
                    "'esm_scale_min_label':'little to do'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'workload_time_time'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your activities in the next 7 days (2/3)'," +
                    "'esm_instructions':'Are you looking forward to your activities in the next 7 days'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'workload_valence_week'" +
                    "}},{'esm':{" +
                    "'esm_type':2," +
                    "'esm_title':'Questions about your activities in the next 7 days (3/3)'," +
                    "'esm_instructions':'Which of the following best describes your activities in the next 7 days?'," +
                    "'esm_radios':['Studying/Learning','Studying/Learning + working at job','Working at job','Free time activity','Other']," +
                    "'esm_submit':'Next'," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_trigger':'workload_type_week'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your personality (1/13)'," +
                    "'esm_instructions':'I see myself as someone who is reserved (German: Ich bin eher zurückhaltend, reserviert)'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'personality_extraversion_1_r'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your personality (2/13)'," +
                    "'esm_instructions':'I see myself as someone who is generally trusting (German: Ich schenke anderen leicht Vertrauen, glaube an das Gute im Menschen)'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'personality_agreeableness_1'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your personality (3/13)'," +
                    "'esm_instructions':'I see myself as someone who tends to be lazy (German: Ich bin bequem, neige zur Faulheit)'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'personality_conscientiousness_1_r'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your personality (4/13)'," +
                    "'esm_instructions':'I see myself as someone who is relaxed, handles stress well (German: Ich bin entspannt, lasse mich durch Stress nicht aus der Ruhe bringen)'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'personality_neuroticism_1_r'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your personality (5/13)'," +
                    "'esm_instructions':'I see myself as someone who has few artistic interests (German: Ich habe nur wenig künstlerisches Interesse)'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'personality_openness_1_r'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your personality (6/13)'," +
                    "'esm_instructions':'I see myself as someone who is outgoing, sociable (German: Ich gehe aus mir heraus, bin gesellig)'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'personality_extraversion_2'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your personality (7/13)'," +
                    "'esm_instructions':'I see myself as someone who tends to find fault with others (German: Ich neige dazu, andere zu kritisieren)'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'personality_agreeableness_2_r'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your personality (8/13)'," +
                    "'esm_instructions':'I see myself as someone who does a thorough job (German: Ich erledige Aufgaben gründlich)'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'personality_conscientiousness_2'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your personality (9/13)'," +
                    "'esm_instructions':'I see myself as someone who gets nervous easily (German: Ich werde leicht nervös und unsicher)'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'personality_neuroticism_2'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your personality (10/13)'," +
                    "'esm_instructions':'I see myself as someone who has an active imagination (German: Ich habe eine aktive Vorstellungskraft, bin phantasievoll)'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'personality_openness_2'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your personality (11/13)'," +
                    "'esm_instructions':'I like to share my emotions with friends (German: Ich rede gerne mit Freunden über meine Gefühle)'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'personality_emotionsharing'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your personality (12/13)'," +
                    "'esm_instructions':'I often feel bored (German: Mir ist oft langweilig)'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'personality_boredom_frequency'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your personality (13/13)'," +
                    "'esm_instructions':'I often feel stressed (German: Ich bin oft gestresst)'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'personality_stress_frequency'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your smartphone usage (1/5)'," +
                    "'esm_instructions':'I often use my smartphone to overcome bored situations'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'smartphone_boredom_absorption'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your smartphone usage (2/5)'," +
                    "'esm_instructions':'I often use my smartphone to take my mind off from stressed situations'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'smartphone_stress_absorption'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your smartphone usage (3/5)'," +
                    "'esm_instructions':'I often use my smartphone to delay unpopular tasks'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'smartphone_procrastination_absorption'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your smartphone usage (4/5)'," +
                    "'esm_instructions':'I normally have my smartphone with me wherever I go'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'smartphone_usage_affection'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Questions about your smartphone usage (5/5)'," +
                    "'esm_instructions':'Estimate, how many minutes do you use your smartphone on an average day?'," +
                    "'esm_scale_start':140," +
                    "'esm_scale_min':0," +
                    "'esm_scale_max':400," +
                    "'esm_scale_max_label':'400 minutes'," +
                    "'esm_scale_min_label':'0 minutes'," +
                    "'esm_scale_step':20," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'smartphone_usage_time'" +
                    "}}]";


            Scheduler.Schedule schedule_initial = new Scheduler.Schedule("initial_questions");
            schedule_initial.setInterval(100000);
            schedule_initial.setActionType(Scheduler.ACTION_TYPE_BROADCAST); //we are doing a broadcast
            schedule_initial.setActionClass(ESM.ACTION_AWARE_QUEUE_ESM); //with this action
            schedule_initial.addActionExtra(ESM.EXTRA_ESM, initial_questions); //and this extra

            Scheduler.saveSchedule(getApplicationContext(), schedule_initial);

            //to remove
            //Scheduler.removeSchedule(getApplicationContext(), "schedule_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void esmQuestionnaire() {

        try {
            String esm_questions = "[{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'Please answer following question'," +
                    "'esm_instructions':'Right now, I feel …?\n" +
                    "-2: bored\n" +
                    "-1: little to do\n" +
                    " 0: balanced\n" +
                    " 1: slightly under pressure\n" +
                    " 2: stressed'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'stressed'," +
                    "'esm_scale_min_label':'bored'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':300," +
                    "'esm_submit':'Submit'," +
                    "'esm_trigger':'personality_boredom_stress_frequency_esm'" +
                    "}}]";

            Scheduler.Schedule schedule_esm = new Scheduler.Schedule("esm_questions");
            schedule_esm.setInterval(10);
            schedule_esm.setActionType(Scheduler.ACTION_TYPE_BROADCAST); //we are doing a broadcast
            schedule_esm.setActionClass(ESM.ACTION_AWARE_QUEUE_ESM); //with this action
            schedule_esm.addActionExtra(ESM.EXTRA_ESM, esm_questions); //and this extra

            Scheduler.saveSchedule(getApplicationContext(), schedule_esm);

            //to remove
            //Scheduler.removeSchedule(getApplicationContext(), "schedule_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void feedbackQuestionnaire() {

        try {
            String feedback_questions = "[{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'The survey runs now since 7 days! Time for feedback (1/13)'," +
                    "'esm_instructions':'To what extend do you agree: In the last 7 days I was often stressed'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'personality_stress_frequency_week'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'The survey runs now since 7 days! Time for feedback (2/13)'," +
                    "'esm_instructions':'To what extend do you agree: In the last 7 days I was often bored'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'personality_boredom_frequency_week'" +
                    "}},{'esm':{" +
                    "'esm_type':4," +
                    "'esm_title':'The survey runs now since 7 days! Time for feedback (3/13)'," +
                    "'esm_instructions':'How was your overall experience with the survey app?'," +
                    "'esm_likert_max':5," +
                    "'esm_likert_max_label':'great'," +
                    "'esm_likert_min_label':'bad'," +
                    "'esm_likert_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'feedback_overall_rating'" +
                    "}},{'esm':{" +
                    "'esm_type':1," +
                    "'esm_title':'The survey runs now since 7 days! Time for feedback (4/13)'," +
                    "'esm_instructions':'Did you have technical problems with the app?'," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'feedback_technical'" +
                    "}},{'esm':{" +
                    "'esm_type':1," +
                    "'esm_title':'The survey runs now since 7 days! Time for feedback (5/13)'," +
                    "'esm_instructions':'Did you have problems understanding the survey questions?'," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'feedback_question'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'The survey runs now since 7 days! Time for feedback (6/13)'," +
                    "'esm_instructions':'Was it difficult for you to identify your own boredom and stress state?'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'feedback_question_esm_identify'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'The survey runs now since 7 days! Time for feedback (7/13)'," +
                    "'esm_instructions':'Was the frequency of the periodical question about your boredom and stress state too high and tiring?'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_submit':'Next'," +
                    "'esm_trigger':'feedback_question_esm_frequency'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'The survey runs now since 7 days! Time for feedback (8/13)'," +
                    "'esm_instructions':'Was the periodical question about your boredom and stress state intuitive to answer?'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_submit':'Next'," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_trigger':'feedback_question_esm_intuitive'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'The survey runs now since 7 days! Time for feedback (9/13)'," +
                    "'esm_instructions':'Did you accurately answer the periodical questions about your boredom and stress state at all times over the 7 days?'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_submit':'Next'," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_trigger':'feedback_user_reliability'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'The survey runs now since 7 days! Time for feedback (10/13)'," +
                    "'esm_instructions':'Did you feel monitored while participating in the study?'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_submit':'Next'," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_trigger':'feedback_monitoring'" +
                    "}},{'esm':{" +
                    "'esm_type':6," +
                    "'esm_title':'The survey runs now since 7 days! Time for feedback (11/13)'," +
                    "'esm_instructions':'Do you think you showed an unusual smartphone usage behaviour because of participating in the study?'," +
                    "'esm_scale_start':0," +
                    "'esm_scale_min':-2," +
                    "'esm_scale_max':2," +
                    "'esm_scale_max_label':'strongly agree'," +
                    "'esm_scale_min_label':'strongly disagree'," +
                    "'esm_scale_step':1," +
                    "'esm_submit':'Next'," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_trigger':'feedback_reactivity'" +
                    "}},{'esm':{" +
                    "'esm_type':1," +
                    "'esm_title':'The survey runs now since 7 days! Time for feedback (12/13)'," +
                    "'esm_instructions':'If you want to give a more detailed feedback, please write it down here. I will draw the winners for the three 50 euro coupons in end of November and will inform you if you have won one. You would help me a lot to achieve more reliable results, if you continue to answer the questions about your boredom and stress state for 3 more days. If you want to leave the study, just press the test tube symbol left to the cloud in the main window of the AWARE app and select QUIT STUDY.'," +
                    "'esm_submit':'Next'," +
                    "'esm_expiration_threshold':0," +
                    "'esm_notification_timeout':0," +
                    "'esm_trigger':'feedback_extra'" +
//                    "}},{'esm':{" +
//                    "'esm_type':2," +
//                    "'esm_title':'The survey runs now since 7 days! Time for feedback (12/13)'," +
//                    "'esm_instructions':'Do you want to answer the questions about your mood for 7 more days?'," +
//                    "'esm_radios':['Yes','No']," +
//                    "'esm_submit':'Submit'," +
//                    "'esm_expiration_threshold':0," +
//                    "'esm_notification_timeout':0," +
//                    "'esm_trigger':'feedback_continue'" +
                    "}}]";



            Scheduler.Schedule schedule_feedback = new Scheduler.Schedule("feedback_questions");
            //schedule_feedback.setInterval(100000);
            Date dNow_feedback = new Date(); // Instantiate a Date object
            Calendar cal_feedback = Calendar.getInstance();
            cal_feedback.setTime(dNow_feedback);
            cal_feedback.add(Calendar.HOUR_OF_DAY,9); //DATE, MINUTE
            schedule_feedback.setTimer(cal_feedback);
            schedule_feedback.setActionType(Scheduler.ACTION_TYPE_BROADCAST); //we are doing a broadcast
            schedule_feedback.setActionClass(ESM.ACTION_AWARE_QUEUE_ESM); //with this action
            schedule_feedback.addActionExtra(ESM.EXTRA_ESM, feedback_questions); //and this extra

            Scheduler.saveSchedule(getApplicationContext(), schedule_feedback);

            //to remove
            //Scheduler.removeSchedule(getApplicationContext(), "schedule_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}





