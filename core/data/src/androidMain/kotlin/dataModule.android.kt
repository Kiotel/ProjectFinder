import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import eu.anifantakis.lib.ksafe.KSafe
import local.database.UserDataBase
import local.database.getUserDatabase
import local.datastore.createDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal actual val platformModule = module {
    single<DataStore<Preferences>> { createDataStore(context = get()) }
    single<KSafe> { KSafe(androidContext()) }
    single<UserDataBase> { getUserDatabase(get()).build() }
}