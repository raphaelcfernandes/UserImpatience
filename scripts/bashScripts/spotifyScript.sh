#!/bin/bash
echo "Executing SPOTIFY script"
for i in 1; do
    adb shell input tap "702 2302"
    ./responseTimeScript.sh
    echo $?
    adb shell input tap "735 759"
    ./responseTimeScript.sh
    # adb shell input text "Andy\ Timmons\ cry\ for\ you"
    adb shell input text "Ozzy\ Osbourne\ Dee"
    ./responseTimeScript.sh
    #6:55 of music
    adb shell input tap "439 391"
    adb shell input tap "764 2091"
    sleep 1m
    # echo "finished music?"
    adb shell input keyevent 4
done
