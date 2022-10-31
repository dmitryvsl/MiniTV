package com.example.minitv

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.minitv.data.database.MiniTVDao
import com.example.minitv.data.database.MiniTVDatabase
import com.example.minitv.data.model.ReportEntity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class MiniTvDatabaseTest {
    private lateinit var dao: MiniTVDao
    private lateinit var db: MiniTVDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MiniTVDatabase::class.java).build()
        dao = db.dao()
    }

    @Test
    fun writeReportAndReadInList() = runBlocking {
        val report = ReportEntity(
            id = 1,
            videoId = 1,
            videoName = "video1",
            startTime = Date(4600)
        )
        dao.saveReport(report)
        val reportList = dao.getReports()

        assertThat(reportList[0], equalTo(report))

    }
}