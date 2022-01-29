package com.example.doggy.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.doggy.data.local.DoggyDatabase
import com.example.doggy.data.local.dao.BreedDao
import junit.framework.TestCase
import org.junit.After
import org.junit.Before

open class BaseDatabaseTest: TestCase() {
    lateinit var db: DoggyDatabase
    lateinit var dao: BreedDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, DoggyDatabase::class.java).build()
        dao = db.getBreedDao()
    }

    @After
    fun closeDb() {
        db.close()
    }
}