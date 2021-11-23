# ImageGallery

## About the App
A simple image gallery app.Images are coming from this(https://picsum.photos/v2/) api. Images are shown in a list view. User will be able to share the image and download it in local gallery. 

![screenshot](images/gif.gif)

## Architecture of the App
This app used MVVM pattern and Navigation component. For paging functionality, Paging3 library has been used.Room db is used for offline cache.Hilt has been used for dependency injection.

# Features
## Infinite scroll using Paging library
jetpack paging library is used for infinite scroll and paging
https://developer.android.com/topic/libraries/architecture/paging/v3-overview

## Zoom in and Out
image can be zoom in and out

## Download image in local storage

## Share image with other Apps
