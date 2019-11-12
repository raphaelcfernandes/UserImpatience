#include "chromeScript.hpp"

Chrome::Chrome() {}
Chrome::~Chrome(){};

void Chrome::chromeScript() {
  AdbManager adb;
  ResponseTime responseTime;
  adb.tap("1193 223");
  responseTime.calculateResponseTime();
  adb.tap("1292 375");
  responseTime.calculateResponseTime();
  adb.tap("117 229");
  responseTime.calculateResponseTime();
  adb.tap("629 877");
  responseTime.calculateResponseTime();
  adb.typeWithKeyboard("Alan Turing Wikipedia");
  responseTime.calculateResponseTime();
  adb.keyevent(66);
  responseTime.calculateResponseTime();
  adb.tap("421 1020");
  responseTime.calculateResponseTime();
  adb.swipe("880 1452", "880 342", 2000);
  responseTime.calculateResponseTime();
  adb.swipe("863 1492", "863 342", 2000);
  responseTime.calculateResponseTime();
  adb.swipe("863 1216", "863 -630", 0);
  responseTime.calculateResponseTime();
  adb.swipe("863 1216", "863 -630", 0);
  responseTime.calculateResponseTime();
  adb.swipe("677 1734", "677 240", 5000);
  responseTime.calculateResponseTime();
  adb.swipe("1051 1691", "1051  588", 1000);
  responseTime.calculateResponseTime();
  adb.swipe("1037 647", "1037 800", 250);
  responseTime.calculateResponseTime();
}