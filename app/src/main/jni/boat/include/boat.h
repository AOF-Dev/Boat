#ifndef BOAT_H
#define BOAT_H

#include <android/native_window.h>

ANativeWindow* boatGetNativeWindow();
void* boatGetNativeDisplay();
void boatSetCurrentEventProcessor(BoatEventProcessor);
void boatGetCurrentEvent(BoatInputEvent*);
void boatGetCurrentEvent(BoatInputEvent*);
int boatGetEventPipeFd();
void boatSetCursorMode(int);
void boatSetCursorPos(int, int);
void boatSetPrimaryClipString(const char*);
const char* boatGetPrimaryClipString();

#endif

