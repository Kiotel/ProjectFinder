import local.database.TestDataBase
import database.getTestDataBase
import org.koin.dsl.module

actual val platformModule = module {
    single<TestDataBase> { getTestDataBase(get()).build() }
}