package com.shoaib.notes_app_kmp.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module


expect fun KoinApplication.setupPlatformContext()


fun initKoin(
    appDeclaration: KoinApplication.() -> Unit = {}
) {
    startKoin {
        setupPlatformContext()

        modules(
            platformModule,
            dataModule,
            domainModule,
            viewModelModule
        )

        appDeclaration()
    }
}