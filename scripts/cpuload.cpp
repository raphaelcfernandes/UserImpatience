#include <iostream>
#include <string>
#include "appScripts/chrome.hpp"
#include "appScripts/gmail.hpp"
#include "appScripts/spotify.hpp"
#include "appScripts/youtube.hpp"
#include "helpers/generic.hpp"

using namespace std;

int main(int argc, char* argv[]) {
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
    governor = argv[2];
    cout << "C++/ADB setting " << governor << endl;
    adb.setGovernorInUserImpatienceApp(governor);
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
      } else if(app == "spotify") {
        Spotify spotify;
        spotify.spotifyScript();
      } else if(app == "youtube") {
        Youtube youtube;
        youtube.youtubeScript();
      }
      Generic::getInstance()->file.close();
    }
  }
  // vector<string> apps = {"gmail", "youtube", "spotify", "chrome"};
  // vector<string> governors = {"conservative", "powersave", "interactive",
  // "performance",  "ondemand",  "userspace"};
  // Read interval that userspace background thread will read TA from top in
  // ms vector<int> timeToreadTA = {250, 500, 1000}; Time to decrease cpu
  // frequency in ms this is the parameter A vector<int> decreaseCPUInterval =
  // {1000, 2000, 4000, 8000};
  //(Max - current)/C
  //#Where C is the following measurements
  // vector<string> marginToIncreaseCpuFrequency = {"1/2", "1/4", "1/8"};
  // 0 is the user that does not complain at all
  // 5 is the user that complains with high frequency
  // vector<int> userImpatienceLevel = {0, 1, 2, 3, 4, 5};
  return 0;
}

// #Global Variables
// APPS=(gmail)
// # governors=(conservative powersave interactive performance ondemand userspace)
// governors=(ondemand userspace)
// #Read interval that userspace background thread will read TA from top in ms
// timeToReadTA=(250 500 1000)

// #Time to decrease cpu frequency in ms
// #this is the parameter A
// decreaseCPUInterval=(1000 2000 4000 8000)

// #Amount of frequency to reduce after A.time has passed
// decreaseCpuFrequency=(1 2 4 8)

// #(Max - current)/C
// #Where C is the following measurements
// marginToIncreaseCpuFrequency=(1/2 1/4 1/8)

// #0 is the user that does not complain at all
// #5 is the user that complains with high frequency
// userImpatienceLevel=(0 1 2 3 4 5)

// mScreenOn=$(adb shell dumpsys power | grep "Display Power" | awk -F'[-=]' '{gsub(/\./,"",$2)}$0=$2')
// if [ "$mScreenOn" = "OFF" ]; then
//   echo "OFF"
// fi
// #https://rosettacode.org/wiki/Linux_CPU_utilization#UNIX_Shell
// #https://github.com/Leo-G/DevopsWiki/wiki/How-Linux-CPU-Usage-Time-and-Percentage-is-calculated

