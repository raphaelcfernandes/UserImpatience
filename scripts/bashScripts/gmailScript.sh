#!/bin/bash
start=$(date +%s%N | cut -b1-13)
./responseTimeScript.sh
# response=$?
# end=$(date +%s%N | cut -b1-13)
# echo "Executing GMAIL script"

# touch gmail_$1.txt
# batteryLevel=$(adb shell dumpsys battery | grep "level" | tr -dc '0-9')
# echo "start battery level: $batteryLevel" > gmail_$1.txt 
# echo "action,response_time_ms,start,end" >> gmail_$1.txt
# echo "startApp,$response,$start,$end" >> gmail_$1.txt

# for i in 1; do
    
#     #opens sideBar menu
#     adb shell input tap "158 212"

#     #Clicks on All Mail
#     adb shell input tap "395 2040"

#     ./responseTimeScript.sh
#     echo $?

#     #clicks on the first email and 'reads' it
#     adb shell input tap "742 595"

#     ./responseTimeScript.sh
#     echo $?

#     #Scroll down screen
#     adb shell input swipe "880 1452" "880 342" 2000

#     #Clicked to return to all mails
#     adb shell input tap "67 212"

#     #Clicked to write email
#     adb shell input tap "1303 2230"

#     #Any address
#     adb shell input text "aseaseasease@gmail.com"
#     adb shell input tap "707 782"

#     #Subject
#     adb shell input tap "465 776"
#     adb shell input text "This\ is\ a\ test\ message\ from\ UImpatience"

#     #Email text
#     adb shell input tap "292 986"
#     adb shell input text "Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ "

#     #close keyboard
#     adb shell input tap "310 2482"

#     #send email
#     adb shell input tap "1213 187"

#     ./responseTimeScript.sh
#     echo $?

#     #delete last message sent
#     adb shell input swipe "705 514" "705 514" 2500
#     adb shell input tap "1060 199"
#     echo "GMAIL script has finished its work"
# done
