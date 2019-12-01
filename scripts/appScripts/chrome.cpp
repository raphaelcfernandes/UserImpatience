#include "chrome.hpp"

Chrome::Chrome(){};
Chrome::~Chrome(){};

void Chrome::chromeScript(std::string device) {
    AdbManager adb(device);
    if (device == "Nexus 5") {
        adb.tap("870 170", true);
        adb.tap("950 326", true);
        adb.tap("94 160", true);
        adb.tap("410 753", true);
        adb.typeWithKeyboard("Alan Turing Wikipedia", true);
        adb.keyevent(66, true);
        adb.tap("398 863", true);
        adb.swipe("852 1431", "852 211", 2000, true);
        adb.swipe("971 1641", "971 342", 2000, true);
        adb.swipe("993 1640", "993 500", 500, true);
        adb.swipe("993 1640", "993 500", 500, true);
        adb.swipe("993 1640", "993 500", 200, true);
        adb.swipe("879 1551", "879 76", 5000, true);
        adb.swipe("879 1551", "879 76", 5000, true);
    }
    if (device == "Nexus 6") {
        adb.tap("1193 223", true);
        adb.tap("1292 375", true);
        adb.tap("117 229", true);
        adb.tap("629 877", true);
        adb.typeWithKeyboard("Alan Turing Wikipedia", true);
        adb.keyevent(66, true);
        adb.tap("421 1020", true);
        adb.swipe("880 1452", "880 342", 2000, true);
        adb.swipe("863 1492", "863 342", 2000, true);
        adb.swipe("863 1216", "863 -630", 0, true);
        adb.swipe("863 1216", "863 -630", 0, true);
        adb.swipe("677 1734", "677 240", 5000, true);
        adb.swipe("1051 1691", "1051  588", 1000, true);
        adb.swipe("1037 647", "1037 800", 250, true);
    }
    adb.closeApp(device);
}