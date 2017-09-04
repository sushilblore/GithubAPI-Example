# GithubAPI-Example

This is a demo app to demonstrate the use of Github api - All the repos of Jake Wharton is fetched and shown as a list.

# App Features

The app can work both in Online and Offline mode:

# Online :
- On app launch, it fetches 15 items at a time from Jake Whatron repository and shows in a list (CardView). When user scrolls the list (RecyclerView), it fetches another 15 items. Till the items are fetched, it shows the progress bar at the bottom of the page.
- Everytime data is read from network, it is also stored in local database (used Realm)

- When the app is online, it always fetches the data from network to get the latest data.

# Edge cases with online scenarios

- As the user is scrolling the list, it network goes down, a proper error message is shown at the screen botton in place of progress bar with a "ReEntry" button. When network is back, tapping on "ReEntry" button starts to fetch data again.

# Offline
- On first time app launch (the local database (Realm) is empty), if there is no internet, it shows proper error message on screen and a "ReEntry" button. Once the network is back, tapping on "ReEntry" button starts to fetch the data again.

- If it is not first time launch and local database already has some data, then on app launch, it fetches all the stored data from local database and shows it in the list.

# App Design

# MVP
The app is based on MVP architecture (Model, View, Presenter) which is better than MVC when it comes to coupling. MVP makes the view code way cleaner than when using MVC, since the views, activities and the fragments will only have the code related to rendering the customizing the UI and no interactions with the controllers. MVP (Model View Presenter) is a pattern thats allows separate the presentation layer from the logic, so that everything about how the interface works is separated from how we represent it on screen.

# Dagger 2
The app uses Dependency Injection pattern using Dagger 2. Ths is used to inject the network service in the fragment which can then use it in its presenter class to fetch data from network. You can see more details about Dagger in http://square.github.io/dagger/

# Retrofit
The app uses Retrofit as REST client. Retrofit is a REST Client for Android and Java. It makes it relatively easy to retrieve and upload JSON. Retrofit uses the OkHttp library for HTTP requests.

# RXJava
The app also uses RxJava for making asynchronous event calls in a clean way. RxJava is a Java VM implementation of Reactive Extensions: a library for composing asynchronous and event-based programs by using observable sequences.

# Realm
The app uses Realm for local storage of the data. Everytime data is fetched from the server, it is stored in Realm. When the app works in offline mode, it fetches data from Realm (local storage) and shows it to the user.

# RecyclerView and CardView
This app uses RecyclerView and CardView(Gridlayout) to show the items.

# Prerequisites
- JDK 1.8
- Android SDK.
- Supports Jelly Bean (API 16) - Android N (API 25) .
- Latest Android SDK Tools and build tools.

# Tests

# Unit Tests
The app has few local unit tests in the file LocalDatabaseHelperTest.java

# Instrumentation Test
The app has few instrumentation tests in the file NoNetworkInstrumentedTest

# Authors

Sushil Kumar Jha





