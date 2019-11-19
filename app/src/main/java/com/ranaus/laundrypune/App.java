package com.ranaus.laundrypune;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("UPNulfDCXTIn4wrOxzeT2g2t9MlmWAHI90GedSDz")
                // if defined
                .clientKey("OQOJyz89LTyzuUfPAOxqEjBEetBp0I0Vddd5rmZZ")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
