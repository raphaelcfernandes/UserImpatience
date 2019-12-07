#include "photos.hpp"
#include <chrono>
Photos::Photos() {}
Photos::~Photos(){};

void Photos::photosScript(std::string device) {
    AdbManager adb(device);
    if (device == "Nexus 5") {
        adb.tap("134 634", true);
        Generic::getInstance()->sleep(60000);
        adb.keyevent(4, true);
    }
    if (device == "Nexus 6") {
        auto start = std::chrono::high_resolution_clock::now();
        adb.tap("137 643", true);
        auto stop = std::chrono::high_resolution_clock::now();
        Generic::getInstance()->sleep(60000 - (std::chrono::duration_cast<std::chrono::milliseconds>(stop - start).count()));
        adb.keyevent(4, true);
    }
    adb.closeApp(device);
}