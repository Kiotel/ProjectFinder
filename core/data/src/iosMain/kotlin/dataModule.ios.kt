import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import eu.anifantakis.lib.ksafe.KSafe
import local.database.UserDataBase
import local.database.getTestDataBase
import local.datastore.createDataStore
import org.koin.dsl.module

actual val platformModule = module {
    single<DataStore<Preferences>> { createDataStore() }
    single<KSafe> { KSafe() }
    single<UserDataBase> { getTestDataBase().build() }
}