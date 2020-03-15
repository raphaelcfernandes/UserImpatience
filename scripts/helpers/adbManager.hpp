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
                        std::string increaseCpuFrequency,
                        std::string userImpatienceLevel, std::string device);
    AdbManager(){};
    static AdbManager* instance;
    virtual ~AdbManager(){};
	void checkImpatience();
	

   public:
    std::string device = "";
    std::string governor = "";
    int uimpatienceLevel = -1;
    // Mid button responsable to minimize all apps coordinate
    std::string midButtonAndroid = "";
    // Main menu coordinate on Nexus 6
    std::string mainMenuCoordinate = "";
    // Quick search app in the main menu coordinate
    std::string quickSearchAppCoordinate = "";
    // Coordinate after search for app
    std::string appLocationCoordinate = "";
	bool setup = false;

    static AdbManager* getInstance();
    void tap(std::string position, bool saveToFile);
    void swipe(std::string from, std::string to, int time, bool saveToFile);

    void typeWithKeyboard(std::string text, bool saveToFile);
    void turnScreenOnAndUnlock();
    void typeWholeWord(std::string word);
    // Governor, timeToreadTA, decreaseCpuInterval, decreaseCpuFrequency,
    // marginToIncrease
    void setGovernorInUserImpatienceApp(std::string governor,
                                        std::string timeToReadTA,
                                        std::string decreaseCpuInterval,
                                        std::string decreaseCpuFrequency,
                                        std::string increaseCpuFrequency,
                                        std::string userImpatienceLevel,
                                        std::string device);
    void sleep(int timeInMs);
    void keyevent(int code, bool saveToFile);
    void closeApp(std::string device);
    void uimpatienceClompainNotification(bool saveToFile);
    void openAppWithShellMonkey(std::string app);
    void configureLocationByDevice(std::string device);
	void checkImpatience(long time);
};
#endif
