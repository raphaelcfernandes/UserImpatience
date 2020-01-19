#ifndef CHROME_H
#define CHROME_H

#include "../helpers/adbManager.hpp"
#include "../helpers/responseTime.hpp"

class Chrome {
   public:
    Chrome();
    virtual ~Chrome();
    void chromeScript(std::string device);
};
#endif