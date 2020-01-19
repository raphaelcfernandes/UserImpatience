#ifndef GENERIC_H
#define GENERIC_H

#include <chrono>
#include <fstream>
#include <iostream>
#include <string>
#include <thread>
#include <cstdlib>

class Generic {
   private:
    Generic(){};
    static Generic* instance;
    virtual ~Generic(){};
    int userComplaintThreshold;

   public:
    std::ofstream file;
    static Generic* getInstance();
    std::time_t getCurrentTimestamp();
    std::string GetStdoutFromCommand(std::string cmd);
    void createFile(std::string governor, std::string app,
                    std::string iteration, std::string device);
    void createFile(std::string governor, std::string app,
                    std::string iteration, std::string device,
                    std::string timeToReadTA, std::string decreaseCpuInterval,
                    std::string decreaseCpuFrequency,
                    std::string increaseCpuFrequency,
                    std::string userImpatienceLevel);
    void writeToFile(std::string event);
    void sleep(int timeInMs);
    bool generateRandomNumber();
    void setUserComplaintThreshold(int userImpatienceLevel);
    int getuserComplaintThreshold();
};

#endif
