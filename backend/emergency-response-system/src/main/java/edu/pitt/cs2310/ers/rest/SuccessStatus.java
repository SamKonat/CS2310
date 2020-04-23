
package edu.pitt.cs2310.ers.rest;

public class SuccessStatus
{
    public static final SuccessStatus INSTANCE = new SuccessStatus();

    protected SuccessStatus() {}

    public String getStatus()
    {
        return "success";
    }
}

