package com.example.villageplanner2;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestCancelReminder {
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
    public void TestCancelReminder() {
        onView(withId(R.id.login)).perform(click());
        wait(2000);
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        wait(5000);
        onView(withId(R.id.button2)).perform(click());
        wait(2000);

    }

}
