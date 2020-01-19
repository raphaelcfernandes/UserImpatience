#include "adbManager.hpp"

AdbManager* AdbManager::instance = NULL;

AdbManager* AdbManager::getInstance() {
    if (!instance) {
        instance = new AdbManager();
    }
    return instance;
}

void AdbManager::configureLocationByDevice(std::string device) {
    this->device = device;
    if (device == "Nexus 5") {
        midButtonAndroid = "535 1838";
        mainMenuCoordinate = "530 1668";
        quickSearchAppCoordinate = "570 181";
        appLocationCoordinate = "168 420";
    } else if (device == "Nexus 6") {
        midButtonAndroid = "734 2540";
        mainMenuCoordinate = "745 2237";
        quickSearchAppCoordinate = "218 228";
        appLocationCoordinate = "236 478";
    }
}

void AdbManager::openAppWithShellMonkey(std::string app) {
    // Package name can be gathered with:
    // adb shell pm list packages -f | grep "spotify/chrome/gm/photos/etc..."
    if (app == "chrome") {
        app = "com.android.chrome";
    } else if (app == "spotify") {
        app = "com.spotify.music";
    } else if (app == "photos") {
        app = "com.google.android.apps.photos";
    } else if (app == "gmail") {
        app = "com.google.android.gm";
    } else if (app == "battery") {
        app = "com.android.battery.saver";
    }
    std::string cmd = "adb shell monkey -p " + app +
                      " -c "
                      "android.intent.category.LAUNCHER 1";
    popen(cmd.c_str(), "r");
    responseTime.calculateResponseTime(this->governor);
}

void AdbManager::closeApp(std::string device) {
    if (device == "Nexus 5") {
        tap("852 1855", true);
        swipe("861 1471", "84 1471", 100, true);
    }
    if (device == "Nexus 6") {
        tap("1159 2481", true);
        swipe("1014 2132", "84 2132", 100, true);
    }
}

void AdbManager::keyevent(int code, bool saveToFile) {
    std::string cmd = "adb shell input keyevent " + std::to_string(code);
    std::cout << cmd << std::endl;
    if (saveToFile) {
        Generic::getInstance()->writeToFile("keyevent");
    }
    popen(cmd.c_str(), "r");
    responseTime.calculateResponseTime(this->governor);
}

void AdbManager::tap(std::string position, bool saveToFile) {
    if (Generic::getInstance()->generateRandomNumber()) {
        uimpatienceClompainNotification(true);
    }
    std::string cmd = "adb shell input tap " + position;
    std::cout << cmd << std::endl;
    popen(cmd.c_str(), "r");
    if (saveToFile) {
        Generic::getInstance()->writeToFile("tap");
    }
    responseTime.calculateResponseTime(this->governor);
}

// Not using tap and swipe because it could lead to a loop where each tap of
// ComplainNotification Could result in more complaining
void AdbManager::uimpatienceClompainNotification(bool saveToFile) {
    std::string swipe = "adb shell input swipe 884 7 884 630 100";
    std::string tap = "adb shell input tap 851 519";
    popen(swipe.c_str(), "r");
    Generic::getInstance()->writeToFile("swipeComplain");
    responseTime.calculateResponseTime(this->governor);
    popen(tap.c_str(), "r");
    Generic::getInstance()->writeToFile("tapComplain");
    responseTime.calculateResponseTime(this->governor);
}

void AdbManager::swipe(std::string from, std::string to, int time,
                       bool saveToFile) {
    if (Generic::getInstance()->generateRandomNumber()) {
        uimpatienceClompainNotification(true);
    }
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
    responseTime.calculateResponseTime(this->governor);
}

void AdbManager::typeWithKeyboard(std::string text, bool saveToFile) {
    if (Generic::getInstance()->generateRandomNumber()) {
        uimpatienceClompainNotification(true);
    }
    std::string cmd = "";
    for (char const& c : text) {
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
        Generic::getInstance()->sleep(600);
        // responseTime.calculateResponseTime();
    }
}

void AdbManager::typeWholeWord(std::string word) {
    std::string cmd = "adb shell input text " + word;
    std::cout << cmd << std::endl;
    popen(cmd.c_str(), "r");
    Generic::getInstance()->sleep(2000);
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
                                std::string increaseCpuFrequency,
                                std::string userImpatienceLevel,
                                std::string device) {
    if (device == "Nexus 6") {
        tap("313 691", false);
        typeWithKeyboard(timeToReadTA, false);
        keyevent(66, false);
        typeWithKeyboard(decreaseCpuInterval, false);
        keyevent(66, false);
        typeWithKeyboard(increaseCpuFrequency, false);
        keyevent(66, false);
        typeWithKeyboard(decreaseCpuFrequency, false);
        keyevent(66, false);
        typeWithKeyboard(userImpatienceLevel, false);
        tap("322 2484", false);
    }
    // tap("394 687", false);
    // typeWithKeyboard(timeToReadTA, false);
    // tap("540 860", false);
    // typeWithKeyboard(decreaseCpuInterval, false);
    // tap("294 1062", false);
    // typeWithKeyboard(increaseCpuFrequency, false);
    // tap("534 1300", false);
    // typeWithKeyboard(increaseCpuFrequency, false);
}

void AdbManager::setGovernorInUserImpatienceApp(
    std::string governor, std::string timeToReadTA = NULL,
    std::string decreaseCpuInterval = NULL,
    std::string decreaseCpuFrequency = NULL,
    std::string increaseCpuFrequency = NULL,
    std::string userImpatienceLevel = NULL, std::string device = NULL) {
    keyevent(3, false);
    // tap(mainMenuCoordinate, false);
    // tap(quickSearchAppCoordinate, false);
    // typeWholeWord("battery");
    // tap(appLocationCoordinate, false);
    openAppWithShellMonkey("battery");
    if (device == "Nexus 5") {
        // Deactivate button
        tap("826 490", false);
        // Config tab
        tap("949 333", false);
        // Dropdown menu
        tap("376 442", false);
        if (governor.compare("conservative") == 0) {
            tap("211 629", false);
        } else if (governor.compare("powersave") == 0) {
            tap("173 1054", false);
        } else if (governor.compare("interactive") == 0) {
            tap("225 776", false);
        } else if (governor.compare("performance") == 0) {
            tap("243 1208", false);
        } else if (governor.compare("ondemand") == 0) {
            tap("186 929", false);
        } else {  // userspace
            tap("185 1605", false);
            setUImpatience(timeToReadTA, decreaseCpuInterval,
                           decreaseCpuFrequency, increaseCpuFrequency,
                           userImpatienceLevel, device);
        }
        // Save button
        tap("128 556", false);
        // Main menu
        tap("127 298", false);
        // Activate
        tap("363 478", false);
    }
    if (device == "Nexus 6") {
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
            setUImpatience(timeToReadTA, decreaseCpuInterval,
                           decreaseCpuFrequency, increaseCpuFrequency,
                           userImpatienceLevel, device);
        }
        if (governor == "userspace") {
            // Save button
            tap("152 1638", false);
        } else {
            tap("146 634", false);
        }
        // Main menu
        tap("124 362", false);
        // Activate
        tap("479 602", false);
    }
    Generic::getInstance()->sleep(4000);
}