package local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers


internal fun getProjectsDatabase(context: Context): RoomDatabase.Builder<ProjectsDataBase> {
    val dbFile = context.getDatabasePath("projects.db")
    return Room.databaseBuilder<ProjectsDataBase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    )
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
}
