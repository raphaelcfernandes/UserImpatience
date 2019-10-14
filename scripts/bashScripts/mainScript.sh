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
APPS=(gmail)
# governors=(conservative powersave interactive performance ondemand userspace)
governors=(ondemand userspace)
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

setGovernorInUserImpatienceApp() {
  adb shell input keyevent 3
  touchScreenPosition $mainMenuCoordinate
  touchScreenPosition $quickSearchAppCoordinate
  typeWithKeyboard "Battery"
  touchScreenPosition $appLocationCoordinate
  #Deactivate button
  adb shell input tap "1045 570"
  adb shell input tap "1222 345"
  sleep 1
  #Dropdown menu
  adb shell input tap "423 528"
  if [ "$1" = "conservative" ]; then
    adb shell input tap "271 755"
  elif [ "$1" = "powersave" ]; then
    adb shell input tap "186 1249"
  elif [ "$1" = "interactive" ]; then
    adb shell input tap "134 913"
  elif [ "$1" = "performance" ]; then
    adb shell input tap "147 1417"
  elif [ "$1" = "ondemand" ]; then
    adb shell input tap "203 1090"
  elif [ "$1" = "userspace" ]; then
    adb shell input tap "185 1605"
  fi
}

mScreenOn=$(adb shell dumpsys power | grep "Display Power" | awk -F'[-=]' '{gsub(/\./,"",$2)}$0=$2')
if [ "$mScreenOn" = "OFF" ]; then
  echo "OFF"
else
  for gov in "${governors[@]}"; do
    setGovernorInUserImpatienceApp "$gov"
    sleep 1
    if [ "$gov" = "userspace" ]; then
      echo "if"
      # for readTA in "${timeToReadTA[@]}"; do
      #   for decreaseCPUinterval in "${decreaseCPUInterval[@]}"; do
      #     for decreaseCPUfreq in "${decreaseCpuFrequency[@]}"; do
      #       for marginToIncreaseCpu in "${marginToIncreaseCpuFrequency[@]}"; do
      #         for uimpatienceLevel in "${userImpatienceLevel[@]}"; do
      #           echo "$readTA" "$decreaseCPUinterval" "$decreaseCPUfreq" "$marginToIncreaseCpu" "$uimpatienceLevel"
      #           adb shell input tap "599 683"
      #           typeWithKeyboard "$readTA"
      #           adb shell input tap "423 898"
      #           typeWithKeyboard "$decreaseCPUinterval"
      #           adb shell input tap "333 1081"
      #           typeWithKeyboard "$marginToIncreaseCpu"
      #           adb shell input tap "169 1262"
      #           typeWithKeyboard "$decreaseCPUfreq"
      #           adb shell input keyevent 4
      #           #Save button
      #           adb shell input tap "213 1459"
      #           sleep 0.2
      #         done
      #       done
      #     done
      #   done
      # done
    else
      echo "$gov"
      #Save button
      adb shell input tap "140 652"
      # Main menu
      adb shell input tap "124 362"
      #Activate
      adb shell input tap "479 602"
      ./responseTimeScript.sh
      for var in "${APPS[@]}"; do
        #3 = KEYCODE_HOME
        adb shell input keyevent 3
        touchScreenPosition $mainMenuCoordinate
        touchScreenPosition $quickSearchAppCoordinate
        typeWithKeyboard $var
        touchScreenPosition $appLocationCoordinate
        if [ "$var" = "gmail" ]; then
          ./gmailScript.sh $gov
        elif [ "$var" = "youtube" ]; then
          ./youtubeScript.sh
        elif [ "$var" = "chrome" ]; then
          ./chromeScript.sh
        elif [ "$var" = "spotify" ]; then
          ./spotifyScript.sh
        fi
        adb shell input tap "1159 2481"
        adb shell input swipe "1014 2132" "84 2132"
      done
    fi
  done
fi
