package com.example.doggy.data.sync

import androidx.lifecycle.asFlow
import androidx.work.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface SyncOrchestrator {
    val syncStatus: Flow<SyncStatus>
    fun sync()
}

enum class SyncStatus {
    RUNNING, FAILED, COMPLETED
}

class SyncOrchestratorDefault @Inject constructor(
    private val workManager: WorkManager
): SyncOrchestrator {
    override val syncStatus: Flow<SyncStatus> = workManager.getWorkInfosByTagLiveData("SYNC_WORKER")
        .asFlow()
        .map {
            if (it != null && it.firstOrNull() != null) {
                mapWorkInfoStateToSyncState(it.firstOrNull()!!.state)
            } else {
                SyncStatus.COMPLETED
            }
        }

    override fun sync() {
        val syncWorker = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(getRequiredConstraints())
            .addTag("SYNC_WORKER")
            .build()

        workManager.enqueueUniqueWork(
            "SYNC_WORKER",
            ExistingWorkPolicy.REPLACE,
            syncWorker
        )
    }

    private fun getRequiredConstraints(): Constraints = Constraints.Builder()
        .build()

    private fun mapWorkInfoStateToSyncState(state: WorkInfo.State): SyncStatus = when (state) {
        WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING, WorkInfo.State.BLOCKED -> SyncStatus.RUNNING
        WorkInfo.State.CANCELLED -> SyncStatus.COMPLETED
        WorkInfo.State.FAILED -> SyncStatus.FAILED
        WorkInfo.State.SUCCEEDED -> SyncStatus.COMPLETED
    }
}