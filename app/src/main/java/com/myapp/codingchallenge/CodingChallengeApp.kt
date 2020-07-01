package com.myapp.codingchallenge

import android.app.Application
import com.facebook.stetho.Stetho
import com.google.gson.Gson
import com.myapp.codingchallenge.data.db.AppDatabase
import com.myapp.codingchallenge.data.network.ApiInterface
import com.myapp.codingchallenge.data.network.NetworkConnectionInterceptor
import com.myapp.codingchallenge.data.preferences.PreferenceProvider
import com.myapp.codingchallenge.data.repositories.FilterRepository
import com.myapp.codingchallenge.data.repositories.ItunesItemRepository
import com.myapp.codingchallenge.ui.filter.FilterViewModelFactory
import com.myapp.codingchallenge.ui.master.ItunesItemViewModelFactory
import com.myapp.codingchallenge.utils.WrapContentLinearLayoutManager
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class CodingChallengeApp : Application(), KodeinAware {

    /**
     * I used Kotlin Dependency injection (KodeIn) since I need to inject all my dependency class for my MVVM structure, this is commonly used by other android professionals
     * and I chose this instantiating different class from a specific class are said to be a bad practice and it promotes good app architecture for reusability of codes, ease of refactoring and testing.
     */
    override val kodein = Kodein.lazy {
        import(androidXModule(this@CodingChallengeApp))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { ApiInterface(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { Gson() }
        bind() from singleton { WrapContentLinearLayoutManager(instance()) }
        bind() from singleton { ItunesItemRepository(instance(), instance(), instance()) }
        bind() from singleton { FilterRepository(instance(), instance()) }

        bind() from provider { ItunesItemViewModelFactory(instance()) }
        bind() from provider { FilterViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()

        /**
         * Stetho is a debugging tool you can use in debugging core data and api responses that are synced from your app, it only requires a chrome browser,
         * just like the inspect element feature in web browsers.
         */
        if (BuildConfig.DEBUG) {
            Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .build()
            )
        }
    }
}