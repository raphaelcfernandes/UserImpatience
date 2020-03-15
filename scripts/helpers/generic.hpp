#ifndef GENERIC_H
#define GENERIC_H

#include <chrono>
#include <fstream>
#include <iostream>
#include <string>
#include <thread>
#include <cstdlib>
#include <vector>

class Generic {
   private:
    Generic(){};
    static Generic* instance;
    virtual ~Generic(){};
    int userComplaintThreshold;
	long userWaitTime;
	int perf_index = 0;
	int perf_size = 0;
	std::fstream perfFile;
	std::vector<long> perf_data;
	bool impatience = false;

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

	void setUserWaitTime(long time);
	long getUserWaitTime();
	void readPerfData(std::string app);
	void writePerfFile(long time);
	void openToWritePerfFile(std::string app);
	void closePerfFile();
	bool comparePerf(long time);
	void enableImpatience();
	void disableImpatience();
	
};

#endif
