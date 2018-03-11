package com.ksucapstone.gasandgo.Models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CarModel {
    public int Id;
    public String Company;
    public String Make;
    public String Model;
    public double Mpg;
    public double TankCapacity;

    public CarModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public CarModel(int Id, String Company, String Make, String Model, double Mpg, double TankCapacity){
        this.Id = Id;
        this.Company = Company;
        this.Make = Make;
        this.Model = Model;
        this.Mpg = Mpg;
        this.TankCapacity = TankCapacity;
    }
}
