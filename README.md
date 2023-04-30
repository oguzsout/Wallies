# Wallies


![Component 1](https://user-images.githubusercontent.com/73955284/233776416-58b37197-180c-4fa3-b2e7-4da14733431e.png)

In this application consisting of multiple screens, you can review popular and recently added photos and set the wallpaper for your phone's background on the detail screen. In the detail screen, you can view the high resolution image by swiping or zooming the relevant photo, you can download it to your phone if you want, and you can share the photo with your friends. On the Collections screen, you can review the photos in the collections created by users on the unsplash site and save the photo you like. If you don't want to be content with these, search and review the photo you want on the search screen.

# Setup
- You must create a developer account at [https://unsplash.com/developers](https://unsplash.com/developers)
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
- [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) - A cold asynchronous data stream that sequentially emits values and completes normally or with an exception.
- [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow) - StateFlow is a
  state-holder observable flow that emits the current and new state updates to its collectors.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
- [Material Design](https://material.io/develop/android/docs/getting-started/) - Build awesome beautiful UIs.
- [Navigation](https://developer.android.com/guide/navigation/navigation-getting-started) - Android Jetpack's Navigation component helps you implement navigation, from simple button clicks to more complex patterns, such as app bars and the navigation drawer.
- [Pagination](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - The Paging library helps you load and display pages of data from a larger dataset from local storage or over network.
- [Room](https://developer.android.com/training/data-storage/room) - The room persistence library it is an abstraction layer over SQLite.
- [Coil](https://github.com/coil-kt/coil) - Image loading for Android backed by Kotlin Coroutines.

## 📸 Screenshots

|   |   |
|---|---|
|![](https://user-images.githubusercontent.com/73955284/235374976-869f35d8-8646-4c58-8cd5-9f38a3620fec.png)| ![](https://user-images.githubusercontent.com/73955284/235375011-d6f71965-1039-43a1-afd1-0b264bba5e7a.png)    |![](https://user-images.githubusercontent.com/73955284/235375049-fad3712d-adb0-489d-99a7-cee5ce0111b9.png)| ![](https://user-images.githubusercontent.com/73955284/235375072-d9089ca2-4524-4ce9-b8cc-2d0e0ae65a6b.png) 
|![](https://user-images.githubusercontent.com/73955284/235375106-85b5679b-d68e-4394-8f19-0da2609bf7df.png)| ![](https://user-images.githubusercontent.com/73955284/235375132-9dbc1819-c2e9-461e-9079-ce6097a9d5a7.png)
|![](https://user-images.githubusercontent.com/73955284/235375157-15c13532-2896-4d22-aaf5-49f6ebd8ba3b.png)| ![](https://user-images.githubusercontent.com/73955284/235375256-c17e525f-0709-4b29-b978-e4ed25b1d212.png)|

## Architecture
A well planned architecture is extremely important for an app to scale and all architectures have one common goal- to manage complexity of your app. This isn't something to be worried about in smaller apps however it may prove very useful when working on apps with longer development lifecycle and a bigger team.

Clean architecture was proposed by [Robert C. Martin](https://en.wikipedia.org/wiki/Robert_C._Martin) in 2012 in the [Clean Code Blog](http://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) and it follow the SOLID principle.

<p align="center"><img src="art/clean_arch.jpeg" alt="Clean Architecture"></p>

The circles represent different layers of your app. Note that:

- The center circle is the most abstract, and the outer circle is the most concrete. This is called the [Abstraction Principle](https://en.wikipedia.org/wiki/Abstraction_principle_(computer_programming)). The Abstraction Principle specifies that inner circles should contain business logic, and outer circles should contain implementation details.

- Another principle of Clean Architecture is the [Dependency Inversion](https://en.wikipedia.org/wiki/Dependency_inversion_principle). This rule specifies that each circle can depend only on the nearest inward circle ie. low-level modules do not depend on high-level modules but the other way around.

<p align="center"><img src="art/architecture.png" alt="Clean Architecture Diagram"></p>

# Todos
- [ ] Jetpack Compose Migration
- [ ] Unit Tests
- [ ] More Spesific Screens
