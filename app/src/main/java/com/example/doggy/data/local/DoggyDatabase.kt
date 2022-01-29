package com.example.doggy.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.doggy.data.local.dao.BreedDao
import com.example.doggy.data.local.entity.BreedEntity
import com.example.doggy.data.local.entity.TempBreedEntity

@Database(
    entities = [BreedEntity::class, TempBreedEntity::class],
    exportSchema = false,
    version = 1
)
abstract class DoggyDatabase: RoomDatabase() {
    abstract fun getBreedDao(): BreedDao
    companion object {
        @Volatile
        private var Instance: DoggyDatabase? = null

        fun getInstance(context: Context): DoggyDatabase {
            val tempInstance = Instance
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DoggyDatabase::class.java,
                    "doggy"
                ).build()

                Instance = instance
                return instance
            }
        }
    }
}