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
    // Mid button responsable to minimize all apps coordinate 
    std::string midButtonAndroid = "";
    // Main menu coordinate on Nexus 6
    std::string mainMenuCoordinate = "";
    // Quick search app in the main menu coordinate
    std::string quickSearchAppCoordinate = "";
    // Coordinate after search for app  
    std::string appLocationCoordinate = "";

    AdbManager(std::string device);
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
    void closeApp(std::string device);
    void uimpatienceClompainNotification(bool saveToFile);
};
#endif