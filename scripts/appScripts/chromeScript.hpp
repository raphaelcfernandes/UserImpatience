#ifndef CHROME_H
#define CHROME_H
#include "../helpers/adbManager.hpp"
#include "../helpers/responseTime.hpp"
#include <iostream>
class Chrome {
    public:
    Chrome();
    virtual ~Chrome();
    void chromeScript();
};
#endif