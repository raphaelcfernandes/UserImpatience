#!/bin/bash
echo "Executing CHROME script"
for i in 1 2 3; do
    adb shell input tap "1193 223"
    adb shell input tap "1292 375"
    adb shell input tap "117 229"
    adb shell input tap "629 877"
    adb shell input text "Alan\ Turing\ Wikipedia"
    adb shell input keyevent 66
    ./responseTimeScript.sh
    echo $?
    adb shell input tap "421 1020"
    adb shell input swipe "880 1452" "880 342" 2000

    adb shell input swipe "863 1492" "863 342" 2000

    adb shell input swipe "863 1216" "863 -630"
    adb shell input swipe "863 1216" "863 -630"
    adb shell input swipe "677  1734" "677  240" 5000

    adb shell input swipe "1051 1691" "1051  588" 1000
    adb shell input swipe "1037 647" "1037 800" 250
done
