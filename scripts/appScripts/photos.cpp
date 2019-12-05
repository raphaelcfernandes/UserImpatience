#include "photos.hpp"

Photos::Photos() {}
Photos::~Photos(){};

void Photos::photosScript(std::string device) {
    AdbManager adb(device);
    if(device == "Nexus 5") {
        adb.tap("134 634",true);
        Generic::getInstance()->sleep(60000);
        adb.keyevent(4, true);
    }
    if (device == "Nexus 6") {
        
    }
    adb.closeApp(device);
}