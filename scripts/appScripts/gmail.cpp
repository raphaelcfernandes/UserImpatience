#include "gmail.hpp"

Gmail::Gmail() {}
Gmail::~Gmail() {}

void Gmail::gmailScript(std::string governor) {
  AdbManager adb;
  std::cout << "Executing GMAIL script." << std::endl;
  ResponseTime::calculateResponseTime();
  // Generic::getCurrentTimestamp();
  // Generic::GetStdoutFromCommand("adb shell dumpsys battery | grep 'level' |
  // tr -dc '0-9'");
  // std::ofstream outfile("testResults/gmail_" + governor + ".csv");
  // outfile.close();
  // # echo "start battery level: $batteryLevel" > gmail_$1.txt
  // # echo "action,response_time_ms,start,end" >> gmail_$1.txt
  // # echo "startApp,$response,$start,$end" >> gmail_$1.txt
  for (int i = 0; i < 3; i++) {
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
    adb.typeWithKeyboard("Creating a simple e-mail to test UImpatience project. If you are receiving this email feel blessed about it, you are the chosen on!");
    ResponseTime::calculateResponseTime();
    // Close keyboard
    adb.touchScreenPosition("310 2482");
    ResponseTime::calculateResponseTime();
    // send email
    adb.touchScreenPosition("1213 187");
    ResponseTime::calculateResponseTime();
    //delete last message sent
    adb.swipeScreen("705 514", "705 514", 2500);
    ResponseTime::calculateResponseTime();
    adb.touchScreenPosition("1060 199");
    std::cout<< "GMAIL script has finished its work"<<std::endl;
    
  }
}

// start=$(date +%s%N | cut -b1-13)
// ./responseTimeScript.sh
// # response=$?
// # end=$(date +%s%N | cut -b1-13)
// # echo "Executing GMAIL script"