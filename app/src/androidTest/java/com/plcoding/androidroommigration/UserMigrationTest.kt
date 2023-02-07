package com.plcoding.androidroommigration

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val DB_NAME = "test"

@RunWith(AndroidJUnit4::class)
class UserMigrationTest {

    // @get:Rule - add new behaviour to our use cases, get access to special functionality in our case Room
    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        UserDatabase::class.java,
        listOf(UserDatabase.Migration2To3()),
        FrameworkSQLiteOpenHelperFactory()
    )

    // We could test a specific migration from 1 to 2
    @Test
    fun migration1To2_containsCorrectData() {
        var db = helper.createDatabase(DB_NAME, 1).apply {
            execSQL("INSERT INTO user VALUES('test@test.com', 'Philipp')")
            close()
        }

        db = helper.runMigrationsAndValidate(DB_NAME, 2, true)

        db.query("SELECT * FROM user").apply {
            assertThat(moveToFirst()).isTrue()
            assertThat(getLong(getColumnIndex("created"))).isEqualTo(0)
        }
    }

    // Or run all of our migrations to see if there are some issues
    @Test
    fun testAllMigrations() {
        // create db and close it because we dont want to add anything
        helper.createDatabase(DB_NAME, 1).apply { close() }

        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            UserDatabase::class.java,
            DB_NAME
        ).addMigrations(UserDatabase.migration3To4).build().apply {
            openHelper.writableDatabase.close()
        }
    }
}