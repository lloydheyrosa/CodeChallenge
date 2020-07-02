# Code Challenge

This is a Coding challenge from Appetiser Company.

## Architecture - MVVM, KodeIn & Coroutines

I used [MVVM](https://developer.android.com/jetpack/guide) as my coding architecture in since this is the best practice in Android development so far, not only to provide a clean code structure, but to provide a scalable, readable and understandable code structure. It also provides a better way of data handling using ViewModels and LiveData.

```Kotlin
implementation "androidx.lifecycle:lifecycle-extensions:2.2.0"
```

And for handling long process functions, I used [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) for asynchronous programming techniques to provide seamless and non-blocking execution of long-process functions such as api requests and saving or deleting large records from the local database, and for the usage of many classes, it is given to have some classes to be dependent in each other, so I use [KodeIn](https://kodein.org/di/) to serve as a dependency injection of my classes.

## Persistence: ROOM & SharedPreferences

I used [Room](https://developer.android.com/topic/libraries/architecture/room) as my persistence local database library because it is easy and straightforward to use, the advantage of using this is, unlike [Realm](https://realm.io/blog/realm-for-android/), it doesn't increase APK size and supports table relationship.

```Kotlin
//Android Room
implementation "androidx.room:room-runtime:2.2.5"
implementation "androidx.room:room-ktx:2.2.5"
kapt "androidx.room:room-compiler:2.2.5"
```

## REST Client - Retrofit
I used [Retrofit]() becuase it is easy and very straightforward to use, it automatically converts api responses to your data class models.


