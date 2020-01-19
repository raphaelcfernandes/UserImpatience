#include <cstdlib>
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
    AdbManager::getInstance()->configureLocationByDevice(device);
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
        AdbManager::getInstance()->governor = governor;
        cout << "C++/ADB setting " << governor << endl;
        AdbManager::getInstance()->setGovernorInUserImpatienceApp(
            governor, timeToReadTA, decreaseCpuInterval,
            decreaseCpuFrequency, increaseCpuF, userImpatienceLevel, device);
    } else {
        app = argv[2];
        governor = argv[3];
        AdbManager::getInstance()->governor = governor;
        if (cmd == "search") {
            AdbManager::getInstance()->keyevent(3, false);
            // AdbManager::getInstance()->openAppWithShellMonkey(app);
            std::cout << "going to sleep 2s after keyeven3" << std::endl;
            Generic::getInstance()->sleep(2000);
        }
        if (cmd == "run") {
            iteration = argv[4];
            AdbManager::getInstance()->governor = governor;
            if (governor == "userspace") {
                timeToReadTA = argv[5];
                decreaseCpuInterval = argv[6];
                decreaseCpuFrequency = argv[7];
                increaseCpuF = argv[8];
                userImpatienceLevel = argv[9];
                AdbManager::getInstance()->uimpatienceLevel =
                stoi(userImpatienceLevel);
                Generic::getInstance()->setUserComplaintThreshold(AdbManager::getInstance()->uimpatienceLevel);
                Generic::getInstance()->createFile(
                    governor, app, iteration, device, timeToReadTA,
                    decreaseCpuInterval, decreaseCpuFrequency, increaseCpuF,
                    userImpatienceLevel);
            } else {
                Generic::getInstance()->createFile(governor, app, iteration,
                                                   device);
            }
            std::cout << "C++/ADB going to run " << app << endl;
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