package com.example.villageplanner2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestSignUp {
    @Rule
    public ActivityScenarioRule<SignInActivity> activityScenarioRule = new ActivityScenarioRule<>(SignInActivity.class);

    @Test
    public void TestSignUp() throws InterruptedException {
        onView(withId(R.id.firstName)).perform(typeText("Angie"), closeSoftKeyboard());
        onView(withId(R.id.lastName)).perform(typeText("Jia"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("aqjia@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.profilePhoto)).perform(typeText("https://cdn.mos.cms.futurecdn.net/7GCPeSkqz3duhcXkg7E6H7-320-80.jpg"), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());
        Thread.sleep(10000);
        onView(withId(R.id.login)).check(matches(isDisplayed()));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        FirebaseDatabase.getInstance().getReference("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Deleted user");
                        }
                    }
                });
    }

    @Test
    public void TestExistingUser() throws InterruptedException {
        onView(withId(R.id.firstName)).perform(typeText("Angie"), closeSoftKeyboard());
        onView(withId(R.id.lastName)).perform(typeText("Jia"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("irocha@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.profilePhoto)).perform(typeText("https://cdn.mos.cms.futurecdn.net/7GCPeSkqz3duhcXkg7E6H7-320-80.jpg"), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.register)).check(matches(isDisplayed()));
    }

    @Test
    public void TestNoFirstName() throws InterruptedException {
        onView(withId(R.id.lastName)).perform(typeText("Jia"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("aqjia@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.profilePhoto)).perform(typeText("https://cdn.mos.cms.futurecdn.net/7GCPeSkqz3duhcXkg7E6H7-320-80.jpg"), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.register)).check(matches(isDisplayed()));
    }

    @Test
    public void TestNoLastName() throws InterruptedException {
        onView(withId(R.id.firstName)).perform(typeText("Angie"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("aqjia@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.profilePhoto)).perform(typeText("https://cdn.mos.cms.futurecdn.net/7GCPeSkqz3duhcXkg7E6H7-320-80.jpg"), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.register)).check(matches(isDisplayed()));
    }

    @Test
    public void TestNoEmail() throws InterruptedException {
        onView(withId(R.id.firstName)).perform(typeText("Angie"), closeSoftKeyboard());
        onView(withId(R.id.lastName)).perform(typeText("Jia"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.profilePhoto)).perform(typeText("https://cdn.mos.cms.futurecdn.net/7GCPeSkqz3duhcXkg7E6H7-320-80.jpg"), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.register)).check(matches(isDisplayed()));
    }

    @Test
    public void TestNoPassword() throws InterruptedException {
        onView(withId(R.id.firstName)).perform(typeText("Angie"), closeSoftKeyboard());
        onView(withId(R.id.lastName)).perform(typeText("Jia"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("aqjia@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.profilePhoto)).perform(typeText("https://cdn.mos.cms.futurecdn.net/7GCPeSkqz3duhcXkg7E6H7-320-80.jpg"), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.register)).check(matches(isDisplayed()));
    }

    @Test
    public void TestNoConfirmPassword() throws InterruptedException {
        onView(withId(R.id.firstName)).perform(typeText("Angie"), closeSoftKeyboard());
        onView(withId(R.id.lastName)).perform(typeText("Jia"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("aqjia@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.profilePhoto)).perform(typeText("https://cdn.mos.cms.futurecdn.net/7GCPeSkqz3duhcXkg7E6H7-320-80.jpg"), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.register)).check(matches(isDisplayed()));
    }

    @Test
    public void TestDifferentConfirmPassword() throws InterruptedException {
        onView(withId(R.id.firstName)).perform(typeText("Angie"), closeSoftKeyboard());
        onView(withId(R.id.lastName)).perform(typeText("Jia"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("aqjia@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.confirmPassword)).perform(typeText("1234567"), closeSoftKeyboard());
        onView(withId(R.id.profilePhoto)).perform(typeText("https://cdn.mos.cms.futurecdn.net/7GCPeSkqz3duhcXkg7E6H7-320-80.jpg"), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.register)).check(matches(isDisplayed()));
    }

}

