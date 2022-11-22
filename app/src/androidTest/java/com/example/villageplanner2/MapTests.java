package com.example.villageplanner2;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.example.villageplanner2.HomeLogic.HomepageActivity;
import com.example.villageplanner2.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

public class MapTests {
    @Rule
    public ActivityScenarioRule<MapActivity> activityRule = new ActivityScenario<MapActivity>(MapActivity.class);

    @Test
    public void displayRouteTest() throws UIObjectNotFoundException {
        onView(withId(R.id.store)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("CAVA"))).perform(click());
        onView(withId(R.id.display_route)).perform(click());
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject storeMarkerOne = device.findObject(new UiSelector().descriptionContains("CAVA"));
        UiObject myMarkerOne = device.findObject(new UiSelector().descriptionContains("My Location"));
        UiObject routeOne = device.findObject(new UiSelector().descriptionContains("Route"));

    }
}
