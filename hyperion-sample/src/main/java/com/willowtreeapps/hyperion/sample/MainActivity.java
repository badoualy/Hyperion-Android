package com.willowtreeapps.hyperion.sample;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.willowtreeapps.hyperion.sample.debug.CustomLog;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Log samples for Timber plugin.
        Timber.wtf("Hello Timber Assert!");
        Timber.tag("TAG").d("Hello Timber Debug!");
        Timber.e("Hello Timber Error!");
        Timber.i("Hello Timber Info!");
        Timber.v("Hello Timber Verbose!");
        Timber.w("Hello Timber Warn!");
        Timber.d("https://google.com");

        CustomLog.debug("CUSTOM_TAG", "I'm a custom message!");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new PagerFragment())
                    .commit();
        }
    }
}
