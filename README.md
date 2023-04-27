# Wallies


![Component 1](https://user-images.githubusercontent.com/73955284/233776416-58b37197-180c-4fa3-b2e7-4da14733431e.png)

In this application consisting of multiple screens, you can review popular and recently added photos and set the wallpaper for your phone's background on the detail screen. In the detail screen, you can view the high resolution image by swiping or zooming the relevant photo, you can download it to your phone if you want, and you can share the photo with your friends. On the Collections screen, you can review the photos in the collections created by users on the unsplash site and save the photo you like. If you don't want to be content with these, search and review the photo you want on the search screen.

# Setup
- You must create a developer account at (https://unsplash.com/developers)
- You have to create your application and get the access key
- You have to add the access key in `local.properties`
```
API_KEY=""

```


# Tech Stacks
This project uses many of the popular libraries, plugins and tools of the android ecosystem.
- [Clean Architecture](https://developer.android.com/topic/architecture) - Clean architecture is a method of software development in which you should be able to identify what a program performs merely by looking at its source code.
- [Hilt](https://dagger.dev/hilt/) - Dependency Injection library.
- [Retrofit](https://square.github.io/retrofit/) - Type-safe http client and supports coroutines out of the box.
- [OkHttp-Logging-Interceptor](https://github.com/square/okhttp/blob/master/okhttp-logging-interceptor/README.md) - Logs HTTP request and response data.
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Library Support for coroutines.
- [Flow](https://developer.android.com/kotlin/flow) - Flows are built on top of coroutines and can provide multiple values. A flow is conceptually a stream of data that can be computed asynchronously.
- [Material Design](https://material.io/develop/android/docs/getting-started/) - Build awesome beautiful UIs.
- [Navigation](https://developer.android.com/guide/navigation/navigation-getting-started) - Android Jetpack's Navigation component helps you implement navigation, from simple button clicks to more complex patterns, such as app bars and the navigation drawer.
- [Pagination](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - The Paging library helps you load and display pages of data from a larger dataset from local storage or over network.
- [Room](https://developer.android.com/training/data-storage/room) - The room persistence library it is an abstraction layer over SQLite.
- [Coil](https://github.com/coil-kt/coil) - Image loading for Android backed by Kotlin Coroutines.

# Todos
- Jetpack Compose Migration
- Unit Tests
- More Spesific Screens

