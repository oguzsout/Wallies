# Wallies

<p align="center"><img src=https://user-images.githubusercontent.com/73955284/233776416-58b37197-180c-4fa3-b2e7-4da14733431e.png width="700" height="500">

<a href='https://play.google.com/store/apps/details?id=com.oguzdogdu.wallieshd&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png' height='80' /></a>


In this application consisting of multiple screens, you can review popular and recently added photos and set the wallpaper for your phone's background on the detail screen. In the detail screen, you can view the high resolution image by swiping or zooming the relevant photo, you can download it to your phone if you want, and you can share the photo with your friends. On the Collections screen, you can review the photos in the collections created by users on the unsplash site and save the photo you like. If you don't want to be content with these, search and review the photo you want on the search screen.

# 🛠️ Setup
- You must create a developer account at [https://unsplash.com/developers](https://unsplash.com/developers)
- You have to create your application and get the access key
- You have to add the access key in `local.properties`
```
DEBUG_API_KEY=""
```

# 🏭 Tech Stacks
This project uses many of the popular libraries, plugins and tools of the android ecosystem.
- [Clean Architecture](https://developer.android.com/topic/architecture) - Clean architecture is a method of software development in which you should be able to identify what a program performs merely by looking at its source code.
- [Hilt](https://dagger.dev/hilt/) - Dependency Injection library.
- [KSP](https://developer.android.com/build/migrate-to-ksp) - KSP (Kotlin Symbol Processing) is a Kotlin-first alternative to kapt. KSP analyzes Kotlin code directly, which is up to 2x faster.
- [Retrofit](https://square.github.io/retrofit/) - Type-safe http client and supports coroutines out of the box.
- [Ktlint](https://github.com/jlleitschuh/ktlint-gradle) - This plugin creates convenient tasks in your Gradle project that run ktlint checks or do code auto format.
- [OkHttp-Logging-Interceptor](https://github.com/square/okhttp/blob/master/okhttp-logging-interceptor/README.md) - Logs HTTP request and response data.
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Library Support for coroutines.
- [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) - A cold asynchronous data stream that sequentially emits values and completes normally or with an exception.
- [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow) - StateFlow is a
  state-holder observable flow that emits the current and new state updates to its collectors.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
- [Material Design](https://material.io/develop/android/docs/getting-started/) - Build awesome beautiful UIs.
- [Motion Layout](https://developer.android.com/develop/ui/views/animations/motionlayout) - MotionLayout is a layout type that helps you manage motion and widget animation in your app.
- [Navigation](https://developer.android.com/guide/navigation/navigation-getting-started) - Android Jetpack's Navigation component helps you implement navigation, from simple button clicks to more complex patterns, such as app bars and the navigation drawer.
- [Viewpager](https://developer.android.com/guide/navigation/advanced/swipe-view) - Swipe views let you navigate between sibling screens, such as tabs, with a horizontal finger gesture (swipe). This navigation pattern is also referred to as horizontal paging.
- [Firebase Auth](https://firebase.google.com/docs/auth) - Firebase Authentication provides backend services, easy-to-use SDKs, and ready-made UI libraries to authenticate users to your app. It supports authentication using passwords, phone numbers, popular federated identity providers like Google, Facebook and Twitter, and more.
- [Firebase Firestore](https://firebase.google.com/docs/firestore) - Cloud Firestore is a flexible, scalable database for mobile, web, and server development from Firebase and Google Cloud.
- [Datastore](https://developer.android.com/topic/libraries/architecture/datastore) - Jetpack DataStore is a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers. DataStore uses Kotlin coroutines and Flow to store data asynchronously, consistently, and transactionally.
- [Pagination](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - The Paging library helps you load and display pages of data from a larger dataset from local storage or over network.
- [Room](https://developer.android.com/training/data-storage/room) - The room persistence library it is an abstraction layer over SQLite.
- [Coil](https://github.com/coil-kt/coil) - Image loading for Android backed by Kotlin Coroutines.
- [Balloon](https://github.com/skydoves/Balloon) - Modernized and sophisticated tooltips, fully customizable with an arrow and animations on Android.
- [PowerMenu](https://github.com/skydoves/PowerMenu) - PowerMenu is a modernized and fully customizable popup menu, which can be displayed on top of layouts.

## 📸 Screenshots
![Component 1](https://github.com/oguzsout/Wallies/assets/73955284/004ce7ba-c6d6-42e9-9259-de51d4920a4b)

## 📐 Architecture
A well planned architecture is extremely important for an app to scale and all architectures have one common goal- to manage complexity of your app. This isn't something to be worried about in smaller apps however it may prove very useful when working on apps with longer development lifecycle and a bigger team.

Clean architecture was proposed by [Robert C. Martin](https://en.wikipedia.org/wiki/Robert_C._Martin) in 2012 in the [Clean Code Blog](http://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) and it follow the SOLID principle.

<p align="center"><img src="https://user-images.githubusercontent.com/73955284/235376514-a93cdacd-5b50-4f67-ac8d-8cf6456303fc.jpg" alt="Clean Architecture"></p>

The circles represent different layers of your app. Note that:

- The center circle is the most abstract, and the outer circle is the most concrete. This is called the [Abstraction Principle](https://en.wikipedia.org/wiki/Abstraction_principle_computer_programming). The Abstraction Principle specifies that inner circles should contain business logic, and outer circles should contain implementation details.

- Another principle of Clean Architecture is the [Dependency Inversion](https://en.wikipedia.org/wiki/Dependency_inversion_principle). This rule specifies that each circle can depend only on the nearest inward circle ie. low-level modules do not depend on high-level modules but the other way around.

<p align="center"><img src="https://user-images.githubusercontent.com/73955284/235376390-e2ab1adc-0569-4601-9995-ddac08c27a5e.png" alt="Clean Architecture Diagram"></p>

# ✅ Todos
- [ ] Jetpack Compose Migration
- [ ] Unit Tests
- [ ] More Spesific Screens
