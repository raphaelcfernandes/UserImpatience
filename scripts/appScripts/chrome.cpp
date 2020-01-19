#include "chrome.hpp"

Chrome::Chrome(){};
Chrome::~Chrome(){};

void Chrome::chromeScript(std::string device) {
    AdbManager::getInstance()->openAppWithShellMonkey("chrome");
    if (device == "Nexus 5") {
        AdbManager::getInstance()->tap("870 170", true);
        AdbManager::getInstance()->tap("950 326", true);
        AdbManager::getInstance()->tap("94 160", true);
        AdbManager::getInstance()->tap("410 753", true);
        AdbManager::getInstance()->typeWithKeyboard("Alan Turing Wikipedia", true);
        AdbManager::getInstance()->keyevent(66, true);
        AdbManager::getInstance()->tap("398 863", true);
        AdbManager::getInstance()->swipe("852 1431", "852 211", 2000, true);
        AdbManager::getInstance()->swipe("971 1641", "971 342", 2000, true);
        AdbManager::getInstance()->swipe("993 1640", "993 500", 500, true);
        AdbManager::getInstance()->swipe("993 1640", "993 500", 500, true);
        AdbManager::getInstance()->swipe("993 1640", "993 500", 200, true);
        AdbManager::getInstance()->swipe("879 1551", "879 76", 5000, true);
        AdbManager::getInstance()->swipe("879 1551", "879 76", 5000, true);
    }
    if (device == "Nexus 6") {
        AdbManager::getInstance()->tap("1193 223", true);
        AdbManager::getInstance()->tap("1292 375", true);
        AdbManager::getInstance()->tap("117 229", true);
        AdbManager::getInstance()->tap("629 877", true);
        AdbManager::getInstance()->typeWithKeyboard("Alan Turing Wikipedia", true);
        AdbManager::getInstance()->keyevent(66, true);
        AdbManager::getInstance()->tap("421 1020", true);
        AdbManager::getInstance()->swipe("880 1452", "880 342", 2000, true);
        AdbManager::getInstance()->swipe("863 1492", "863 342", 2000, true);
        AdbManager::getInstance()->swipe("863 1216", "863 -630", 0, true);
        AdbManager::getInstance()->swipe("863 1216", "863 -630", 0, true);
        AdbManager::getInstance()->swipe("677 1734", "677 240", 5000, true);
        AdbManager::getInstance()->swipe("1051 1691", "1051  588", 1000, true);
        AdbManager::getInstance()->swipe("1037 647", "1037 800", 250, true);
    }
    AdbManager::getInstance()->closeApp(device);
}