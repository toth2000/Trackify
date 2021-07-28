# Trackify

Trackify is an Android App that predicts when a vehicle will require servicing and the type of servicing. This project has won of CAT’s Choice at HackIllinois 2021 hackathon.
<br>
[Check out DevPost](https://devpost.com/software/trackify-gsal28)

## About App

### Inspiration

One of the most important, yet easily unnoticed, aspects about fleet management is preventative care and maintenance. It's not the quality of a vehicle's build that causes a premature demise, but rather improper maintenance. Given a lot of fleet data, our team would analyze it, so we can promote a more pro-active, instead of reactive, approach to maintenance, thus ultimately saving a company money and preventing unnecessary waste.

### What it does

A user can log into the app and check up on their fleet of vehicles. The user can add new vehicle by simply scanning the QR code containing vehicle number .From there, a python script manipulated the data given to find vehicles that need service/maintenance. The app will then tell the user which vehicles need what service, and the user can easily locate what vehicles need service by using augmented reality and location data! It chiefly takes into consideration the work laod over engine to compute the next required service check but also the other two main features are that it will use the latitude longitude data along with timestamp to check for weather log at that day, and also it would leverage the location data to find out terrain of the location. The weather and terrain form two important factor alongside of mileage/fuel efficiency of vehicle these all parameters are taken into consideration to find out when is next maintenance required and what type of maintainance is required. Based on location it suggests the nearest service station at the time of required maintainance.

### Built With

 - Android Studio
 - Firebase
 - Echo Ar
 - PyreBase
 - Python
 - Java
 
## Install

To setup the project on your local machine follow the steps below

    $ git clone https://github.com/toth2000/Trackify.git
    $ cd Trackify
    
Alternatively, you can also use Android Studio to get the project on you local machine
 - Open Android Studio
 - Click `Get from Version Control`
 - In the URL https://github.com/toth2000/Trackify.git
 - Click Clone

## Contribution guidelines

Please refer to our [Contribution Guide](https://github.com/toth2000/Trackify/blob/master/CONTRIBUTING.md) for contributing to this project. And remeber no contribution is small,  every contribution matters.
