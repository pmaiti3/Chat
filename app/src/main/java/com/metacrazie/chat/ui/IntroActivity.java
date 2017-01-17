package com.metacrazie.chat.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.metacrazie.chat.MainActivity;
import com.metacrazie.chat.R;

/**
 * Created by praty on 27/12/2016.
 */

public class IntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mSharedPreference = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putBoolean("first_run", false);
        editor.apply();

        //TODO improve UI

        addSlide(AppIntroFragment.newInstance(getString(R.string.intro1), getString(R.string.intro1_desc), R.drawable.app_icon , getTitleColor()));
        addSlide(AppIntroFragment.newInstance(getString(R.string.intro2), getString(R.string.intro2_desc), R.drawable.app_icon , getTitleColor()));
        addSlide(AppIntroFragment.newInstance(getString(R.string.intro3), getString(R.string.intro3_desc), R.drawable.app_icon , getTitleColor()));


        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        setSlideOverAnimation();

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
