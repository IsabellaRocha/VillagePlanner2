package com.example.villageplanner2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class TestNavBar {
    private String email = "irocha@usc.edu";
    private String password = "123456";

    @Rule
    public ActivityScenarioRule<LandingActivity> landingActivityActivityScenarioRule
            = new ActivityScenarioRule<>(LandingActivity.class);

    @Test
    public void TestGoToReminderPage() throws InterruptedException {
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.button2)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.reminders)).check(matches(isDisplayed()));
    }

    @Test
    public void TestGoToMapPage() throws InterruptedException {
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.button2)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.button1)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    @Test
    public void TestSignOut() throws InterruptedException {
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.button3)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
    }
}
