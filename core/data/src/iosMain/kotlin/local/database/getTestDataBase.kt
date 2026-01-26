package local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSHomeDirectory

fun getTestDataBase(): RoomDatabase.Builder<TestDataBase> {
    val dbFile = NSHomeDirectory() + "/user.db"
    return Room.databaseBuilder<TestDataBase>(
        name = dbFile,
        // factory = { TestDataBase::class.instantiateImpl() }
    )
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
}