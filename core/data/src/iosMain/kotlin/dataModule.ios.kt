import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import local.database.TestDataBase
import local.database.getTestDataBase
import local.datastore.createDataStore
import org.koin.dsl.module

actual val platformModule = module {
    single<DataStore<Preferences>> { createDataStore() }
    single<TestDataBase> { getTestDataBase().build() }
}