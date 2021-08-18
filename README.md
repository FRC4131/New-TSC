# New-TSC
The code for our new T-Shirt Cannon.
dugtator - 8/17/2021 8:45PM
1) Updated the drive command to use a third power scaling of the x-box control command so that hopefully robot won't be as jerky with the controller."
   If this doesn't work we'll want to go to a trapazoidal controller and use a little more sophisticated drive scheme.  
   Might even want to go to field centric!

2) Changed the azimuth rotation to 0.8 from 0.4 to provide more power to the motor.  0.4 is too low and motor stalls out.  
   Might want to make this a scaler and map to the other joystick or a second controller

3) Attempted to update gradle to 2020.1.1. - seems to be successfull.  Still needs to be further updated to 2021.1.2
