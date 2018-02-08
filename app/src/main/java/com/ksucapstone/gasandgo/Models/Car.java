package com.ksucapstone.gasandgo.Models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Car {
    public int id;
    public String name;
    public double mpg;
    public double tankCapacity;

    public Car() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Car(int id, String name, double mpg, double tankCapacity){
        this.id = id;
        this.name = name;
        this.mpg = mpg;
        this.tankCapacity = tankCapacity;
    }
}
