#include "gmail.hpp"

Gmail::Gmail() {}
Gmail::~Gmail() {}

void Gmail::gmailScript(std::string device) {
    AdbManager adb(device);
    if (device == "Nexus 5") {
        // opens sideBar menu
        adb.tap("108 180", true);
        // Clicks on All Mail
        adb.tap("345 1709", true);
        // clicks on the first email and 'reads' it
        adb.tap("492 489", true);
        // Scroll down screen
        adb.swipe("726 1712", "726 550", 2000, true);
        // Click to return to all mails
        adb.keyevent(4, true);
        // Clicked to write email
        adb.tap("956 1643", true);
        // Any address
        adb.typeWithKeyboard("aseaseasease@gmail.com", true);
        // Click to write subject
        adb.tap("260 661", true);
        // Subject
        adb.typeWithKeyboard("This is a test message from UImpatience",true);
        // Email text
        adb.tap("164 857", true);
        adb.typeWithKeyboard("Creating a simple e-mail to test UImpatience project.", true);
        // send email
        adb.tap("872 179", true);
        // delete last message sent
        adb.swipe("591 489", "591 489", 2500, true);
        // Click on trash bin
        adb.tap("734 165", true);
        // opens sideBar menu
        adb.tap("108 180", true);
        // Clicks on primary
        adb.tap("473 353", true);
    }
    if (device == "Nexus 6") {
        // opens sideBar menu
        adb.tap("158 212", true);
        // Clicks on All Mail
        adb.tap("395 2040", true);
        // clicks on the first email and 'reads' it
        adb.tap("742 595", true);
        // Scroll down screen
        adb.swipe("880 1452", "880 342", 2000, true);
        // Click to return to all mails
        adb.keyevent(4, true);
        // Clicked to write email
        adb.tap("1303 2230", true);
        // Any address
        adb.typeWithKeyboard("uimpatience_test@gmail.com", true);
        adb.tap("707 782", true);
        // Subject
        adb.tap("465 776", true);
        adb.typeWithKeyboard("This is a test message from UImpatience", true);
        // Email text
        adb.tap("292 986", true);
        adb.typeWithKeyboard(
            "Creating a simple e-mail to test UImpatience project.", true);
        // Close keyboard
        adb.tap("310 2482", true);
        // send email
        adb.tap("1213 187", true);
        // delete last message sent
        adb.swipe("705 514", "705 514", 2500, true);
        adb.tap("1060 199", true);
        // opens sideBar menu
        adb.tap("158 212", true);
        // Clicks on All Mail
        adb.tap("452 411", true);
    }
    adb.closeApp(device);
}