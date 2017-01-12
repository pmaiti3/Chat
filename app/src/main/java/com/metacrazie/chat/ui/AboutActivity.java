package com.metacrazie.chat.ui;

import android.os.Bundle;

import com.metacrazie.chat.R;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsActivity;

/**
 * Created by praty on 03/01/2017.
 */

public class AboutActivity extends LibsActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        setIntent(new LibsBuilder()
                .withAboutIconShown(true)
                .withActivityTitle(getString(R.string.nav_about))
                .withAboutAppName(getString(R.string.app_name))
                .withAboutVersionShown(true)
                .withAboutDescription(getString(R.string.intro1_desc))
                //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR).intent(this));
                //start the activity
        super.onCreate(savedInstanceState);
    }
}
