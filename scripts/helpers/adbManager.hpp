#ifndef ADBMANAGER_H
#define ADBMANAGER_H

#include <iostream>
#include <string>
#include "responseTime.hpp"

class AdbManager {
    ResponseTime responseTime;
    void setUImpatience(std::string timeToReadTA,
                        std::string decreaseCpuInterval,
                        std::string decreaseCpuFrequency,
                        std::string increaseCpuFrequency);

   public:
    // Mid button responsable to minimize all apps coordinate Nexus 6
    std::string midButtonAndroid = "734 2540";
    // Mid button responsable to minimize all apps coordinate Nexus 5
    // std::string midButtonAndroid = "535 1838";
    // Main menu coordinate on Nexus 6
    std::string mainMenuCoordinate = "745 2237";
    // Main menu coordinate on Nexus 5
    // std::string mainMenuCoordinate = "530 1668";
    // Quick search app in the main menu coordinate on Nexus 6
    std::string quickSearchAppCoordinate = "218 228";
    // Quick search app in the main menu coordinate on Nexus 5
    // std::string quickSearchAppCoordinate = "570 181";
    // Coordinate after search for app on Nexus 6
    std::string appLocationCoordinate = "236 478";
    // Coordinate after search for app on Nexus 5
    // std::string appLocationCoordinate = "168 420";

    AdbManager();
    virtual ~AdbManager();
    void tap(std::string position, bool saveToFile);
    void swipe(std::string from, std::string to, int time, bool saveToFile);
    void typeWithKeyboard(std::string text, bool saveToFile);
    void turnScreenOnAndUnlock();
    // Governor, timeToreadTA, decreaseCpuInterval, decreaseCpuFrequency,
    // marginToIncrease
    void setGovernorInUserImpatienceApp(std::string governor,
                                        std::string timeToReadTA,
                                        std::string decreaseCpuInterval,
                                        std::string decreaseCpuFrequency,
                                        std::string increaseCpuFrequency);
    void sleep(int timeInMs);
    void keyevent(int code, bool saveToFile);
    void closeApp();
    void uimpatienceClompainNotification(bool saveToFile);
};
#endif