#!/bin/bash
echo "Executing GMAIL script"

#Wait for it to load (this is not optimal)
# sleep 3
./responseTimeScript.sh
echo $?

#opens sideBar menu
adb shell input tap "158 212"

#Clicks on All Mail
adb shell input tap "395 2040"

sleep 2

#clicks on the first email and 'reads' it
adb shell input tap "742 595"

sleep 2

#Scroll down screen
adb shell input swipe "880 1452" "880 342" 2000

#Clicked to return to all mails
adb shell input tap "67 212"

#Clicked to write email
adb shell input tap "1303 2230"

#Any address
adb shell input text "aseaseasease@gmail.com"
adb shell input tap "707 782"

#Subject
adb shell input tap "465 776"
adb shell input text "This\ is\ a\ test\ message\ from\ UImpatience"

#Email text
adb shell input tap "292 986"
adb shell input text "Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ Lorem\ ipsum.\ "

#close keyboard
adb shell input tap "310 2482"

#send email
adb shell input tap "1213 187"

sleep 3

#delete last message sent
adb shell input swipe "705 514" "705 514" 2500
adb shell input tap "1060 199"
echo "GMAIL script has finished its work"
