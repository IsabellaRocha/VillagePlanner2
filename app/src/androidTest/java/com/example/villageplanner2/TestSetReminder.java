package com.example.villageplanner2;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.contrib.PickerActions;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static org.hamcrest.Matchers.anything;

import java.text.SimpleDateFormat;
import java.util.Date;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestSetReminder {
    private String email = "irocha@usc.edu";
    private String password = "123456";

    @Rule
    public ActivityScenarioRule<LandingActivity> landingActivityActivityScenarioRule
            = new ActivityScenarioRule<>(LandingActivity.class);

    public void wait(int time) {
        long stopWait = System.currentTimeMillis() + time;
        while (System.currentTimeMillis() < stopWait);
    }

    @Test
    public void TestSetReminderWithValidTime() {
        long reminderTime = System.currentTimeMillis() + 12*60000;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String time = df.format(new Date(reminderTime));
        int hour = Integer.parseInt(time.substring(0, 2));
        int min = Integer.parseInt(time.substring(3, 5));
        onView(withId(R.id.login)).perform(click());
        wait(2000);
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        wait(5000);
        onView(withId(R.id.button2)).perform(click());
        wait(2000);
        onView(withId(R.id.setReminder)).perform(click());
        wait(2000);
        onView(withId(R.id.timePicker)).perform(PickerActions.setTime(hour, min));
        onView(withId(R.id.store)).perform(click());
        onData(anything()).inRoot(isPlatformPopup()).atPosition(16).perform(click());
        onView(withId(R.id.confirmReminder)).perform(click());
    }

    @Test
    public void TestSetReminderWithInvalidTime() {
        long reminderTime = System.currentTimeMillis() - 12*60000;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String time = df.format(new Date(reminderTime));
        int hour = Integer.parseInt(time.substring(0, 2));
        int min = Integer.parseInt(time.substring(3, 5));
        onView(withId(R.id.login)).perform(click());
        wait(2000);
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        wait(5000);
        onView(withId(R.id.button2)).perform(click());
        wait(2000);
        onView(withId(R.id.setReminder)).perform(click());
        wait(2000);
        onView(withId(R.id.timePicker)).perform(PickerActions.setTime(hour, min));
        onView(withId(R.id.store)).perform(click());
        onData(anything()).inRoot(isPlatformPopup()).atPosition(16).perform(click());
        onView(withId(R.id.confirmReminder)).perform(click());
        wait(2000);
        onView(withId(R.id.timePicker)).inRoot(isDialog()).check(matches(isDisplayed()));
    }
}
