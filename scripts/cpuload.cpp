#include <iostream>
#include <string>
#include "appScripts/chrome.hpp"
#include "appScripts/gmail.hpp"
#include "appScripts/photos.hpp"
#include "appScripts/spotify.hpp"
#include "appScripts/youtube.hpp"
#include "helpers/generic.hpp"
#include "helpers/responseTime.hpp"
using namespace std;

int main(int argc, char *argv[]) {
    if (argc < 2) {
        cout << "Number incorrect of parameters" << endl;
        return -1;
    }
    string cmd = argv[1];
    string governor = "";
    string app = "";
    string iteration = "";
    string device = "Nexus 6";
    string timeToReadTA, decreaseCpuInterval, decreaseCpuFrequency,
        increaseCpuF, userImpatienceLevel;
    AdbManager adb(device);
    if (cmd == "set") {
        // We are setting Uimpatience
        if (argc > 3) {
            timeToReadTA = argv[3];
            decreaseCpuInterval = argv[4];
            decreaseCpuFrequency = argv[5];
            increaseCpuF = argv[6];
            userImpatienceLevel = argv[7];
        }
        governor = argv[2];

        cout << "C++/ADB setting " << governor << endl;
        // Governor, timeToreadTA, decreaseCpuInterval,
        // decreaseCpuFrequency,margintoIncrease
        adb.setGovernorInUserImpatienceApp(
            governor, timeToReadTA, decreaseCpuInterval, decreaseCpuFrequency,
            increaseCpuF, userImpatienceLevel, device);
    } else {
        app = argv[2];
        if (cmd == "search") {
            adb.keyevent(3, false);
            adb.tap(adb.mainMenuCoordinate, false);
            adb.tap(adb.quickSearchAppCoordinate, false);
            adb.typeWithKeyboard(app, false);
        }
        if (cmd == "run") {
            governor = argv[3];
            iteration = argv[4];
            timeToReadTA = argv[5];
            decreaseCpuInterval = argv[6];
            decreaseCpuFrequency = argv[7];
            increaseCpuF = argv[8];
            userImpatienceLevel = argv[9];
            if (governor == "userspace") {
                Generic::getInstance()->createFile(
                    governor, app, iteration, device, timeToReadTA,
                    decreaseCpuInterval, decreaseCpuFrequency, increaseCpuF,
                    userImpatienceLevel);
            } else {
                Generic::getInstance()->createFile(governor, app, iteration,
                                                   device);
            }
            std::cout << "C++/ADB going to run " << app << endl;
            adb.tap(adb.appLocationCoordinate, false);
            if (app == "gmail") {
                Gmail g;
                g.gmailScript(device);
            } else if (app == "chrome") {
                Chrome chrome;
                chrome.chromeScript(device);
            } else if (app == "spotify") {
                Spotify spotify;
                spotify.spotifyScript(device);
            } else if (app == "youtube") {
                Youtube youtube;
                youtube.youtubeScript(device);
            } else if (app == "photos") {
                Photos photos;
                photos.photosScript(device);
            }
            Generic::getInstance()->file.close();
        }
    }
    return 0;
}