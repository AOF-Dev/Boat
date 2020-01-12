

#include "Client.h"
Client* Client::mClient = 0;
Client::Client(){
	
	char* value = getenv("BOAT");
	if (value == NULL){
		printf("BOAT not specificed!");
		abort();
	}
	sscanf(value, "%p", &this->mBoat);
	this->mBoat->client = this;
	
	
}
Client::~Client(){
	
}
void Client::setWindow(ANativeWindow* win){
	this->window = win;
}
int Client::initDisplay(){
	
    const EGLint
    attribs[] = {
		EGL_SURFACE_TYPE, 
		EGL_WINDOW_BIT, 
		EGL_BLUE_SIZE, 8, 
		EGL_GREEN_SIZE, 8, 
		EGL_RED_SIZE, 8, 
		EGL_ALPHA_SIZE, 8,  
        EGL_DEPTH_SIZE, 24, //Request depth test buffer.
		EGL_NONE};
    EGLint w;
    EGLint h;
    EGLint dummy;
    EGLint format;
    EGLint numConfigs;
    EGLConfig  config;
    EGLSurface surface;
    EGLContext context;
	EGLDisplay display = eglGetDisplay(EGL_DEFAULT_DISPLAY);
	
    eglInitialize(display, 0, 0);
    eglChooseConfig(display, attribs, &config, 1, &numConfigs );
    eglGetConfigAttrib(display, config, EGL_NATIVE_VISUAL_ID, &format );

    ANativeWindow_setBuffersGeometry(this->window, 0, 0, format );
	
    surface = eglCreateWindowSurface(display, config, this->window, NULL );
    context = eglCreateContext(display, config, NULL, NULL );
	
    if ( EGL_FALSE == eglMakeCurrent(display, surface, surface, context ) )
    {
        return -1;
    }

    eglQuerySurface(display, surface, EGL_WIDTH, &w );
    eglQuerySurface(display, surface, EGL_HEIGHT, &h );

    this->display = display;
    this->context = context;
    this->surface = surface;
    this->width = w;
    this->height = h;
	
	
	/*
    glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
	glViewport(0, 0, w, h);
	glClear(GL_COLOR_BUFFER_BIT);
	eglSwapBuffers( this->display, this->surface );
	*/
    return 0;
}

void Client::teardownDisplay(){
	this->window = 0;
	if ( this->display != EGL_NO_DISPLAY )
    {
        eglMakeCurrent(this->display, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT );
        if ( this->context != EGL_NO_CONTEXT )
        {
            eglDestroyContext(this->display, this->context );
        }

        if ( this->surface != EGL_NO_SURFACE )
        {
            eglDestroySurface( this->display, this->surface );
        }

        eglTerminate( this->display );
    }

    this->display = EGL_NO_DISPLAY;
    this->context = EGL_NO_CONTEXT;
    this->surface = EGL_NO_SURFACE;
}

bool Client::eglSwapBuffers_func(){
	//__android_log_print(ANDROID_LOG_ERROR, "Boat", "eglSwapBuffers");
	return eglSwapBuffers( this->display, this->surface );	
}
bool Client::eglMakeCurrent_func(){
	__android_log_print(ANDROID_LOG_ERROR, "BoatClient", "Try to eglMakeCurrent");
	return eglMakeCurrent(this->display, this->surface, this->surface, this->context );
}

//__android_log_print(ANDROID_LOG_ERROR, "Boat", "Request: %d", this->request);
			

