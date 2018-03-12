package com.ksucapstone.gasandgo.Helpers;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DistanceHelperShould {

    private static final double DELTA = 1e-15;

    @Test
    public void ZeroMetersShouldEqualZeroMiles() throws Exception {
        double meters = 0;
        double mileEquivalent = DistanceHelper.MetersToMiles(meters);
        assertEquals(0,mileEquivalent, DELTA);
    }

    @Test
    public void ZeroMilesShouldEqualZeroMeters() throws Exception {
        double miles = 0;
        double meterEquivalent = DistanceHelper.MilesToMeters(miles);
        assertEquals(0, meterEquivalent, DELTA);
    }

    @Test
    public void OneMileShouldConvertProperly() throws Exception{
        double miles = 1;
        double meterEquivalent = DistanceHelper.MilesToMeters(miles);
        assertEquals(1609.344, meterEquivalent, DELTA);
    }

    @Test
    public void OneMeterShouldConvertProperly() throws Exception {
        double meters = 1;
        double mileEquivalent = DistanceHelper.MetersToMiles(meters);
        assertEquals(0.000621371, mileEquivalent, DELTA);
    }

}
