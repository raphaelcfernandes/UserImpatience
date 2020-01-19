#include "photos.hpp"
#include <chrono>
Photos::Photos() {}
Photos::~Photos(){};

void Photos::photosScript(std::string device) {
    AdbManager::getInstance()->openAppWithShellMonkey("photos");
    if (device == "Nexus 5") {
        AdbManager::getInstance()->tap("134 634", true);
        Generic::getInstance()->sleep(60000);
        AdbManager::getInstance()->keyevent(4, true);
    }
    if (device == "Nexus 6") {
        auto start = std::chrono::high_resolution_clock::now();
        AdbManager::getInstance()->tap("137 643", true);
        auto stop = std::chrono::high_resolution_clock::now();
        Generic::getInstance()->sleep(
            60000 -
            (std::chrono::duration_cast<std::chrono::milliseconds>(stop - start)
                 .count()));
        AdbManager::getInstance()->keyevent(4, true);
    }
    AdbManager::getInstance()->closeApp(device);
}