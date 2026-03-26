import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import local.database.UserDataBase
import local.database.getUserDatabase
import local.datastore.createDataStore
import org.koin.dsl.module

actual val platformModule = module {
    single<DataStore<Preferences>> { createDataStore(context = get()) }
    single<UserDataBase> { getUserDatabase(get()).build() }
}