package com.example.villageplanner2;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

import android.view.View;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestCancelReminder {
    private String email = "irocha@usc.edu";
    private String password = "123456";

    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }

    @Rule
    public ActivityScenarioRule<LandingActivity> landingActivityActivityScenarioRule
            = new ActivityScenarioRule<>(LandingActivity.class);
/*
    @Test
    public void TestCancelReminder() throws InterruptedException{
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.button2)).perform(click());
        Thread.sleep(2000);
        onView(withText("Cancel Reminder")).perform(click());
        onView(withIndex(withText("Cancel Reminder"), 1)).perform(click());
        Thread.sleep(5000);
    }

 */

}
