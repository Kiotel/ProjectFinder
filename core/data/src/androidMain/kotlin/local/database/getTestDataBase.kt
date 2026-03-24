package local.database

/*
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
}*/
