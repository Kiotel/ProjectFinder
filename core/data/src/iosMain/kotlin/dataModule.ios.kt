import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import eu.anifantakis.lib.ksafe.KSafe
import local.database.ProjectsDataBase
import local.database.UserDataBase
import local.database.getProjectsDataBase
import local.database.getUserDataBase
import local.datastore.createDataStore
import org.koin.dsl.module

internal actual val platformModule = module {
    single<DataStore<Preferences>> { createDataStore() }
    single<KSafe> { KSafe() }
    single<UserDataBase> { getUserDataBase().build() }
    single<ProjectsDataBase> { getProjectsDataBase().build() }
}