package com.example.villageplanner2;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestUserLatLng {
    @Test
    public void testGetLatitude() {
        UserLatLng temp = new UserLatLng(100.987, 9.879);
        assertEquals(100.987, temp.getLatitude(), 0);
    }

    @Test
    public void testGetLongitude() {
        UserLatLng temp = new UserLatLng(100.987, 9.879);
        assertEquals(9.879, temp.getLongitude(), 0);
    }

    @Test
    public void testEmptyUserLatLng() {
        UserLatLng temp = new UserLatLng();
        assertNull(temp.getLongitude());
        assertNull(temp.getLatitude());
    }
}
