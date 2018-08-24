package wijayantoap.bakingapp;

import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import wijayantoap.bakingapp.Activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Wijayanto A.P on 9/17/2017.
 */

@RunWith(AndroidJUnit4.class)
@MediumTest
public class RecyclerViewTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(MainActivity.class);

    @Test
    public void listGoesOverTheFold() {
        onView(withId(R.id.recyclerViewMain)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerViewMain)).perform(click());
    }

}