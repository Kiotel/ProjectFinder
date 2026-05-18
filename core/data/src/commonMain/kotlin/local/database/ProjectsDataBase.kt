package local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import local.database.entities.ProjectEntity
import local.database.entities.ProjectsDao
import local.database.utils.converters.ListStringConverter

@Database(entities = [ProjectEntity::class], version = 3)
@ConstructedBy(ProjectsDataBaseConstructor::class)
@TypeConverters(ListStringConverter::class)
internal abstract class ProjectsDataBase : RoomDatabase() {
    abstract fun projectsDao(): ProjectsDao
}


@Suppress("KotlinNoActualForExpect")
internal expect object ProjectsDataBaseConstructor : RoomDatabaseConstructor<ProjectsDataBase> {
    override fun initialize(): ProjectsDataBase
}

