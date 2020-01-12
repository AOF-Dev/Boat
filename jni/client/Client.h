#pragma once

#include <EGL/egl.h>
#include <GLES/gl.h>
#include <android/native_activity.h>
#include <stdlib.h>
#include <stdio.h>
#include <android/log.h>


#include "boat.h"

class Client{

public :
     
    struct boat* mBoat;
	
	int width;
    int height;
	
    EGLDisplay display;
    EGLSurface surface;
    EGLContext context;
	
	ANativeWindow* window;
	
	

public :
    Client();
	~Client();
	
    int initDisplay();
	void teardownDisplay();
	void setWindow(ANativeWindow*);
	bool eglSwapBuffers_func();
	bool eglMakeCurrent_func();
	
	
public :
    static Client* mClient;
	
};

