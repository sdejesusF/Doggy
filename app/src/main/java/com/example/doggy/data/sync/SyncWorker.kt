package com.example.doggy.data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncer: Syncer,
): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return sync()
    }

    private suspend fun sync(): Result {
        return try {
            syncer.execute()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}