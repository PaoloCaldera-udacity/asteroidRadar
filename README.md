# Asteroid Radar

Asteroid Radar is the second project of the Android Kotlin Developer nanodegree provided by Udacity. It is a test project for the first chapter of the course, Developing Android Apps with Kotlin. The project focuses on displaying asteroid data loaded from the internet and saved into an offline cache, by exploiting Kotlin coroutines. Moreover, it focuses on designing an app for everyone, taking into account RTL support and Talkback. In addition to the ones listed in [Shoe Store](https://github.com/PaoloCaldera/shoeStore), the project demonstrates the ability to exploit the following components and libraries:

* [Retrofit](https://square.github.io/retrofit/), for HTTP REST interactions
* [Moshi](https://github.com/square/moshi), for converting JSON responses to Kotlin classes
* [Room](https://developer.android.com/jetpack/androidx/releases/room), for saving data locally
* [WorkManager](https://developer.android.com/guide/background/persistent), for starting background jobs
* [Talkback](https://support.google.com/accessibility/android/topic/3529932?hl=en&ref_topic=9078845&sjid=7074746708030549321-EU), for the cell phone to talk back to the user if he or she cannot see what is happening

The project consists of just two screens, the first one representing a list of asteroids whose path is close to the Earth planet, and the second one provides details of an asteroid selected from the list.

Visit the [Wiki](https://github.com/PaoloCaldera/asteroidRadar/wiki) to see the application screens.


## Getting Started
To clone the repository, use the command
```
$ git clone https://github.com/PaoloCaldera/asteroidRadar.git
```
or the `Get from VCS` option inside Android Studio by copying the link above.

Then, run the application on an Android device or emulator. The application is compiled with API 33, thus use a device or emulator supporting such API version.
For complete usage of the application, be sure that the device or emulator is connected to a Wi-Fi network.


## License

Asteroid Radar is a public project that can be downloaded and modified under the terms and conditions of the [MIT License](LICENSE).
