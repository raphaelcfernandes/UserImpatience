#!/bin/bash

#Global Variables
APPS=(gmail youtube chrome maps)
governors=(powersave interactive performance ondemand userspace)
#Read interval that userspace background thread will read TA from top
backgroundReadRefreshInterval=()

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
	adb shell input keyevent 26;
	adb shell input keyevent 82;
	#This will turn on screen and unlock
}

mScreenOn=$(adb shell dumpsys power | grep "Display Power"|awk -F'[-=]' '{gsub(/\./,"",$2)}$0=$2')
if [ "$mScreenOn" = "OFF" ]; then
	echo "OFF"
else
	for var in "${APPS[@]}"
	do
		touchScreenPosition $midButtonAndroid
		touchScreenPosition $mainMenuCoordinate
		touchScreenPosition $quickSearchAppCoordinate
		typeWithKeyboard $var
		touchScreenPosition $appLocationCoordinate
		sleep 5
	done
fi

#1-10
# number=$(( ( RANDOM % 10 )  + 1 ))

#ARRAY DECLARATION

# array=( value1 value2 )
# for var in "${array[@]}"
# do
#   echo "${var}"
#   # do something on $var
# done