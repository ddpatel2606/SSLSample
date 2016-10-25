package com.sauyee333.visatest1.model.response;

/**
 * Created by sauyee on 25/10/16.
 */

public class HelloResponse {
    private String message;

    private String timestamp;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getTimestamp ()
    {
        return timestamp;
    }

    public void setTimestamp (String timestamp)
    {
        this.timestamp = timestamp;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", timestamp = "+timestamp+"]";
    }
}
