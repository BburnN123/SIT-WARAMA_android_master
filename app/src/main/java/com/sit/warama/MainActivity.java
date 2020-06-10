package com.sit.warama;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.api.ProximityObserver;
import com.estimote.proximity_sdk.api.ProximityObserverBuilder;
import com.estimote.proximity_sdk.api.ProximityZone;
import com.estimote.proximity_sdk.api.ProximityZoneBuilder;
import com.estimote.proximity_sdk.api.ProximityZoneContext;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sit.warama.account.Account;
import com.sit.warama.estimote.NotificationsManager;

import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    MediaPlayer player;
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private HomeFragment homeFragment;
    private InfoFragment infoFragment;

    // 1. Add a property to hold the Proximity Observer
    public static ProximityObserver proximityObserver;
    private String appId = "unviseral-template-ejl";
    private String appToken = "afb244ac9a07e1daca2beb29e9662091";

    private double red_zone_range = 1.0;
    private double yellow_zone_range = 3.0;
    public boolean in_red = false;

    Button enterInfo;

    private NotificationsManager notificationsManager;
    private Account account;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);
        homeFragment = new HomeFragment();
        setFragment(homeFragment);

        //Estimote stuff
        EstimoteCloudCredentials cloudCredentials = new EstimoteCloudCredentials(appId, appToken);

        //Create the Proximity Observer
        this.proximityObserver = new ProximityObserverBuilder(getApplicationContext(), cloudCredentials)
            .onError(new Function1<Throwable, Unit>() {
                @Override
                public Unit invoke(Throwable throwable) {
                    Log.e("app", "proximity observer error: " + throwable);
                    return null;
                }
            }).withBalancedPowerMode().build();
        setup_application_settings();
        setUpProximity();
    }


    //https://github.com/Estimote/Android-Proximity-SDK
    //Set up the different proximity
    public void setUpProximity(){
        //Set up the vibrator
        final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);  // Get instance of Vibrator from current Context

        //This is working when it is nearing.
        int dot = 200;          // Length of a Morse Code "dot" in milliseconds
        int dash = 500;         // Length of a Morse Code "dash" in milliseconds
        int short_gap = 200;    // Length of Gap Between dots/dashes
        int medium_gap = 500;   // Length of Gap Between Letters
        int long_gap = 1000;    // Length of Gap Between Words

        //Pattern for yellow zone
        final long[] pattern_yellow_zone = {
                0,  // Start immediately
                dot, short_gap, dot, short_gap, dot, medium_gap,    // S
                dash, short_gap, dash, short_gap, dash, medium_gap, // O
                dot, short_gap, dot, short_gap, dot, long_gap       // S
        };

        //Pattern for red zone
        final long[] pattern_red_zone = {
                0,  // Start immediately
                dot, short_gap, dot, short_gap, dot, short_gap
        };

        final String multi_beacon_key = "multiple--7l9/title";
        notificationsManager = new NotificationsManager(getApplicationContext());

        ProximityZone red_zone = new ProximityZoneBuilder()
                .forTag("multiple--7l9")
                .inCustomRange(red_zone_range)
                .onEnter(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext context) {
                     notificationsManager.notificationEnterNotify("red");
                        in_red = true;

                        stopPlaying();
                        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.firetruck);
                        mediaPlayer.setLooping(true);
                        int maxVolume1 = 100;
                        mediaPlayer.setVolume(maxVolume1,maxVolume1); //set volume takes two paramater
                        mediaPlayer.start();

                        //Change Main Text
                        TextView mainText = (TextView) findViewById(R.id.homeText);
                        mainText.setTextColor(Color.WHITE);
                        mainText.setText("DANGER");
                        mainText.setVisibility(View.VISIBLE);

                        //Home page image set to invisible
                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
                        imageView.setVisibility(View.INVISIBLE);
                        ImageView footprint = (ImageView) findViewById(R.id.footprint);
                        footprint.setVisibility(View.INVISIBLE);
                        //Change bg to RED
                        RelativeLayout coloredFrame = (RelativeLayout) findViewById(R.id.homeLayout);
                        coloredFrame.setBackgroundColor(Color.parseColor("#BD001A"));
                        ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);
                        imageView3.setVisibility(View.INVISIBLE);
                        ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);
                        imageView2.setVisibility(View.VISIBLE);

                        Log.e("app",  "Entering range of " + red_zone_range);
                        return null;
                    }
                })
                .onExit(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext context) {
                        notificationsManager.notificationExitNotify("red");
                        in_red = false;

                        stopPlaying();

                        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.sch_alarm);
                        mediaPlayer.setLooping(true);
                        int maxVolume1 = 100;

                        mediaPlayer.setVolume(maxVolume1,maxVolume1); //set volume takes two paramater
                        mediaPlayer.start();

                        //RED TO ORANGE
                        //Change Main Text
                        TextView mainText = (TextView) findViewById(R.id.homeText);
                        mainText.setTextColor(Color.WHITE);
                        mainText.setText("CAUTION");
                        mainText.setVisibility(View.VISIBLE);

                        //Home page image set to invisible
                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
                        imageView.setVisibility(View.INVISIBLE);
                        ImageView footprint = (ImageView) findViewById(R.id.footprint);
                        footprint.setVisibility(View.INVISIBLE);

                        //Change BG to ORANGE
                        RelativeLayout coloredFrame = (RelativeLayout) findViewById(R.id.homeLayout);
                        coloredFrame.setBackgroundColor(Color.parseColor("#FA7705"));
                        ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);
                        imageView2.setVisibility(View.INVISIBLE);
                        ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);
                        imageView3.setVisibility(View.VISIBLE);


                        Log.d("app", "Leaving range of " + red_zone_range);
                        return null;
                    }
                })
                //When one or more beacons is detected
                .onContextChange(new Function1<Set<? extends ProximityZoneContext>, Unit>() {
                    @Override
                    public Unit invoke(Set<? extends ProximityZoneContext> contexts) {
                        for (ProximityZoneContext proximityContext : contexts) {
                            String title = proximityContext.getAttachments().get(multi_beacon_key);
                            if (title != null) {
                                v.vibrate(pattern_red_zone, 0);
                                in_red = true;
                                Log.e("app", "You are approaching " + title);
                            }
                            else
                            {
                                Log.e("app", "None");
                            }
                        }
                        return null;
                    }
                })
                .build();

        //Zone for yellow
        ProximityZone yellow_zone = new ProximityZoneBuilder()
                .forTag("multiple--7l9")
                .inCustomRange(yellow_zone_range)
                .onEnter(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext context) {
                        if(!in_red)
                        {
                            notificationsManager.notificationEnterNotify("yellow");

                            stopPlaying();
                            mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.sch_alarm);
                            mediaPlayer.setLooping(true);
                            int maxVolume1 = 100;
                            mediaPlayer.setVolume(maxVolume1,maxVolume1); //set volume takes two paramater
                            mediaPlayer.start();

                             //ORANGE
                            //Change Main Text
                            TextView mainText = (TextView) findViewById(R.id.homeText);
                            mainText.setTextColor(Color.WHITE);
                            mainText.setText("CAUTION");
                            mainText.setVisibility(View.VISIBLE);

                            //Home page image set to invisible
                            ImageView imageView = (ImageView) findViewById(R.id.imageView);
                            imageView.setVisibility(View.INVISIBLE);
                            ImageView footprint = (ImageView) findViewById(R.id.footprint);
                            footprint.setVisibility(View.INVISIBLE);

                            //Change BG to ORANGE
                            RelativeLayout coloredFrame = (RelativeLayout) findViewById(R.id.homeLayout);
                            coloredFrame.setBackgroundColor(Color.parseColor("#FA7705"));
                            ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);
                            imageView2.setVisibility(View.INVISIBLE);
                            ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);
                            imageView3.setVisibility(View.VISIBLE);
                        }

                        Log.e("app",  "Entering range of " + yellow_zone_range);
                        return null;
                    }
                })
                .onExit(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext context) {
                        v.cancel();

                        stopPlaying();

                        //ORANGE TO GREEN
                        //Change BG to green
                        RelativeLayout coloredFrame = (RelativeLayout) findViewById(R.id.homeLayout);
                        coloredFrame.setBackgroundColor(Color.parseColor("#00AF50"));
                        ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);
                        imageView2.setVisibility(View.INVISIBLE);
                        ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);
                        imageView3.setVisibility(View.INVISIBLE);

                        //Change Back Main Text & make maintext invisible
                        TextView mainText = (TextView) findViewById(R.id.homeText);
                        mainText.setTextColor(Color.BLACK);
                        mainText.setText(R.string.homeText);
                        mainText.setVisibility(View.INVISIBLE);

                        //Make image come back
                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
                        imageView.setVisibility(View.VISIBLE);
                        ImageView footprint = (ImageView) findViewById(R.id.footprint);
                        footprint.setVisibility(View.VISIBLE);

                        Log.d("app", "Leaving range of "+ yellow_zone_range);
                        return null;
                    }
                })
                .onContextChange(new Function1<Set<? extends ProximityZoneContext>, Unit>() {
                    @Override
                    public Unit invoke(Set<? extends ProximityZoneContext> contexts) {
                        for (ProximityZoneContext proximityContext : contexts) {
                            String title = proximityContext.getAttachments().get(multi_beacon_key);
                            if (title != null) {
                                if(!in_red) {
                                    v.vibrate(pattern_yellow_zone, 0);
                                }

                                Log.e("app", "You are approaching " + title);
                            }
                            else
                            {
                                Log.e("app", "None");
                            }
                        }
                        return null;
                    }
                })
                .build();

        //Ensure that the zone is being observe
        this.requestLocationPermission(red_zone,yellow_zone);
    }

    protected void setFragment(Fragment fragment) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        Fragment curFrag = getSupportFragmentManager().getPrimaryNavigationFragment();
        Fragment cacheFrag = getSupportFragmentManager().findFragmentByTag(tag);
        if (curFrag != null)
            tr.hide(curFrag);

        if (cacheFrag == null) {
            System.out.println("TAGGGGG" + tag);
            tr.add(R.id.main_frame, fragment, tag);
        } else {
            tr.show(cacheFrag);
            fragment = cacheFrag;
        }
        tr.setPrimaryNavigationFragment(fragment);
        tr.commit();
    }

    public void requestLocationPermission(final ProximityZone red_zone, final ProximityZone yellow_zone)
    {
        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(this,
                        // onRequirementsFulfilled
                        new Function0<Unit>() {
                            @Override public Unit invoke() {
                                Log.d("app", "requirements fulfilled");
                                proximityObserver.startObserving(red_zone,yellow_zone);
                                return null;
                            }
                        },
                        // onRequirementsMissing
                        new Function1<List<? extends Requirement>, Unit>() {
                            @Override public Unit invoke(List<? extends Requirement> requirements) {
                                Log.e("app", "requirements missing: " + requirements);
                                return null;
                            }
                        },
                        // onError
                        new Function1<Throwable, Unit>() {
                            @Override public Unit invoke(Throwable throwable) {
                                Log.e("app", "requirements error: " + throwable);
                                return null;
                            }
                        });
    }


    public void setup_application_settings()
    {
        SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
        String name = sp1.getString("name", "No name defined");//"No name defined" is the default value.
        account = new Account(getApplicationContext());

        //Get the sound
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float set_volume = 0;

        switch(account.getHearing())
        {
            case "weak":
                set_volume = 1.0f;
                break;
            case "adequate":
                set_volume = 0.7f;
                break;
            case "strong":
                set_volume = 0.1f;
                break;
        }
        int volume = (int) (maxVolume*set_volume);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }

    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void radioReflexClicked(View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.firstWeak:
                if (checked)
                    account.setReflex("weak");
                    break;
            case R.id.firstAdequate:
                if (checked)
                    account.setReflex("adequate");
                    break;
            case R.id.firstStrong:
                if (checked)
                    account.setReflex("strong");
                    break;
        }
    }

    public void radioVisualClicked(View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.secondWeak:
                if (checked)
                    account.setVisual("weak");
                    break;
            case R.id.secondAdequate:
                if (checked)
                    account.setVisual("adequate");
                    break;
            case R.id.secondStrong:
                if (checked)
                    account.setVisual("strong");
                    break;
        }
    }

    public void radioHearingClicked(View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.thirdWeak:
                if (checked)
                    account.setHearing("weak");
                    break;
            case R.id.thirdAdequate:
                if (checked)
                    account.setHearing("adequate");
                    break;
            case R.id.thirdStrong:
                if (checked)
                    account.setHearing("strong");
                    break;
        }
    }

    public void updateInfo(View view)
    {
        EditText editName = (EditText) view.findViewById(R.id.editTextPersonName);
        EditText editNumber = (EditText) view.findViewById(R.id.editTextMobileNumber);
        EditText editClinic = (EditText) view.findViewById(R.id.editTextClinic);

        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("name", editName.getText().toString());
        Ed.putString("contact",editNumber.getText().toString());
        Ed.putString("clinic",editClinic.getText().toString());

        Ed.putString("reflex", account.getReflex());
        Ed.putString("visual", account.getVisual());
        Ed.putString("hearing", account.getHearing());
        Ed.commit();
    }


}
