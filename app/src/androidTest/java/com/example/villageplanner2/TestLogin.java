package com.example.villageplanner2;

import android.app.Activity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.activity.result.ActivityResult;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestLogin {
    @Rule public ActivityScenarioRule<LogInActivity> activityScenarioRule = new ActivityScenarioRule<>(LogInActivity.class);

    @Test
    public void testLogin() throws InterruptedException {
        onView(withId(R.id.email)).perform(typeText("irocha@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(10000);
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    @Test
    public void testWrongPassword() throws InterruptedException {
        onView(withId(R.id.email)).perform(typeText("irocha@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("1234567"), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(10000);
        onView(withId(R.id.login)).check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidEmail() throws InterruptedException {
        onView(withId(R.id.email)).perform(typeText("irocha1@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("1234567"), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(10000);
        onView(withId(R.id.login)).check(matches(isDisplayed()));
    }
}
