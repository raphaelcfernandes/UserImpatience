#include "gmail.hpp"

Gmail::Gmail() {}
Gmail::~Gmail() {}

void Gmail::gmailScript() {
  AdbManager adb;
  ResponseTime::calculateResponseTime();
  for (int i = 0; i < 1; i++) {
    // opens sideBar menu
    adb.touchScreenPosition("158 212");
    ResponseTime::calculateResponseTime();
    // Clicks on All Mail
    adb.touchScreenPosition("395 2040");
    ResponseTime::calculateResponseTime();
    // clicks on the first email and 'reads' it
    adb.touchScreenPosition("742 595");
    ResponseTime::calculateResponseTime();
    // Scroll down screen
    adb.swipeScreen("880 1452", "880 342", 2000);
    ResponseTime::calculateResponseTime();
    // Click to return to all mails
    adb.touchScreenPosition("67 212");
    ResponseTime::calculateResponseTime();
    // Clicked to write email
    adb.touchScreenPosition("1303 2230");
    ResponseTime::calculateResponseTime();
    // Any address
    adb.typeWithKeyboard("aseaseasease@gmail.com");
    ResponseTime::calculateResponseTime();
    adb.touchScreenPosition("707 782");
    ResponseTime::calculateResponseTime();
    // Subject
    adb.touchScreenPosition("465 776");
    ResponseTime::calculateResponseTime();
    adb.typeWithKeyboard("This is a test message from UImpatience");
    ResponseTime::calculateResponseTime();
    // Email text
    adb.touchScreenPosition("292 986");
    ResponseTime::calculateResponseTime();
    adb.typeWithKeyboard(
        "Creating a simple e-mail to test UImpatience project. If you are "
        "receiving this email feel blessed about it, you are the chosen on!");
    ResponseTime::calculateResponseTime();
    // Close keyboard
    adb.touchScreenPosition("310 2482");
    ResponseTime::calculateResponseTime();
    // send email
    adb.touchScreenPosition("1213 187");
    ResponseTime::calculateResponseTime();
    // delete last message sent
    adb.swipeScreen("705 514", "705 514", 2500);
    ResponseTime::calculateResponseTime();
    adb.touchScreenPosition("1060 199");
    // opens sideBar menu
    adb.touchScreenPosition("158 212");
    ResponseTime::calculateResponseTime();
    // Clicks on All Mail
    adb.touchScreenPosition("452 411");
    ResponseTime::calculateResponseTime();
  }
  adb.touchScreenPosition("1159 2481");
  adb.swipeScreen("1014 2132", "84 2132", 100);
}