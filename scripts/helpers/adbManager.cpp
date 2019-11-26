#include "adbManager.hpp"

AdbManager::AdbManager(){};
AdbManager::~AdbManager(){};

void AdbManager::closeApp() {
    tap("1159 2481", true);
    swipe("1014 2132", "84 2132", 100, true);
}

void AdbManager::tap(std::string position, bool saveToFile) {
    std::string cmd = "adb shell input tap " + position;
    if (saveToFile) {
        Generic::getInstance()->writeToFile("tap");
    }
    std::cout << cmd << std::endl;
    popen(cmd.c_str(), "r");
    responseTime.calculateResponseTime();
}

void AdbManager::swipe(std::string from, std::string to, int time,
                       bool saveToFile) {
    std::string cmd = "";
    if (time != 0) {
        cmd = "adb shell input swipe " + from + " " + to + " " +
              std::to_string(time);
    } else {
        cmd = "adb shell input swipe " + from + " " + to;
    }
    std::cout << cmd << std::endl;
    if (saveToFile) {
        Generic::getInstance()->writeToFile("swipe");
    }
    popen(cmd.c_str(), "r");
    responseTime.calculateResponseTime();
}

void AdbManager::typeWithKeyboard(std::string text, bool saveToFile) {
    std::string cmd = "";
    for (char const &c : text) {
        if (isspace(c)) {
            cmd = "adb shell input keyevent 62";
        } else {
            cmd = "adb shell input text ";
            cmd += c;
        }
        if (saveToFile) {
            Generic::getInstance()->writeToFile("typeKeyboard");
        }
        std::cout << "typing " << c << std::endl;
        popen(cmd.c_str(), "r");
        responseTime.calculateResponseTime();
    }
}

void AdbManager::turnScreenOnAndUnlock() {
    std::string cmd = "adb shell input keyevent 26";
    popen(cmd.c_str(), "r");
    Generic::getInstance()->sleep(1000);
    cmd = "adb shell input keyevent 82";
    popen(cmd.c_str(), "r");
}

void AdbManager::setUImpatience(std::string timeToReadTA,
                                std::string decreaseCpuInterval,
                                std::string decreaseCpuFrequency,
                                std::string increaseCpuFrequency) {
    tap("394 687", false);
    typeWithKeyboard(timeToReadTA, false);
    tap("540 860", false);
    typeWithKeyboard(decreaseCpuInterval, false);
    tap("294 1062", false);
    typeWithKeyboard(increaseCpuFrequency, false);
    tap("534 1300", false);
    typeWithKeyboard(increaseCpuFrequency, false);
}

void AdbManager::setGovernorInUserImpatienceApp(
    std::string governor, std::string timeToReadTA = NULL,
    std::string decreaseCpuInterval = NULL,
    std::string decreaseCpuFrequency = NULL,
    std::string increaseCpuFrequency = NULL) {
    keyevent(3, false);
    tap(mainMenuCoordinate, false);
    tap(quickSearchAppCoordinate, false);
    typeWithKeyboard("battery", false);
    tap(appLocationCoordinate, false);
    // Deactivate button
    tap("1045 570", false);
    // Config tab
    tap("1222 345", false);
    // Dropdown menu
    tap("423 528", false);
    if (governor.compare("conservative") == 0) {
        tap("271 755", false);
    } else if (governor.compare("powersave") == 0) {
        tap("186 1249", false);
    } else if (governor.compare("interactive") == 0) {
        tap("134 913", false);
    } else if (governor.compare("performance") == 0) {
        tap("147 1417", false);
    } else if (governor.compare("ondemand") == 0) {
        tap("203 1090", false);
    } else {  // userspace
        tap("185 1605", false);
        setUImpatience(timeToReadTA, decreaseCpuInterval, decreaseCpuFrequency,
                       increaseCpuFrequency);
    }
    // Save button
    tap("140 652", false);
    // Main menu
    tap("124 362", false);
    // Activate
    tap("479 602", false);
    Generic::getInstance()->sleep(4000);
}

void AdbManager::keyevent(int code, bool saveToFile) {
    std::string cmd = "adb shell input keyevent " + std::to_string(code);
    std::cout << cmd << std::endl;
    if (saveToFile) {
        Generic::getInstance()->writeToFile("keyevent");
    }
    popen(cmd.c_str(), "r");
    responseTime.calculateResponseTime();
}