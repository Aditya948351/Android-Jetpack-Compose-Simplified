# To-Do App with Jetpack Compose

## Introduction
This To-Do App is a modern Android application built from the ground up using Kotlin and Jetpack Compose. The application demonstrates the use of the latest tools and libraries in Android development to create a fully functional, reactive user experience.

## Features
- **Jetpack Compose**: An entirely declarative UI toolkit for intuitive user interface design.
- **ROOM Database**: Robust local persistence with type-safe querying.
- **Compose Navigation**: Simple and powerful navigation handling within the Compose ecosystem.
- **ViewModel**: Manage UI-related data in a lifecycle-conscious way.
- **Dagger-Hilt**: Dependency injection with minimal boilerplate and maximum scalability.
- **Preferences DataStore**: A modern way to store user preferences asynchronously.
- **Dark and Light Theme Support**: User-friendly theme support that respects system settings.
- **Animations**: Enhance the user experience with smooth and responsive animations.
- **Clean Architecture**: A maintainable and testable architecture that separates concerns.

## Architecture
This application embraces the MVVM (Model-View-ViewModel) architectural pattern, leveraging the Android Architecture Components for robust, production-quality apps. 

### MVVM Pattern
The MVVM pattern reduces the amount of boilerplate code and separates concerns for better testability and maintenance. ViewModels are used to abstract the business logic and data handling away from the UI, which is defined entirely using Jetpack Compose.

### Used Architecture Components and Libraries:
- **ViewModel**: Holds UI data across configuration changes and is lifecycle-aware.
- **LiveData**: Observable data holder class that respects the lifecycle of app components.
- **Lifecycle**: Perform actions in response to a change in the lifecycle status of another component.
- **ROOM**: Abstract layer over SQLite to allow for more robust database access.
- **Navigation Component**: Handles navigation logic in a concise and consistent manner.
- **DataStore**: A data storage solution that allows for asynchronous, consistent, and transactional data storage.
- **Coroutines & Flow**: For asynchronous programming, making the code more readable and easier to understand.

