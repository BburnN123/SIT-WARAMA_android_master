package com.sit.warama;

public class Proximity {
    public int range;
    public String status;
    public Proximity(int range, String status)
    {
        this.range = range;
        this.status = status;
    }

    public int getRange()
    {
        return range;
    }

    public void setRange(int range)
    {
        this.range = range;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }



}
