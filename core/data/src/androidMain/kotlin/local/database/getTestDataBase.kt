package local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

fun getTestDataBase(context: Context): RoomDatabase.Builder<TestDataBase> {
    val dbFile = context.getDatabasePath("user.db")
    return Room.databaseBuilder<TestDataBase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    )
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
}