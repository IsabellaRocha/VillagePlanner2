package com.example.villageplanner2;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.example.villageplanner2.Destination;
import com.google.android.gms.maps.model.LatLng;

public class DestinationTests {
    @Test
    public void testGetLat() {
        Destination temp = new Destination ("Target", 12.213, 19.798);
        assertEquals(12.213, temp.getLat(), 0);
    }

    @Test
    public void testGetLng() {
        Destination temp = new Destination ("Target", 12.213, 19.798);
        assertEquals(19.798, temp.getLng(), 0);
    }

    @Test
    public void testGetLatLng() {
        Destination temp = new Destination ("Target", 12.213, 19.798);
        LatLng l = new LatLng(12.213, 19.798);
        assertEquals(l, temp.getLatlng());
    }

    @Test
    public void testGetStore() {
        Destination temp = new Destination ("Amazon", 100.593, 89.928);
        assertEquals("Amazon", temp.getStore());
    }
}
