package com.android.sushil.assignment;

import com.android.sushil.assignment.data.models.LocalRepositories;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import io.realm.Realm;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Unit tests integration with Realm using Robolectric
 */

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 19)
@PowerMockIgnore({"org.mockito.*"})
@PrepareForTest({Realm.class})
public class LocalDatabaseHelperTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();
    private Realm mMockRealm;

    @Before
    public void setupRealm() {
        mockStatic(Realm.class);

        Realm mockRealm = PowerMockito.mock(Realm.class);

        when(Realm.getDefaultInstance()).thenReturn(mockRealm);

        mMockRealm = mockRealm;
    }

    @Test
    public void shouldBeAbleToGetDefaultInstance() {
        assertThat(Realm.getDefaultInstance(), is(mMockRealm));
    }

    @Test
    public void shouldBeAbleToMockRealmMethods() {
        when(mMockRealm.isAutoRefresh()).thenReturn(true);
        assertThat(mMockRealm.isAutoRefresh(), is(true));

        when(mMockRealm.isAutoRefresh()).thenReturn(false);
        assertThat(mMockRealm.isAutoRefresh(), is(false));
    }

    @Test
    public void shouldBeAbleToCreateRealmObject() {
        LocalRepositories repo = new LocalRepositories();
        when(mMockRealm.createObject(LocalRepositories.class)).thenReturn(repo);

        LocalRepositories output = mMockRealm.createObject(LocalRepositories.class);

        assertThat(output, is(repo));
    }
}
