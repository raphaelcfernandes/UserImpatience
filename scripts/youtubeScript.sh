#!/bin/bash
echo "Executing YOUTUBE script"
x=$(date +%s%N | cut -b1-13)
./responseTimeScript.sh
y=$(date +%s%N | cut -b1-13)

ar=(a night to remember launch cinematic - the witcher III: wild hunt)
# echo "youtube,$x,$y,interactive" >> tests.csv

#Search Button
# adb shell input tap "1182 167"

# for var in "${ar[@]}"; do
#     adb shell input text "$var"
#     adb shell input text "\ "
#     sleep 0.02
# done

# #Press enter to search
# adb shell input keyevent 66

# sleep 1

# adb shell input tap "310 512"

# sleep 285 

# adb shell input swipe "935 491" "1094 1731"

# adb shell input tap "1324 2090"