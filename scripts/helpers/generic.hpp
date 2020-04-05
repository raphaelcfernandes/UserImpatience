#ifndef GENERIC_H
#define GENERIC_H

#include <chrono>
#include <fstream>
#include <iostream>
#include <string>
#include <thread>
#include <cstdlib>
#include <vector>
#include <ctime>

class Generic {
   private:
    Generic(){};
    static Generic* instance;
    virtual ~Generic(){};
    int userComplaintThreshold;

	long userWaitTime;	//How long user is willing to wait for task beyond expected time
	int perf_index = 0;	//Index of reading performance data
	int perf_size = 0;	//Size of performance data
	std::fstream perfFile;	//Performance file to be read/write
	std::vector<long> perf_data;	//Vector to store performance data
	bool impatience = false;	//Boolean to enable/disable impatience check
	std::string freq_log = "";	//CPU frequency log file name

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
	void write_frequency();
	void set_frequency_log(std::string governor, std::string app,
                    std::string iteration, 
                    std::string timeToReadTA, std::string decreaseCpuInterval,
                    std::string decreaseCpuFrequency,
                    std::string increaseCpuFrequency,
                    std::string userImpatienceLevel);
	
};

#endif
