package com.android.sushil.assignment;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.android.sushil.assignment.ui.main.MainActivity;
import com.android.sushil.assignment.util.ActivityUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class NoNetworkInstrumentedTest {

    public final ActivityTestRule<MainActivity> main =
            new ActivityTestRule<MainActivity>(MainActivity.class, false, false) {
                @Override
                protected Intent getActivityIntent() {
                    // Override the default intent so we pass a false flag for syncing so it doesn't
                    // start a sync service in the background that would affect  the behaviour of
                    // this test.
                    return MainActivity.getStartIntent(
                            InstrumentationRegistry.getTargetContext(), false);
                }
            };

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.android.sushil.githubexample", appContext.getPackageName());
    }


    @Test
    public void testNetworkConnectedFunc() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals(ActivityUtils.isNetworkConnected(appContext), false);
    }

    @Test
    public void testErrorMessageFunc() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        String errormsg = ActivityUtils.fetchErrorMessage(new Throwable(), appContext);
        assertEquals(errormsg, "Not connected to internet");
    }

}
