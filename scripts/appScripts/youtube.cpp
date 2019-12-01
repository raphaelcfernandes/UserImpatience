#include "youtube.hpp"

Youtube::Youtube() {}
Youtube::~Youtube(){};

void Youtube::youtubeScript(std::string device) {
    AdbManager adb(device);
    if(device == "Nexus 5") {
        // Search Button
        adb.tap("846 146", true);
        adb.typeWithKeyboard("ozzy osbourne dee", true);
        // Press enter to search
        adb.keyevent(66, true);
        adb.tap("328 440", true);
        Generic::getInstance()->sleep(50000);
        adb.swipe("491 321", "983 1460", 200, true);
        adb.tap("998 1500", true);
    }
    if (device == "Nexus 6") {
        // Search Button
        adb.tap("1182 167", true);
        adb.typeWithKeyboard("ozzy osbourne dee", true);
        // Press enter to search
        adb.keyevent(66, true);
        adb.tap("310 512", true);
        Generic::getInstance()->sleep(50000);
        adb.swipe("935 491", "1094 1731", 0, true);
        adb.tap("1324 2090", true);
    }
    adb.closeApp(device);
}