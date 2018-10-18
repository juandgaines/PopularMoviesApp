package com.example.android.popularmoviesapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class ReminderService extends IntentService{


    public ReminderService() {
        super("ReminderService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
