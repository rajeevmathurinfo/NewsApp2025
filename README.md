# News App - Jetpack Compose + Clean Architecture

This is a sample Android News App built using **Jetpack Compose**, following **MVVM** and **Clean Architecture** principles. It fetches top headlines using a remote API and supports local caching, pagination, dependency injection, and unit testing.

---

## Features

- Display news headlines in a scrollable list
- Pagination with **Paging 3**
- Offline caching using **Room** and **RemoteMediator**
- View details on a separate screen
- Shared ViewModel between screens
- Modern UI using **Jetpack Compose**
- **Hilt** for Dependency Injection
- **Clean Architecture** with Domain layer
- **Unit tested** Repository, usecase and viewModel

---

## Tech Stack

- **Jetpack Compose**
- **MVVM + Clean Architecture**
- **Paging 3**
- **Room**
- **Retrofit**
- **Hilt**
- **Navigation Compose**
- **Coroutines & Flow**
- **Coil** for image loading
- **MockK** for unit tests


## Best Practices Followed

- **Clean separation of concerns with data, domain, and presentation layers**
- **State management using Kotlin Coroutines and StateFlow**
- **Shared ViewModel between composables**
- **Encoded navigation arguments for safe data transfer**
- **Proper test coverage for data layer and repository**
- **Use of sealed classes, type safety, and idiomatic Kotlin**

## Improvement needed

- **Add full UI tests using androidx.compose.ui.test**
- **Improve error handling and add retry actions**
- **Replace Log.d with Timber**
- **Hide API key using secure storage or environment configs**
- **Add KDoc comments and more documentation**



