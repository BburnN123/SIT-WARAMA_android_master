//package com.sit.warama;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.constraintlayout.widget.ConstraintLayout;
//
//import kotlin.Unit;
//import kotlin.jvm.functions.Function0;
//import kotlin.jvm.functions.Function1;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//
//
//import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
//import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
//import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;
//import com.estimote.proximity_sdk.api.ProximityObserver;
//import com.estimote.proximity_sdk.api.ProximityObserverBuilder;
//import com.estimote.proximity_sdk.api.ProximityZone;
//
//import com.estimote.proximity_sdk.api.ProximityZoneBuilder;
//import com.estimote.proximity_sdk.api.ProximityZoneContext;
//
//import java.util.List;
//import android.os.Vibrator;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//public class MainActivity extends AppCompatActivity {
//
//    // 1. Add a property to hold the Proximity Observer
//    private ProximityObserver proximityObserver;
//    private String tag = "unknownfame-hotmail-com-s--h7b";
//    private String appToken = "a99f7aa19e2c4830f9ab55cad0641cfb";
//
//    private String key = "traffic-lightID";
////    private String value = "balestier1";
//
//    private double red_zone_range = 1.0;
//    private double yellow_zone_range = 6.0;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        EstimoteCloudCredentials cloudCredentials = new EstimoteCloudCredentials(tag, appToken);
//
//        //Create the Proximity Observer
//        this.proximityObserver = new ProximityObserverBuilder(getApplicationContext(), cloudCredentials)
//        .onError(new Function1<Throwable, Unit>() {
//            @Override
//            public Unit invoke(Throwable throwable) {
//                Log.e("app", "proximity observer error: " + throwable);
//                return null;
//            }
//        }).withBalancedPowerMode().build();
//
//        setUpProximity();
//
//
//
//
//    }
//
//    //Set up the different proximity
//    public void setUpProximity(){
//
//        //Set up the vibrator
//        final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);  // Get instance of Vibrator from current Context
//
//        //This is working when it is nearing.
//        int dot = 200;          // Length of a Morse Code "dot" in milliseconds
//        int dash = 500;         // Length of a Morse Code "dash" in milliseconds
//        int short_gap = 200;    // Length of Gap Between dots/dashes
//        int medium_gap = 500;   // Length of Gap Between Letters
//        int long_gap = 1000;    // Length of Gap Between Words
//
//        //Pattern for yellow zone
//        final long[] pattern_yellow_zone = {
//                0,  // Start immediately
//                dot, short_gap, dot, short_gap, dot, medium_gap,    // S
//                dash, short_gap, dash, short_gap, dash, medium_gap, // O
//                dot, short_gap, dot, short_gap, dot, long_gap       // S
//        };
//
//        //Pattern for red zone
//        final long[] pattern_red_zone = {
//                0,  // Start immediately
//                dot, short_gap, dot, short_gap, dot, short_gap
//        };
//
//
//        //Zone for red
//        ProximityZone red_zone = new ProximityZoneBuilder()
//                .forTag(tag).inCustomRange(red_zone_range) //Distance 1m
//                .onEnter(new Function1<ProximityZoneContext, Unit>() {
//                    @Override
//                    public Unit invoke(ProximityZoneContext context) {
//                        String trafficlightID = context.getAttachments().get("traffic-lightID");
//                        Log.d("app", trafficlightID + ": in range of 1.0");
//                        // Vibrate for 400 milliseconds
//                        v.vibrate(pattern_red_zone,0);
//                        ConstraintLayout currentLayout = (ConstraintLayout) findViewById(R.id.main_layout);
//                        Log.d("app", "RED ZONE RED ZONE RED ZONE");
//                        currentLayout.setBackgroundColor(Color.RED);
//                        return null;
//                    }
//                })
//                .onExit(new Function1<ProximityZoneContext, Unit>() {
//                    @Override
//                    public Unit invoke(ProximityZoneContext context) {
//                        Log.d("app", "Leaving range of 1.0");
//                        ConstraintLayout currentLayout = (ConstraintLayout) findViewById(R.id.main_layout);
//                        currentLayout.setBackgroundColor(Color.YELLOW);
//                        v.vibrate(pattern_yellow_zone,0);
//                        return null;
//                    }
//                })
//                .build();
//
//        //Zone for yellow
//        ProximityZone yellow_zone = new ProximityZoneBuilder()
//                .forTag(tag).inCustomRange(yellow_zone_range)
//                .onEnter(new Function1<ProximityZoneContext, Unit>() {
//                    @Override
//                    public Unit invoke(ProximityZoneContext context) {
//                        String deskOwner = context.getAttachments().get("desk-owner");
//                        Log.d("app", deskOwner + " : in range of 6.0");
//                        ConstraintLayout currentLayout = (ConstraintLayout) findViewById(R.id.main_layout);
//                        TextView maintext = (TextView) findViewById(R.id.mainText);
//                        currentLayout.setBackgroundColor(Color.YELLOW);
//                        maintext.setText("You are near an accident prone area. \n Please take the safe way in your journey!");
//                        v.vibrate(pattern_yellow_zone,0);
//                        return null;
//                    }
//                })
//                .onExit(new Function1<ProximityZoneContext, Unit>() {
//                    @SuppressLint("SetTextI18n")
//                    @Override
//                    public Unit invoke(ProximityZoneContext context) {
//                        Log.d("app", "Leaving range of 6.0");
//                        ConstraintLayout currentLayout = (ConstraintLayout) findViewById(R.id.main_layout);
//                        TextView maintext = (TextView) findViewById(R.id.mainText);
//                        currentLayout.setBackgroundColor(Color.WHITE);
//                        maintext.setText("Be safe on your journey! 请有安全的旅程");
//
//
//                        v.cancel();
//                        return null;
//                    }
//                })
//                .build();
//
//        //Ensure that the zone is being observe
//        requestLocationPermission(red_zone,yellow_zone);
//    }
//
//    public void requestLocationPermission(final ProximityZone red_zone, final ProximityZone yellow_zone)
//    {
//        RequirementsWizardFactory
//            .createEstimoteRequirementsWizard()
//            .fulfillRequirements(this,
//                    // onRequirementsFulfilled
//                    new Function0<Unit>() {
//                        @Override public Unit invoke() {
//                            Log.d("app", "requirements fulfilled");
//                            proximityObserver.startObserving(red_zone, yellow_zone);
//                            return null;
//                        }
//                    },
//                    // onRequirementsMissing
//                    new Function1<List<? extends Requirement>, Unit>() {
//                        @Override public Unit invoke(List<? extends Requirement> requirements) {
//                            Log.e("app", "requirements missing: " + requirements);
//                            return null;
//                        }
//                    },
//                    // onError
//                    new Function1<Throwable, Unit>() {
//                        @Override public Unit invoke(Throwable throwable) {
//                            Log.e("app", "requirements error: " + throwable);
//                            return null;
//                        }
//                    });
//    }
//}
//
