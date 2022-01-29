package com.example.doggy.data.sync

import android.app.Application
import com.example.doggy.AppInitializer
import dagger.Lazy
import javax.inject.Inject

class SyncInitializer @Inject constructor(
    private val syncOrchestrator: Lazy<SyncOrchestrator>
): AppInitializer {
    override fun init(application: Application) {
        syncOrchestrator.get().sync()
    }
}