package com.sit.warama;
import com.sit.warama.estimote.*;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;
import java.util.Set;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class Main2Activity extends AppCompatActivity {

    // 1. Add a property to hold the Proximity Observer
    public static ProximityObserver proximityObserver;
    private String appId = "unviseral-template-ejl";
    private String appToken = "afb244ac9a07e1daca2beb29e9662091";

    private double red_zone_range = 1.0;
    private double yellow_zone_range = 3.0;

    private NotificationsManager notificationsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Nav bar stuff
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


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
                        Log.e("app",  "Entering range of " + red_zone_range);
                        return null;
                    }
                })
                .onExit(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext context) {
                        notificationsManager.notificationExitNotify("red");
                        v.vibrate(pattern_yellow_zone, 0);
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
                        notificationsManager.notificationEnterNotify("yellow");
                        Log.e("app",  "Entering range of " + yellow_zone_range);
                        return null;
                    }
                })
                .onExit(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext context) {
                        v.cancel();
                        notificationsManager.notificationExitNotify("yellow");
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
                                v.vibrate(pattern_yellow_zone, 0);
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



    public void requestLocationPermission(final ProximityZone red_zone, final ProximityZone yellow_zone)
    {
        RequirementsWizardFactory
            .createEstimoteRequirementsWizard()
            .fulfillRequirements(this,
                    // onRequirementsFulfilled
                    new Function0<Unit>() {
                        @Override public Unit invoke() {
                            Log.d("app", "requirements fulfilled");
                            proximityObserver.startObserving(red_zone);
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



}
