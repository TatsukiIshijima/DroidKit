# DroidKit

[![](https://jitpack.io/v/TatsukiIshijima/DroidKit.svg)](https://jitpack.io/#TatsukiIshijima/DroidKit)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## What's this?
This is a BLE library for Droid Inventor Kit app development that simplifies the code to run the Droid Inventor Kit. It is based on the Swift version of [Droid Kit](https://github.com/crane-hiromu/DroidKit). Thanks to @hcrane.

### Do you know **'Droid Inventor Kit'** ?

![littlebits-box-1-1024x686](https://user-images.githubusercontent.com/24838521/190877953-560dd403-e13f-4e0d-a3af-90ebd0cf1393.jpeg)

ref : [‘TURN EVERYONE INTO AN INVENTOR’: THE STORY OF THE LITTLEBITS DROID INVENTOR KIT](https://www.starwars.com/news/turn-everyone-into-an-inventor-the-story-of-the-littlebits-droid-inventor-kit)

**'Droid Inventor Kit'** is a kit to create a droid.
In addition, the droid can be operated with simple programming from a smartphone.
However, there is a problem.
Currently, [the app has been removed from the store and cannot be installed](https://community.sphero.com/t/droid-inventor-kit-app-gone-from-play-app-store/2783).

## Preparation
Before use, connect the device to each port on the w32 control hub of the Droid Inventor Kit.
Connect o11 Servo to port 2. Connect DC motor to port 3.

- w32 control hub port2 -> o11 Servo
- w32 control hub port3 -> o25 dc motor

## Install
1. In `settings.gradle`, add `maven { url 'https://jitpack.io }`.

### Groovy
```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### Kotlin
```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven ("https://jitpack.io")
    }
}
```

2. In `build.gradle`(app level), add `implementation`
### Groovy
```
dependencies {
    ...
    implementation 'com.github.TatsukiIshijima:DroidKit:$version'
}
```

### Kotlin
```
dependencies {
    ...
    implementation ("com.github.TatsukiIshijima:DroidKit:$version")
}
```

## How to Use
### Instance
```kotlin
  val bluetoothAdapter: BluetoothAdapter = (application.applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
  val droidScanner: DroidScanner = DroidScannerImpl(bluetoothAdapter)
  val droidConnector: DroidConnector = DroidConnectorImpl(application)
  val droidOperator: DroidOperator = DroidOperatorImpl(droidConnector)
```

### Connect
```kotlin
CoroutineScope.launch {
  droidScanner.startScan()
  val droidDevice = droidScanner.deivce
  droidConnector.connect(droidDevice)
}
```

### Disconnect
```kotlin
CoroutineScope.launch {
  droidConnector.disconnect()
}
```

### Go
```kotlin
CoroutineScope.launch {
  droidOperator.go(0.5f)
}
```

### Back
```kotlin
CoroutineScope.launch {
  droidOperator.back(0.5f)
}
```

### Stop
```kotlin
CoroutineScope.launch {
  droidOperator.stop()
}
```

### Turn
```kotlin
CoroutineScope.launch {
  droidOperator.turn(90f)
}
```

### EndTurn
```kotlin
CoroutineScope.launch {
  droidOperator.endTurn()
}
```

### ChangeLED
```kotlin
CoroutineScope.launch {
  droidOperator.changeLEDColor(
    red = 255,
    green = 255,
    blue = 255,
  )
}
```

### PlaySound
```kotlin
CoroutineScope.launch {
  droidOperator.playSound(DroidCommand.PlaySound.S0)
}
```

## Sample App ScreenShot
| App | Color Picker | Sound Menu |
|:--:|:---:|:---:|
| <img src="https://user-images.githubusercontent.com/17661705/232210097-83f46862-92c9-4663-96ef-02720425743e.png" width="300"/> | <img src="https://user-images.githubusercontent.com/17661705/232210103-f87565e3-e7e7-4412-8ced-f76f2f051200.png" width="300"/> | <img src="https://user-images.githubusercontent.com/17661705/232210104-a73b6684-e96f-40bf-8a7d-7e2c34eaaea0.png" width="300"/> |