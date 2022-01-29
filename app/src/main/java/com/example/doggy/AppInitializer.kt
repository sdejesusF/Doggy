package com.example.doggy

import android.app.Application

interface AppInitializer {
    fun init(application: Application)
}