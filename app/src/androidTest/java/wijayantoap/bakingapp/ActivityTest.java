package wijayantoap.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import wijayantoap.bakingapp.Activity.MainActivity;
import wijayantoap.bakingapp.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Wijayanto A.P on 9/18/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void checkTextCheesecake() {
        onView(withId(R.id.recyclerViewMain)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText("Cheesecake")).check(matches(isDisplayed()));
    }

    @Test
    public void openVideoCheesecake() {
        onView(withId(R.id.recyclerViewMain)).perform(RecyclerViewActions.actionOnItemAtPosition(3,click()));
        onView(withId(R.id.recyclerViewSteps)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(allOf(withId(R.id.exoPlayer), isDisplayed()));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fabFull), isDisplayed()));
        floatingActionButton.perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();
        pressBack();
    }

    @Test
    public void checkTextNutella() {
        onView(withId(R.id.recyclerViewMain)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText("Nutella Pie")).check(matches(isDisplayed()));
    }

    @Test
    public void openVideoNutellaPie() {
        onView(withId(R.id.recyclerViewMain)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.recyclerViewSteps)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(allOf(withId(R.id.exoPlayer), isDisplayed()));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fabFull), isDisplayed()));
        floatingActionButton.perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();
        pressBack();
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
