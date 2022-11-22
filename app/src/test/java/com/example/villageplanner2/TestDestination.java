package com.example.villageplanner2;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.example.villageplanner2.Destination;
import com.google.android.gms.maps.model.LatLng;

public class TestDestination {
    @Test
    public void testGetLat() {
        Destination temp = new Destination ("Target", 12.213, 19.798);
        Assert.assertEquals(12.213, temp.getLat(), 0);
    }

    @Test
    public void testGetLng() {
        Destination temp = new Destination ("Target", 12.213, 19.798);
        Assert.assertEquals(19.798, temp.getLng(), 0);
    }

    @Test
    public void testGetLatLng() {
        Destination temp = new Destination ("Target", 12.213, 19.798);
        LatLng l = new LatLng(12.213, 19.798);
        Assert.assertEquals(l, temp.getLatlng());
    }

    @Test
    public void testGetStore() {
        Destination temp = new Destination ("Amazon", 100.593, 89.928);
        Assert.assertEquals("Amazon", temp.getStore());
    }

    @Test
    public void testGetWaitTime() {
        Destination temp = new Destination ("Amazon", 100.593, 89.928);
        Assert.assertEquals(0, temp.getWaitTime(), 0);
    }

    @Test
    public void testDestinationWithStore() {
        Destination temp = new Destination("Target");
        Assert.assertEquals("Target", temp.getStore());
    }

    @Test
    public void testEmptyDestination() {
        Destination temp = new Destination();
        Assert.assertNull(temp.getStore());
        Assert.assertNull(temp.getLatlng());
        Assert.assertEquals(0, temp.getWaitTime(), 0);
        Assert.assertEquals(0, temp.getLat(), 0);
        Assert.assertEquals(0, temp.getLng(), 0);
    }
}
