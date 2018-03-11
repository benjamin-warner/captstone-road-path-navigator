package com.ksucapstone.gasandgo.TestHelpers;


import com.ksucapstone.gasandgo.Models.CarModel;

public class CarHelper {

    public static CarModel MakeCar(double mpg, double tankCapacity){
        return new CarModel(0, "TestCompany", "TestMake", "TestModel", mpg, tankCapacity);
    }
}
