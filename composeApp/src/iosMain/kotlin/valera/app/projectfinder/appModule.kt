package valera.app.projectfinder

import dataModule
import domainModule
import org.koin.dsl.module
import uiModule

val appModule = module {
    includes(uiModule, domainModule, dataModule)
}