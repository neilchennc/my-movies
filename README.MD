# My Movies

An Android app sample with Jetpack Compose in MVVM architecture. This app fetches movie data from [The Movie Database](https://www.themoviedb.org/) (a.k.a. [TMDB](https://www.themoviedb.org/)) API and displays movies information.

![Screenshot Overview](images/screenshot_overview.png)

## Features

Browse movies (trending, top-rateed, popular...), posters, backdrops, people's profile, images, and search for movies.

The language of the movie data from the TMDB server depends on your device's settings; therefore, the movie's content will be updated accordingly.

## Build

Before build the project, you have to register [TMDB](https://www.themoviedb.org/) for an [API key](https://www.themoviedb.org/settings/api), and replace with your access token in **app/build.gradle.kts** file.

> *In build.gradle.kts file, Replace **YOUR_TMDB_ACCESS_TOKEN** with yor access token.*

Alternatively, you can download latest apk: [my-movies-v1.0.apk](https://github.com/neilchennc/my-movies/releases/download/v1.0/my-movies-v1.0.apk)

## Architecture

![Architecture](images/my_movies_architecture.png)

## Techniques

- **Jetpack Compose** - Android’s recommended modern toolkit for building native UI.

- **Material Design** - Color and style theming.

- **Hilt** - The dependency injection.

- **Coil** - Load images from the network.

- **Retrofit** - HTTP API requests.

- **Paging library** - Load and display pages of data from a network.

- **Shape** - Custom shapes in compose.

- **Type-Safe Navigation** - Navigating between screens with type-safe.

- **ViewModel** - Part of MVVM architecture. ViewModel is response for keeping data during configuration changes, handling business logic, exposing state to UI, and handling UI events.

- **DataStore** - Store user's preferences.

- **Kotlin coroutines and flows**

- **Timber** - A tiny log library.

- **Zoomable** - Compose multiplatform library that enables the content to be zoomable by gestures.
