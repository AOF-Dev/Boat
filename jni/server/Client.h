#pragma once

#include "boat.h"

class Client{

public :
     
    struct boat* mBoat;
    int width;
    int height;
public :
    bool eglSwapBuffers_func();
    bool eglMakeCurrent_func();
};
