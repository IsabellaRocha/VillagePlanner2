package com.example.villageplanner2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestSignUp {
    @Rule
    public ActivityScenarioRule<SignInActivity> activityScenarioRule = new ActivityScenarioRule<>(SignInActivity.class);

    @Test
    public void testSignUp() throws InterruptedException {
        onView(withId(R.id.firstName)).perform(typeText("Angie"), closeSoftKeyboard());
        onView(withId(R.id.lastName)).perform(typeText("Jia"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("aqjia@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.profilePhoto)).perform(typeText("https://cdn.mos.cms.futurecdn.net/7GCPeSkqz3duhcXkg7E6H7-320-80.jpg"), closeSoftKeyboard());
        onView(withId(R.id.signin)).perform(click());
        Thread.sleep(10000);
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

//    @Test
//    public voidTestExistingUser() throws InterruptedException {
//
//    }
}

