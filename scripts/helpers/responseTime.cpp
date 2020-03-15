#include "responseTime.hpp"

ResponseTime::ResponseTime() {}
ResponseTime::~ResponseTime() {}

long ResponseTime::calculateResponseTime(std::string governor) {
    int PREV_TOTAL = 0, PREV_IDLE = 0, TOTAL = 100, DIFF_USAGE = 100, cont = 0,
        pos = 0;
    int IDLE = 0, DIFF_IDLE = 0, DIFF_TOTAL = 0, media = 0;
    std::string text = "";

	struct timespec ts;
	timespec_get(&ts, TIME_UTC);
	long start_s = ts.tv_sec;
	long start_n = ts.tv_nsec;

    while (DIFF_USAGE > 5) {
        std::string CPU = Generic::getInstance()->GetStdoutFromCommand(
            "adb shell top -d 1 -n 1 -m 1");
        for (int i = 0; i < CPU.length(); i++) {
            if (CPU[i] == 'U') {
                int j = i;
                std::string text = "";
                for (j; CPU[j] != '%'; j++) {
                    if (isdigit(CPU[j])) text += CPU[j];
                }
                DIFF_USAGE = stoi(text);
                break;
            }
        }
        if (DIFF_USAGE <= 20 && (governor == "powersave" || governor == "userspace")) {
            break;
        }
    }

	timespec_get(&ts, TIME_UTC);
	long secs = ts.tv_sec - start_s;
	long convToNano = secs * pow(10, 9);
	long result = (ts.tv_nsec - start_n) + convToNano;
	std::cout << "seconds: " << secs << std::endl;
	std::cout << "nanoseconds: " << result << std::endl;
	

    return secs;
}
