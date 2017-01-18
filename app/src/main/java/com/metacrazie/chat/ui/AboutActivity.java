package com.metacrazie.chat.ui;

import android.content.SharedPreferences;
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


        Libs.ActivityStyle style;
        Libs.ActivityStyle style1 = Libs.ActivityStyle.LIGHT_DARK_TOOLBAR;
        Libs.ActivityStyle style2 = Libs.ActivityStyle.DARK;

        SharedPreferences sharedPrefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean isSet = sharedPrefs.getBoolean("switch_theme", false);

        if (isSet){
            style = style2;
        }else {
            style = style1;
        }
        setIntent(new LibsBuilder()
                .withAboutIconShown(true)
                .withActivityTitle(getString(R.string.nav_about))
                .withAboutAppName(getString(R.string.app_name))
                .withAboutVersionShown(true)
                .withAboutDescription(getString(R.string.credits1))
                //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                .withActivityStyle(style).intent(this));
                //start the activity
        super.onCreate(savedInstanceState);
    }
}
