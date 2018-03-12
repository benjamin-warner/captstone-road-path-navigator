package com.ksucapstone.gasandgo.Iterators;

import com.google.android.gms.maps.model.LatLng;
import com.ksucapstone.gasandgo.Helpers.DistanceHelper;
import com.ksucapstone.gasandgo.Models.CarModel;
import com.ksucapstone.gasandgo.Models.Directions.DirectionsModel;
import com.ksucapstone.gasandgo.Models.Directions.Step;
import com.ksucapstone.gasandgo.TestHelpers.CarHelper;
import com.ksucapstone.gasandgo.TestHelpers.StepHelper;

import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class DirectionsIteratorShould {

    @Test
    public void returnFourRefillLocationsForFourLongSteps() throws Exception {
        ArrayList<Step> steps = new ArrayList<>();
        int fourMilesInMeters = (int)DistanceHelper.MilesToMeters(4);
        steps.add(StepHelper.MakeStep(fourMilesInMeters, new LatLng(0,0)));
        steps.add(StepHelper.MakeStep(fourMilesInMeters, new LatLng(1,1)));
        steps.add(StepHelper.MakeStep(fourMilesInMeters, new LatLng(2,2)));

        DirectionsModel directionsModel = new DirectionsModel();
        directionsModel.steps = steps;

        CarModel car = CarHelper.MakeCar(1, 4);

        DirectionsIterator directionsIterator = new DirectionsIterator(directionsModel);

        ArrayList<LatLng> locations = directionsIterator.FindRefillPointsForCar(car);
        assertEquals(4, locations.size());
    }

    @Test
    public void returnThreeRefillLocationsForFourLongSteps() throws Exception {
        ArrayList<Step> steps = new ArrayList<>();
        int threeMilesInMeters = (int)DistanceHelper.MilesToMeters(3);
        steps.add(StepHelper.MakeStep(threeMilesInMeters, new LatLng(0,0)));
        steps.add(StepHelper.MakeStep(threeMilesInMeters, new LatLng(1,1)));
        steps.add(StepHelper.MakeStep(threeMilesInMeters, new LatLng(2,2)));

        DirectionsModel directionsModel = new DirectionsModel();
        directionsModel.steps = steps;

        CarModel car = CarHelper.MakeCar(1, 2);

        DirectionsIterator directionsIterator = new DirectionsIterator(directionsModel);

        ArrayList<LatLng> locations = directionsIterator.FindRefillPointsForCar(car);
        assertEquals(3, locations.size());
    }

    @Test
    public void returnOneLocationForCarThatCanTotallyDoThat() throws Exception {
        ArrayList<Step> steps = new ArrayList<>();
        int fourMilesInMeters = (int)DistanceHelper.MilesToMeters(4);
        steps.add(StepHelper.MakeStep(fourMilesInMeters, new LatLng(0,0)));
        steps.add(StepHelper.MakeStep(fourMilesInMeters, new LatLng(1,1)));
        steps.add(StepHelper.MakeStep(fourMilesInMeters, new LatLng(2,2)));
        steps.add(StepHelper.MakeStep(fourMilesInMeters, new LatLng(3,3)));

        DirectionsModel directionsModel = new DirectionsModel();
        directionsModel.steps = steps;

        CarModel car = CarHelper.MakeCar(1, 16);

        DirectionsIterator directionsIterator = new DirectionsIterator(directionsModel);

        ArrayList<LatLng> locations = directionsIterator.FindRefillPointsForCar(car);
        assertEquals(1, locations.size());
    }

    @Test
    public void returnZeroRefillLocationsForCrazyEfficientCar() throws Exception {
        ArrayList<Step> steps = new ArrayList<>();
        int fiveMilesInMeters = (int)DistanceHelper.MilesToMeters(5);
        steps.add(StepHelper.MakeStep(fiveMilesInMeters, new LatLng(0,0)));
        steps.add(StepHelper.MakeStep(fiveMilesInMeters, new LatLng(1,1)));
        steps.add(StepHelper.MakeStep(fiveMilesInMeters, new LatLng(2,2)));
        steps.add(StepHelper.MakeStep(fiveMilesInMeters, new LatLng(3,3)));

        DirectionsModel directionsModel = new DirectionsModel();
        directionsModel.steps = steps;

        CarModel car = CarHelper.MakeCar(50000000, 4);

        DirectionsIterator directionsIterator = new DirectionsIterator(directionsModel);

        ArrayList<LatLng> locations = directionsIterator.FindRefillPointsForCar(car);
        assertEquals(0, locations.size());
    }
}
