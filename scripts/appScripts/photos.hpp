#ifndef PHOTOS_H
#define PHOTOS_H

#include "../helpers/adbManager.hpp"
#include "../helpers/generic.hpp"
#include "../helpers/responseTime.hpp"

#include <fstream>

class Photos {
   public:
    Photos();
    virtual ~Photos();
    void photosScript(std::string device);
};
#endif