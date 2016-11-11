package com.example.jaikh.trubian_teachers;

import java.util.Map;

/**
 * Created by jaikh on 03-11-2016.
 */

public class User {
    String enrollment_number;
    String name;

    public User(Map<String, String> values)
    {
        name=values.get("name");
        enrollment_number=values.get("enrollment_number");
    }

    public String getEnrollmentnumber()
    {
        return enrollment_number;
    }

    public String getUsername()
    {
        return name;
    }

    public void setEnrollmentnumber()
    {
        this.enrollment_number = enrollment_number;
    }

    public void setUsername()
    {
        this.name = name;
    }
}
