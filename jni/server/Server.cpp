#include "Server.h"
#include <stdio.h>

#include <android/log.h>

Server* Server::mServer = 0;
Server::Server(){
    char* value = getenv("BOAT");
    if (value == NULL){
        printf("BOAT not specificed!");
		abort();
    }
    sscanf(value, "%p", &this->mBoat);
	
    this->mBoat->server = this;
}


extern "C"{
bool eglSwapBuffers(){
	return Server::mServer->mBoat->client->eglSwapBuffers_func();
}
bool eglMakeCurrent(){
	return Server::mServer->mBoat->client->eglMakeCurrent_func();
}

int getWindowHeight(){
   return Server::mServer->mBoat->client->height;
}
int getWindowWidth(){
   return Server::mServer->mBoat->client->width;
}


__attribute__((constructor))
void init_server(){
	Server::mServer = new Server();
	__android_log_print(ANDROID_LOG_ERROR, "BoatServer", "Server initialized!");
			
}
}

