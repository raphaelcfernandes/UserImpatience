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

void Generic::readPerfData(std::string app)
{

	this->perfFile.open(app + "_perf.txt", std::ios::in);

	if(perfFile)
	{
		long curr;
		while(true)
		{
			this->perfFile >> curr;
			if(this->perfFile.eof())
			{
				break;
			}
			this->perf_data.push_back(curr);
			this->perf_size++;
		}
		this->perfFile.close();
	}

	if(perf_size == 0)
	{
		std::cout << "No data could be loaded for performance data." << std::endl;
		std::cout << "Continued testing will assume '0' speed for performance data." << std::endl;
		std::cout << "Please run setup to create performance data file." << std:: endl;
		
	}	
}

void Generic::writePerfFile(long time)
{
	//std::cout << "time: " << time << std::endl;
	this->perfFile << time << "\n";		
	
}

void Generic::openToWritePerfFile(std::string app)
{
	this->perfFile.open(app + "_perf.txt", std::ios::out);
}

void Generic::closePerfFile()
{
	if(this->perfFile)
	{
		this->perfFile.close();
	}
}



void Generic::writeToFile(std::string event) {
	//std::cout << event << std::endl;
    this->file << event + ", " + std::to_string(getCurrentTimestamp()) + "\n";
}

void Generic::setUserWaitTime(long time)
{
	userWaitTime = time;
}

long Generic::getUserWaitTime()
{
	return userWaitTime;
}

bool Generic::comparePerf(long time)
{
	if(!impatience)
	{
		return false;
	}
	else if(perf_size > 0)
	{
		long check = time - perf_data.at(perf_index);
		//std::cout << "Check: " << check << std::endl;
		perf_index++;
		if(perf_index >= perf_size)
		{
			std::cout << "Performance analysis has exceeded number of tasks." << std::endl;
			std::cout << "Resetting performance analysis to first task." << std::endl;
			perf_index = 0;
		}

		if(check > userWaitTime)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	else
	{
		if(time > userWaitTime)
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
}

void Generic::enableImpatience()
{
	impatience = true;
}

void Generic::disableImpatience()
{
	impatience = false;
}
