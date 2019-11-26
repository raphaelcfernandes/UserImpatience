#include <iostream>
#include <string>
#include "appScripts/chrome.hpp"
#include "appScripts/gmail.hpp"
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
    AdbManager adb;
    if (cmd == "set") {
        // We are setting Uimpatience
        string timeToReadTA, decreaseCpuInterval, decreaseCpuFrequency,
            marginToIncreaseCpu;
        if (argc > 3) {
            timeToReadTA = argv[3];
            decreaseCpuInterval = argv[4];
            decreaseCpuFrequency = argv[5];
            marginToIncreaseCpu = argv[6];
        }
        governor = argv[2];

        cout << "C++/ADB setting " << governor << endl;
        // Governor, timeToreadTA, decreaseCpuInterval,
        // decreaseCpuFrequency,margintoIncrease
        adb.setGovernorInUserImpatienceApp(
            governor, timeToReadTA, decreaseCpuInterval, decreaseCpuFrequency,
            marginToIncreaseCpu);
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
            Generic::getInstance()->createFile(governor, app, iteration);
            std::cout << "C++/ADB going to run " << app << endl;
            adb.tap(adb.appLocationCoordinate, false);
            if (app == "gmail") {
                Gmail g;
                g.gmailScript();
            } else if (app == "chrome") {
                Chrome chrome;
                chrome.chromeScript();
            } else if (app == "spotify") {
                Spotify spotify;
                spotify.spotifyScript();
            } else if (app == "youtube") {
                Youtube youtube;
                youtube.youtubeScript();
            }
            Generic::getInstance()->file.close();
        }
    }
    return 0;
}