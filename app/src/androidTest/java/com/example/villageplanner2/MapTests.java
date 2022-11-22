package com.example.villageplanner2;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
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


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MapTests {
    private String email = "irocha@usc.edu";
    private String password = "123456";
    @Rule
    public ActivityScenarioRule<LandingActivity> activityRule = new ActivityScenarioRule<>(LandingActivity.class);

    @Test
    public void displayRouteTest() throws InterruptedException {
        onView(withId(R.id.login)).perform(click());
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(5000);
        //onView(withId(R.id.store)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("CAVA"))).perform(click());
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject storeMarkerOne = device.findObject(new UiSelector().descriptionContains("CAVA"));
        UiObject myMarkerOne = device.findObject(new UiSelector().descriptionContains("My Location"));
        UiObject routeOne = device.findObject(new UiSelector().descriptionContains("Route"));

    }
}
