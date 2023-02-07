package com.plcoding.androidroommigration

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = false)
    val email: String,
    val username: String,

    // if we change the current entity columns it is necessary to update db version
    // and migrate it (what to assign to that column)
    // This is a automatic migration we just set a default value
    // If we want specific values we need to do a manual migration.
    @ColumnInfo(name = "created", defaultValue = "0")
    val createdAt: Long = System.currentTimeMillis()
    // Now the database will add this new column without throwing exceptions
    // it will also add the default value.
    // Next step is to go to UserDatabase and update db version and ...
)
