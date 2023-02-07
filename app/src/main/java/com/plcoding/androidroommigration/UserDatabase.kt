package com.plcoding.androidroommigration

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [User::class, School::class],
    version = 4,
    // after updating the version add autoMigrations
    autoMigrations = [
        // from one migration to the other (added created column)
        AutoMigration(from = 1, to = 2),

        // (renamed created column)
        // Room cannot recognize whether we changed a column or replace a column. (Changing a name is also a migration)
        // So we need to specify what we done in a Migration class.
        AutoMigration(from = 2, to = 3, spec = UserDatabase.Migration2To3::class),
    ]
)
abstract class UserDatabase: RoomDatabase() {

    abstract val dao: UserDao

    // We renamed a column so we use the annotation @RenameColumn.
    // There are other annotations like @DeleteColumn, DeleteTable, RenameTable
    @RenameColumn(tableName = "User", fromColumnName = "created", toColumnName = "createdAt")
    class Migration2To3: AutoMigrationSpec

    // (added School table)
    // Manual Migration
    companion object {
        // Migration from 3 to 4
        val migration3To4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // we get a db reference so we can run queries
                // Here we create the new table we added
                database.execSQL("CREATE TABLE IF NOT EXISTS school (name CHAR NOT NULL PRIMARY KEY)")
            }
        }
    }
}