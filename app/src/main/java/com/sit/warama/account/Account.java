package com.sit.warama.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Account {

    public String name;
    public String contact;
    public String clinic;
    public String reflex;
    public String visual;
    public String hearing;

    public Account(Context context)
    {
        SharedPreferences sp1 = context.getSharedPreferences("Login", context.MODE_PRIVATE);
        this.name = sp1.getString("name", null);
        this.contact = sp1.getString("contact", null);
        this.clinic = sp1.getString("clinic", null);
        this.reflex = sp1.getString("reflex", null);
        this.visual = sp1.getString("visual", null);
        this.hearing = sp1.getString("hearing", null);
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getContact()
    {
        return this.contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getClinic()
    {
        return this.clinic;
    }

    public void setClinic(String clinic)
    {
        this.clinic = clinic;
    }

    public String getReflex()
    {
        return this.reflex;
    }

    public void setReflex(String reflex)
    {
        this.reflex = reflex;
    }

    public String getVisual()
    {
        return this.visual;
    }

    public void setVisual(String visual)
    {
        this.visual = visual;
    }

    public String getHearing()
    {
        return this.hearing;
    }

    public void setHearing(String hearing)
    {
        this.hearing = hearing;
    }

}
