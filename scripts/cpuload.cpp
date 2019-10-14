#include <iostream>
#include <string>
#include <vector>
#include "appScripts/gmail.hpp"
#include "helpers/adbManager.hpp"
#include "helpers/responseTime.hpp"

using namespace std;

int main() {
  // Gmail g;
  // g.gmailScript("ondemand");
  // ResponseTime::calculateResponseTime();

  vector<string> apps = {"gmail", "youtube", "spotify", "chrome"};
  vector<string> governors = {"conservative", "powersave", "interactive",
                              "performance",  "ondemand",  "userspace"};
  // Read interval that userspace background thread will read TA from top in ms
  vector<int> timeToreadTA = {250, 500, 1000};
  // Time to decrease cpu frequency in ms
  // this is the parameter A
  vector<int> decreaseCPUInterval = {1000, 2000, 4000, 8000};
  //(Max - current)/C
  //#Where C is the following measurements
  vector<string> marginToIncreaseCpuFrequency = {"1/2", "1/4", "1/8"};
  // 0 is the user that does not complain at all
  // 5 is the user that complains with high frequency
  vector<int> userImpatienceLevel = {0, 1, 2, 3, 4, 5};
  return 0;
}