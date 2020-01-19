#include "youtube.hpp"

Youtube::Youtube() {}
Youtube::~Youtube(){};

void Youtube::youtubeScript(std::string device) {
    if (device == "Nexus 5") {
        AdbManager::getInstance()->tap("978 1685", true);
        // Search Button
        AdbManager::getInstance()->tap("846 146", true);
        AdbManager::getInstance()->typeWithKeyboard("ozzy osbourne dee", true);
        // Press enter to search
        AdbManager::getInstance()->keyevent(66, true);
        AdbManager::getInstance()->tap("328 440", true);
        Generic::getInstance()->sleep(50000);
        AdbManager::getInstance()->swipe("491 321", "983 1460", 200, true);
        AdbManager::getInstance()->tap("998 1500", true);
    }
    if (device == "Nexus 6") {
        // Search Button
        AdbManager::getInstance()->tap("1182 167", true);
        AdbManager::getInstance()->typeWithKeyboard("ozzy osbourne dee", true);
        // Press enter to search
        AdbManager::getInstance()->keyevent(66, true);
        AdbManager::getInstance()->tap("310 512", true);
        Generic::getInstance()->sleep(50000);
        AdbManager::getInstance()->swipe("935 491", "1094 1731", 0, true);
        AdbManager::getInstance()->tap("1324 2090", true);
    }
    AdbManager::getInstance()->closeApp(device);
}