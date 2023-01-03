# What is Jukebox

A lighweight android library that helps you focusing on your audio UI and manages independently 
media player under the hood.

It’s built on top of jetpack compose and media player.

The focus is on flexibility to change loading, error and audio views.

# Setup

## Gradle dependencies

``` 
maven { url 'https://jitpack.io' }
```

```
implementation("com.github.LouisDuboscq:Jukebox:0.3.1")
```

# What can I do with Jukebox

## Launch the Jukebox composable with the minimal parameters and get ready to use slider and audio player.

![jukebox simple](assets/jukebox_simple.png)

![slider](assets/slider.png)

## Customize audio view : 

![jukebox custom](assets/jukebox_custom.png)
 
![circular and actions](assets/circular_and_actions.png)

![custom_style](assets/custom_style.png)

## Customize loading view : 

![loading](assets/loading.png)

## Customize error view :

![custom_error](assets/custom_error.png)
![error_404](assets/error_404.png)

## Call play and pause, the commands understood by Jukebox

![commands](assets/commands.png)

## Set play when ready

By default, Jukebox does not play your audio when the media player has finished preparing it.
You can force him to play when it's ready :

```
Jukebox(
    uri = uri,
    playWhenReady = true,
    commands = commands.receiveAsFlow()
)
```

## Sample 

[There's a sample to show you how to use Jukebox:](examples/src/main/java/com/lduboscq/jukeboxe/examples/JukeboxSample.kt)

![sample](assets/sample.mov)
