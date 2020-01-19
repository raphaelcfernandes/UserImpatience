#include "generic.hpp"
#include "adbManager.hpp"

Generic* Generic::instance = NULL;

Generic* Generic::getInstance() {
    if (!instance) {
        instance = new Generic();
        std::srand(std::time(nullptr));
    }
    return instance;
}

int Generic::getuserComplaintThreshold() {
    return this->userComplaintThreshold;
}

void Generic::setUserComplaintThreshold(int userImpatienceLevel) {
    // Less complains
    if (userImpatienceLevel == 0) {
        this->userComplaintThreshold = 10;
    } else if (userImpatienceLevel == 1) {
        this->userComplaintThreshold = 30;
    } else if (userImpatienceLevel == 2) {
        this->userComplaintThreshold = 40;
    }
}

bool Generic::generateRandomNumber() {
    int random = std::rand() / ((RAND_MAX + 1u) / 100);
    if (random < this->userComplaintThreshold) {
        this->userComplaintThreshold =
            (this->userComplaintThreshold * this->userComplaintThreshold) / 100;
        return true;
    }
    return false;
}

std::time_t Generic::getCurrentTimestamp() {
    auto duration = std::chrono::system_clock::now().time_since_epoch();
    auto millis =
        std::chrono::duration_cast<std::chrono::milliseconds>(duration).count();
    return millis;
}

void Generic::sleep(int timeInMs) {
    std::this_thread::sleep_for(std::chrono::milliseconds(timeInMs));
}

std::string Generic::GetStdoutFromCommand(std::string cmd) {
    std::string data;
    FILE* stream;
    const int max_buffer = 256;
    char buffer[max_buffer];
    stream = popen(cmd.c_str(), "r");
    if (stream) {
        while (!feof(stream))
            if (fgets(buffer, max_buffer, stream) != NULL) data.append(buffer);
        pclose(stream);
    }
    return data;
}

void Generic::createFile(std::string governor, std::string app,
                         std::string iteration, std::string device) {
    std::string p = "adbTouchEvents/" + device + '/' + governor + '/' + app +
                    "/" + iteration + ".txt";
    std::cout << p << std::endl;
    this->file.open(p);
}

void Generic::createFile(std::string governor, std::string app,
                         std::string iteration, std::string device,
                         std::string timeToReadTA,
                         std::string decreaseCpuInterval,
                         std::string decreaseCpuFrequency,
                         std::string increaseCpuFrequency,
                         std::string userImpatienceLevel) {
    std::string p =
        "adbTouchEvents/" + device + '/' + governor + '/' + app + "/readTA" +
        timeToReadTA + "_decreaseCpuI" + decreaseCpuInterval + "_decreaseCpuF" +
        decreaseCpuFrequency + "_increaseCpuF" + increaseCpuFrequency +
        "_impatienceLevel" + userImpatienceLevel + "/" + iteration + ".txt";
    std::cout << p << std::endl;
    this->file.open(p);
}

void Generic::writeToFile(std::string event) {
    this->file << event + ", " + std::to_string(getCurrentTimestamp()) + "\n";
}