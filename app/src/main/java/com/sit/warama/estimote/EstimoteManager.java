package com.sit.warama.estimote;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.api.ProximityObserver;
import com.estimote.proximity_sdk.api.ProximityObserverBuilder;
import com.estimote.proximity_sdk.api.ProximityZone;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class EstimoteManager extends Activity {
    private Context context;

    // 1. Add a property to hold the Proximity Observer
    public ProximityObserver proximityObserver;
    public String appId = "unviseral-template-ejl";
    public String appToken = "afb244ac9a07e1daca2beb29e9662091";

    public double red_zone_range = 1.0;
    public double yellow_zone_range = 3.0;
    private EstimoteCloudCredentials cloudCredentials;

    public EstimoteManager()
    {
        this.appId = appId;
        this.appToken = appToken;
        this.red_zone_range = red_zone_range;
        this.yellow_zone_range = yellow_zone_range;

        this.proximityObserver = new ProximityObserverBuilder(getApplicationContext(),cloudCredentials)
            .onError(new Function1<Throwable, Unit>() {
                @Override
                public Unit invoke(Throwable throwable) {
                    Log.e("app", "proximity observer error: " + throwable);
                    return null;
                }
            }).withBalancedPowerMode().build();
    }

    public double getRed_zone_range()
    {
        return this.red_zone_range;
    }

    public void setRed_zone_range()
    {
        this.red_zone_range = red_zone_range;
    }

    public double getYellow_zone_range()
    {
        return this.yellow_zone_range;
    }

    public void setYellow_zone_range()
    {
        this.yellow_zone_range = yellow_zone_range;
    }

    public String getAppId()
    {
        return appId;
    }

    public void setAppId()
    {
        this.appId = appId;
    }

    public String getAppToken()
    {
        return appToken;
    }

    public void setAppToken()
    {
        this.appToken = appToken;
    }


    public ProximityObserver getProximityObserver() {
        return proximityObserver;
    }
}
