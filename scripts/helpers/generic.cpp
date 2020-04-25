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
	if(userImpatienceLevel <= 0)
	{
	
	}
	else
	{
		int set = 100 / userImpatienceLevel;
		this->userComplaintThreshold = set;
	}

    /*if (userImpatienceLevel == 0) {
        this->userComplaintThreshold = 10;
    } else if (userImpatienceLevel == 1) {
        this->userComplaintThreshold = 30;
    } else if (userImpatienceLevel == 2) {
        this->userComplaintThreshold = 40;
    }
	*/
}

bool Generic::generateRandomNumber() {

	bool result = false;
    int random = std::rand() % 1000 + 1;
    if (random < this->userComplaintThreshold) {
        /*this->userComplaintThreshold =
            (this->userComplaintThreshold * this->userComplaintThreshold) / 100;*/
        result = true;
    }

	
    return result;
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

/*Read performance file into vector*/
void Generic::readPerfData(std::string app)
{
	this->perfFile.open(app + "_perf.txt", std::ios::in);

	
	if(perfFile)	//If file has been opened
	{
		long curr;

		/*Loop through file storing times into vector*/
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

	/*If no data was stored, 0 performance time will be assumed*/
	if(perf_size == 0)
	{
		std::cout << "No data could be loaded for performance data." << std::endl;
		std::cout << "Continued testing will assume '0' speed for performance data." << std::endl;
		std::cout << "Please run setup to create performance data file." << std:: endl;
		
	}	
}

/*Write to performance file*/
void Generic::writePerfFile(long time)
{
	//std::cout << "time: " << time << std::endl;
	this->perfFile << time << "\n";		
	
}


/*Open to write to performance file*/
void Generic::openToWritePerfFile(std::string app)
{
	this->perfFile.open(app + "_perf.txt", std::ios::out);
}

/*Close performance file*/
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

/*Set user wait time*/
void Generic::setUserWaitTime(long time)
{
	userWaitTime = time;
}

/*Return user wait time*/
long Generic::getUserWaitTime()
{
	return userWaitTime;
}

/*Compare performance
Return true if user is impatient*/
bool Generic::comparePerf(long time)
{
	if(!impatience)	//If disabled return false
	{
		return false;
	}
	else if(perf_size > 0)	//If there is performance data
	{
		long check = time - perf_data.at(perf_index);	//Get comparative difference
		//std::cout << "Check: " << check << std::endl;

		perf_index++;	//Increment performance index
		if(perf_index >= perf_size)	//If index exceeds performance size
		{	
			/*Reset to start of performance data*/
			std::cout << "Performance analysis has exceeded number of tasks." << std::endl;
			std::cout << "Resetting performance analysis to first task." << std::endl;
			perf_index = 0;
		}

		if(check > userWaitTime)	//If difference exceeds wait time
		{
			return true;		//Return impatient
		}
		else				//Otherwise
		{
			return false;		//Return not impatient
		}
	}
	else	//If no performance data
	{
		if(time > userWaitTime)  //If time exceeds wait time
		{
			return true;	//Return impatient
		}
		else			//Otherwise
		{	
			return false;	//Return not impatient
		}
		
	}
}


/*Enable impatience*/
void Generic::enableImpatience()
{
	impatience = true;
}

/*Disable Impatience*/
void Generic::disableImpatience()
{
	impatience = false;
}

/*Write frequency to log*/
void Generic::write_frequency()
{
	if(freq_log != "")
	{
		std::string cmd = "adb shell cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq >> " + freq_log;
	popen(cmd.c_str(), "r");
	}
}

/*Set the filename of the cpu frequency log*/
void Generic::set_frequency_log(std::string governor, std::string app,
                    std::string iteration, std::string timeToReadTA, 
		    std::string decreaseCpuInterval,
                    std::string decreaseCpuFrequency,
                    std::string increaseCpuFrequency,
                    std::string userImpatienceLevel)
{
	freq_log =  governor + "_" + app + "_" + iteration + "_" + timeToReadTA + "_" + decreaseCpuInterval + "_" + decreaseCpuFrequency + "_" + increaseCpuFrequency + "_" + userImpatienceLevel + ".txt";
}
