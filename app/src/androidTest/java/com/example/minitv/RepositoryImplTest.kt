package com.example.minitv

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.minitv.data.RepositoryImpl
import com.example.minitv.data.database.MiniTVDao
import com.example.minitv.data.database.MiniTVDatabase
import com.example.minitv.domain.model.Report
import com.example.minitv.domain.repository.Repository
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class RepositoryImplTest {
    private lateinit var dao: MiniTVDao
    private lateinit var db: MiniTVDatabase

    private lateinit var repository: Repository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MiniTVDatabase::class.java).build()
        dao = db.dao()
        repository = RepositoryImpl(dao)
    }

    @After
    fun closeDb(){
        db.close()
    }

    @Test
    fun saveReport_and_getReports_saves_and_returns_saved_report() = runBlocking {
        val report: Report = Report(
            id = 1,
            videoId = 1,
            videoName = "Video1",
            startTime = Date(3600L)
        )
        repository.saveReport(report)
        val savedReports = repository.getReports()

        assertThat(savedReports[0], equalTo(report))
    }
}