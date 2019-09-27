#!/bin/bash

#Y increases according to bottom of screen
#X increases according to right border of screen

#Mid button responsable to minimize all apps coordinate
midButtonAndroid="734 2540"

#Main menu coordinate on Nexus 6
mainMenuCoordinate="745 2237"

#Quick search app in the main menu coordinate on Nexus 6
quickSearchAppCoordinate="218 228"

#Coordinate after search for app
appLocationCoordinate="236 478"

touchScreenPosition() {
  adb shell input tap $1 $2
}

typeWithKeyboard() {
  adb shell input text $1
}

turnScreenOnAndUnlock() {
  #This will turn on screen and unlock
  adb shell input keyevent 26
  adb shell input keyevent 82
  #This will turn on screen and unlock
}
#Global Variables
APPS=(youtube)
# APPS=(gmail)
governors=(conservative powersave interactive performance ondemand userspace)

#Read interval that userspace background thread will read TA from top in ms
timeToReadTA=(250 500 1000)

#Time to decrease cpu frequency in ms
#this is the parameter A
decreaseCPUInterval=(1000 2000 4000 8000)

#Amount of frequency to reduce after A.time has passed
decreaseCpuFrequency=(1 2 4 8)

#(Max - current)/C
#Where C is the following measurements
marginToIncreaseCpuFrequency=(1/2 1/4 1/8)

#0 is the user that does not complain at all
#5 is the user that complains with high frequency
userImpatienceLevel=(0 1 2 3 4 5)


mScreenOn=$(adb shell dumpsys power | grep "Display Power" | awk -F'[-=]' '{gsub(/\./,"",$2)}$0=$2')
if [ "$mScreenOn" = "OFF" ]; then
  echo "OFF"
else
  for var in "${APPS[@]}"; do
    #3 = KEYCODE_HOME
    adb shell input keyevent 3
    touchScreenPosition $mainMenuCoordinate
    touchScreenPosition $quickSearchAppCoordinate
    typeWithKeyboard $var
    touchScreenPosition $appLocationCoordinate
    if [ "$var" = "gmail" ]; then
      ./gmailScript.sh
    elif [ "$var" = "youtube" ]; then
      ./youtubeScript.sh
    elif [ "$var" = "chrome" ]; then
      ./chromeScript.sh
    fi
    sleep 5
  done
fi