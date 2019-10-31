#include "chromeScript.hpp"

Chrome::Chrome() {}
Chrome::~Chrome(){};

void Chrome::chromeScript() {
  std::cout << "Going to execute chrome script" << std::endl;
  AdbManager adb;
  adb.tap("1193 223");
  ResponseTime::calculateResponseTime();
  adb.tap("1292 375");
  ResponseTime::calculateResponseTime();
  adb.tap("117 229");
  ResponseTime::calculateResponseTime();
  adb.tap("629 877");
  ResponseTime::calculateResponseTime();
  adb.typeWithKeyboard("Alan Turing Wikipedia");
  ResponseTime::calculateResponseTime();
  adb.keyevent(66);
  ResponseTime::calculateResponseTime();
  adb.tap("421 1020");
  ResponseTime::calculateResponseTime();
  adb.swipe("880 1452", "880 342", 2000);
  ResponseTime::calculateResponseTime();
  adb.swipe("863 1492", "863 342", 2000);
  ResponseTime::calculateResponseTime();
  adb.swipe("863 1216", "863 -630", 0);
  ResponseTime::calculateResponseTime();
  adb.swipe("863 1216", "863 -630", 0);
  ResponseTime::calculateResponseTime();
  adb.swipe("677 1734", "677 240", 5000);
  ResponseTime::calculateResponseTime();
  adb.swipe("1051 1691", "1051  588", 1000);
  ResponseTime::calculateResponseTime();
  adb.swipe("1037 647", "1037 800", 250);
  ResponseTime::calculateResponseTime();
}