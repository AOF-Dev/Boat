#ifndef BOAT_H
#define BOAT_H

#include <android/native_window.h>
#include <android/native_activity.h>
#include <jni.h>
#include <android/log.h>


#define BOAT_KEYBOARD_Kanji                           0xff21

#define BOAT_KEYBOARD_ISO_Left_Tab                    0xfe20

#define BOAT_KEYBOARD_dead_grave                      0xfe50
#define BOAT_KEYBOARD_dead_acute                      0xfe51
#define BOAT_KEYBOARD_dead_circumflex                 0xfe52
#define BOAT_KEYBOARD_dead_tilde                      0xfe53
#define BOAT_KEYBOARD_dead_macron                     0xfe54
#define BOAT_KEYBOARD_dead_breve                      0xfe55
#define BOAT_KEYBOARD_dead_abovedot                   0xfe56
#define BOAT_KEYBOARD_dead_diaeresis                  0xfe57
#define BOAT_KEYBOARD_dead_abovering                  0xfe58
#define BOAT_KEYBOARD_dead_doubleacute                0xfe59
#define BOAT_KEYBOARD_dead_caron                      0xfe5a
#define BOAT_KEYBOARD_dead_cedilla                    0xfe5b
#define BOAT_KEYBOARD_dead_ogonek                     0xfe5c
#define BOAT_KEYBOARD_dead_iota                       0xfe5d
#define BOAT_KEYBOARD_dead_voiced_sound               0xfe5e
#define BOAT_KEYBOARD_dead_semivoiced_sound           0xfe5f
#define BOAT_KEYBOARD_dead_belowdot                   0xfe60
#define BOAT_KEYBOARD_dead_hook                       0xfe61
#define BOAT_KEYBOARD_dead_horn                       0xfe62

#define BOAT_KEYBOARD_BackSpace                       0xff08
#define BOAT_KEYBOARD_Tab                             0xff09
#define BOAT_KEYBOARD_Linefeed                        0xff0a
#define BOAT_KEYBOARD_Clear                           0xff0b
#define BOAT_KEYBOARD_Return                          0xff0d
#define BOAT_KEYBOARD_Pause                           0xff13
#define BOAT_KEYBOARD_Scroll_Lock                     0xff14
#define BOAT_KEYBOARD_Sys_Req                         0xff15
#define BOAT_KEYBOARD_Escape                          0xff1b
#define BOAT_KEYBOARD_Delete                          0xffff

#define BOAT_KEYBOARD_Home                            0xff50
#define BOAT_KEYBOARD_Left                            0xff51
#define BOAT_KEYBOARD_Up                              0xff52
#define BOAT_KEYBOARD_Right                           0xff53
#define BOAT_KEYBOARD_Down                            0xff54
#define BOAT_KEYBOARD_Prior                           0xff55
#define BOAT_KEYBOARD_Page_Up                         0xff55
#define BOAT_KEYBOARD_Next                            0xff56
#define BOAT_KEYBOARD_Page_Down                       0xff56
#define BOAT_KEYBOARD_End                             0xff57
#define BOAT_KEYBOARD_Begin                           0xff58


/* Misc functions */

#define BOAT_KEYBOARD_Select                          0xff60
#define BOAT_KEYBOARD_Print                           0xff61
#define BOAT_KEYBOARD_Execute                         0xff62
#define BOAT_KEYBOARD_Insert                          0xff63
#define BOAT_KEYBOARD_Undo                            0xff65
#define BOAT_KEYBOARD_Redo                            0xff66
#define BOAT_KEYBOARD_Menu                            0xff67
#define BOAT_KEYBOARD_Find                            0xff68
#define BOAT_KEYBOARD_Cancel                          0xff69
#define BOAT_KEYBOARD_Help                            0xff6a
#define BOAT_KEYBOARD_Break                           0xff6b
#define BOAT_KEYBOARD_Mode_switch                     0xff7e
#define BOAT_KEYBOARD_script_switch                   0xff7e
#define BOAT_KEYBOARD_Num_Lock                        0xff7f

/* Keypad functions, keypad numbers cleverly chosen to map to ASCII */

#define BOAT_KEYBOARD_KP_Space                        0xff80
#define BOAT_KEYBOARD_KP_Tab                          0xff89
#define BOAT_KEYBOARD_KP_Enter                        0xff8d
#define BOAT_KEYBOARD_KP_F1                           0xff91
#define BOAT_KEYBOARD_KP_F2                           0xff92
#define BOAT_KEYBOARD_KP_F3                           0xff93
#define BOAT_KEYBOARD_KP_F4                           0xff94
#define BOAT_KEYBOARD_KP_Home                         0xff95
#define BOAT_KEYBOARD_KP_Left                         0xff96
#define BOAT_KEYBOARD_KP_Up                           0xff97
#define BOAT_KEYBOARD_KP_Right                        0xff98
#define BOAT_KEYBOARD_KP_Down                         0xff99
#define BOAT_KEYBOARD_KP_Prior                        0xff9a
#define BOAT_KEYBOARD_KP_Page_Up                      0xff9a
#define BOAT_KEYBOARD_KP_Next                         0xff9b
#define BOAT_KEYBOARD_KP_Page_Down                    0xff9b
#define BOAT_KEYBOARD_KP_End                          0xff9c
#define BOAT_KEYBOARD_KP_Begin                        0xff9d
#define BOAT_KEYBOARD_KP_Insert                       0xff9e
#define BOAT_KEYBOARD_KP_Delete                       0xff9f
#define BOAT_KEYBOARD_KP_Equal                        0xffbd
#define BOAT_KEYBOARD_KP_Multiply                     0xffaa
#define BOAT_KEYBOARD_KP_Add                          0xffab
#define BOAT_KEYBOARD_KP_Separator                    0xffac
#define BOAT_KEYBOARD_KP_Subtract                     0xffad
#define BOAT_KEYBOARD_KP_Decimal                      0xffae
#define BOAT_KEYBOARD_KP_Divide                       0xffaf

#define BOAT_KEYBOARD_KP_0                            0xffb0
#define BOAT_KEYBOARD_KP_1                            0xffb1
#define BOAT_KEYBOARD_KP_2                            0xffb2
#define BOAT_KEYBOARD_KP_3                            0xffb3
#define BOAT_KEYBOARD_KP_4                            0xffb4
#define BOAT_KEYBOARD_KP_5                            0xffb5
#define BOAT_KEYBOARD_KP_6                            0xffb6
#define BOAT_KEYBOARD_KP_7                            0xffb7
#define BOAT_KEYBOARD_KP_8                            0xffb8
#define BOAT_KEYBOARD_KP_9                            0xffb9



/*
 * Auxilliary functions note the duplicate definitions for left and right
 * function keys  Sun keyboards and a few other manufactures have such
 * function key groups on the left and/or right sides of the keyboard.
 * We've not found a keyboard with more than 35 function keys total.
 */

#define BOAT_KEYBOARD_F1                              0xffbe
#define BOAT_KEYBOARD_F2                              0xffbf
#define BOAT_KEYBOARD_F3                              0xffc0
#define BOAT_KEYBOARD_F4                              0xffc1
#define BOAT_KEYBOARD_F5                              0xffc2
#define BOAT_KEYBOARD_F6                              0xffc3
#define BOAT_KEYBOARD_F7                              0xffc4
#define BOAT_KEYBOARD_F8                              0xffc5
#define BOAT_KEYBOARD_F9                              0xffc6
#define BOAT_KEYBOARD_F10                             0xffc7
#define BOAT_KEYBOARD_F11                             0xffc8
#define BOAT_KEYBOARD_L1                              0xffc8
#define BOAT_KEYBOARD_F12                             0xffc9
#define BOAT_KEYBOARD_L2                              0xffc9
#define BOAT_KEYBOARD_F13                             0xffca
#define BOAT_KEYBOARD_L3                              0xffca
#define BOAT_KEYBOARD_F14                             0xffcb
#define BOAT_KEYBOARD_L4                              0xffcb
#define BOAT_KEYBOARD_F15                             0xffcc
#define BOAT_KEYBOARD_L5                              0xffcc
#define BOAT_KEYBOARD_F16                             0xffcd
#define BOAT_KEYBOARD_L6                              0xffcd
#define BOAT_KEYBOARD_F17                             0xffce
#define BOAT_KEYBOARD_L7                              0xffce
#define BOAT_KEYBOARD_F18                             0xffcf
#define BOAT_KEYBOARD_L8                              0xffcf
#define BOAT_KEYBOARD_F19                             0xffd0
#define BOAT_KEYBOARD_L9                              0xffd0
#define BOAT_KEYBOARD_F20                             0xffd1
#define BOAT_KEYBOARD_L10                             0xffd1
#define BOAT_KEYBOARD_F21                             0xffd2
#define BOAT_KEYBOARD_R1                              0xffd2
#define BOAT_KEYBOARD_F22                             0xffd3
#define BOAT_KEYBOARD_R2                              0xffd3
#define BOAT_KEYBOARD_F23                             0xffd4
#define BOAT_KEYBOARD_R3                              0xffd4
#define BOAT_KEYBOARD_F24                             0xffd5
#define BOAT_KEYBOARD_R4                              0xffd5
#define BOAT_KEYBOARD_F25                             0xffd6
#define BOAT_KEYBOARD_R5                              0xffd6
#define BOAT_KEYBOARD_F26                             0xffd7
#define BOAT_KEYBOARD_R6                              0xffd7
#define BOAT_KEYBOARD_F27                             0xffd8
#define BOAT_KEYBOARD_R7                              0xffd8
#define BOAT_KEYBOARD_F28                             0xffd9
#define BOAT_KEYBOARD_R8                              0xffd9
#define BOAT_KEYBOARD_F29                             0xffda
#define BOAT_KEYBOARD_R9                              0xffda
#define BOAT_KEYBOARD_F30                             0xffdb
#define BOAT_KEYBOARD_R10                             0xffdb
#define BOAT_KEYBOARD_F31                             0xffdc
#define BOAT_KEYBOARD_R11                             0xffdc
#define BOAT_KEYBOARD_F32                             0xffdd
#define BOAT_KEYBOARD_R12                             0xffdd
#define BOAT_KEYBOARD_F33                             0xffde
#define BOAT_KEYBOARD_R13                             0xffde
#define BOAT_KEYBOARD_F34                             0xffdf
#define BOAT_KEYBOARD_R14                             0xffdf
#define BOAT_KEYBOARD_F35                             0xffe0
#define BOAT_KEYBOARD_R15                             0xffe0

/* Modifiers */

#define BOAT_KEYBOARD_Shift_L                         0xffe1
#define BOAT_KEYBOARD_Shift_R                         0xffe2
#define BOAT_KEYBOARD_Control_L                       0xffe3
#define BOAT_KEYBOARD_Control_R                       0xffe4
#define BOAT_KEYBOARD_Caps_Lock                       0xffe5
#define BOAT_KEYBOARD_Shift_Lock                      0xffe6

#define BOAT_KEYBOARD_Meta_L                          0xffe7
#define BOAT_KEYBOARD_Meta_R                          0xffe8
#define BOAT_KEYBOARD_Alt_L                           0xffe9
#define BOAT_KEYBOARD_Alt_R                           0xffea
#define BOAT_KEYBOARD_Super_L                         0xffeb
#define BOAT_KEYBOARD_Super_R                         0xffec
#define BOAT_KEYBOARD_Hyper_L                         0xffed
#define BOAT_KEYBOARD_Hyper_R                         0xffee
#define BOAT_KEYBOARD_space                           0x0020
#define BOAT_KEYBOARD_exclam                          0x0021
#define BOAT_KEYBOARD_quotedbl                        0x0022
#define BOAT_KEYBOARD_numbersign                      0x0023
#define BOAT_KEYBOARD_dollar                          0x0024
#define BOAT_KEYBOARD_percent                         0x0025
#define BOAT_KEYBOARD_ampersand                       0x0026
#define BOAT_KEYBOARD_apostrophe                      0x0027
#define BOAT_KEYBOARD_quoteright                      0x0027
#define BOAT_KEYBOARD_parenleft                       0x0028
#define BOAT_KEYBOARD_parenright                      0x0029
#define BOAT_KEYBOARD_asterisk                        0x002a
#define BOAT_KEYBOARD_plus                            0x002b
#define BOAT_KEYBOARD_comma                           0x002c
#define BOAT_KEYBOARD_minus                           0x002d
#define BOAT_KEYBOARD_period                          0x002e
#define BOAT_KEYBOARD_slash                           0x002f

#define BOAT_KEYBOARD_0                               0x0030
#define BOAT_KEYBOARD_1                               0x0031
#define BOAT_KEYBOARD_2                               0x0032
#define BOAT_KEYBOARD_3                               0x0033
#define BOAT_KEYBOARD_4                               0x0034
#define BOAT_KEYBOARD_5                               0x0035
#define BOAT_KEYBOARD_6                               0x0036
#define BOAT_KEYBOARD_7                               0x0037
#define BOAT_KEYBOARD_8                               0x0038
#define BOAT_KEYBOARD_9                               0x0039
#define BOAT_KEYBOARD_colon                           0x003a
#define BOAT_KEYBOARD_semicolon                       0x003b
#define BOAT_KEYBOARD_less                            0x003c
#define BOAT_KEYBOARD_equal                           0x003d
#define BOAT_KEYBOARD_greater                         0x003e
#define BOAT_KEYBOARD_question                        0x003f
#define BOAT_KEYBOARD_at                              0x0040
#define BOAT_KEYBOARD_A                               0x0041
#define BOAT_KEYBOARD_B                               0x0042
#define BOAT_KEYBOARD_C                               0x0043
#define BOAT_KEYBOARD_D                               0x0044
#define BOAT_KEYBOARD_E                               0x0045
#define BOAT_KEYBOARD_F                               0x0046
#define BOAT_KEYBOARD_G                               0x0047
#define BOAT_KEYBOARD_H                               0x0048
#define BOAT_KEYBOARD_I                               0x0049
#define BOAT_KEYBOARD_J                               0x004a
#define BOAT_KEYBOARD_K                               0x004b
#define BOAT_KEYBOARD_L                               0x004c
#define BOAT_KEYBOARD_M                               0x004d
#define BOAT_KEYBOARD_N                               0x004e
#define BOAT_KEYBOARD_O                               0x004f
#define BOAT_KEYBOARD_P                               0x0050
#define BOAT_KEYBOARD_Q                               0x0051
#define BOAT_KEYBOARD_R                               0x0052
#define BOAT_KEYBOARD_S                               0x0053
#define BOAT_KEYBOARD_T                               0x0054
#define BOAT_KEYBOARD_U                               0x0055
#define BOAT_KEYBOARD_V                               0x0056
#define BOAT_KEYBOARD_W                               0x0057
#define BOAT_KEYBOARD_X                               0x0058
#define BOAT_KEYBOARD_Y                               0x0059
#define BOAT_KEYBOARD_Z                               0x005a
#define BOAT_KEYBOARD_bracketleft                     0x005b
#define BOAT_KEYBOARD_backslash                       0x005c
#define BOAT_KEYBOARD_bracketright                    0x005d
#define BOAT_KEYBOARD_asciicircum                     0x005e
#define BOAT_KEYBOARD_underscore                      0x005f
#define BOAT_KEYBOARD_grave                           0x0060
#define BOAT_KEYBOARD_quoteleft                       0x0060
#define BOAT_KEYBOARD_a                               0x0061
#define BOAT_KEYBOARD_b                               0x0062
#define BOAT_KEYBOARD_c                               0x0063
#define BOAT_KEYBOARD_d                               0x0064
#define BOAT_KEYBOARD_e                               0x0065
#define BOAT_KEYBOARD_f                               0x0066
#define BOAT_KEYBOARD_g                               0x0067
#define BOAT_KEYBOARD_h                               0x0068
#define BOAT_KEYBOARD_i                               0x0069
#define BOAT_KEYBOARD_j                               0x006a
#define BOAT_KEYBOARD_k                               0x006b
#define BOAT_KEYBOARD_l                               0x006c
#define BOAT_KEYBOARD_m                               0x006d
#define BOAT_KEYBOARD_n                               0x006e
#define BOAT_KEYBOARD_o                               0x006f
#define BOAT_KEYBOARD_p                               0x0070
#define BOAT_KEYBOARD_q                               0x0071
#define BOAT_KEYBOARD_r                               0x0072
#define BOAT_KEYBOARD_s                               0x0073
#define BOAT_KEYBOARD_t                               0x0074
#define BOAT_KEYBOARD_u                               0x0075
#define BOAT_KEYBOARD_v                               0x0076
#define BOAT_KEYBOARD_w                               0x0077
#define BOAT_KEYBOARD_x                               0x0078
#define BOAT_KEYBOARD_y                               0x0079
#define BOAT_KEYBOARD_z                               0x007a
#define BOAT_KEYBOARD_braceleft                       0x007b
#define BOAT_KEYBOARD_bar                             0x007c
#define BOAT_KEYBOARD_braceright                      0x007d
#define BOAT_KEYBOARD_asciitilde                      0x007e

#define BOAT_KEYBOARD_nobreakspace                    0x00a0
#define BOAT_KEYBOARD_exclamdown                      0x00a1
#define BOAT_KEYBOARD_cent                            0x00a2
#define BOAT_KEYBOARD_sterling                        0x00a3
#define BOAT_KEYBOARD_currency                        0x00a4
#define BOAT_KEYBOARD_yen                             0x00a5
#define BOAT_KEYBOARD_brokenbar                       0x00a6
#define BOAT_KEYBOARD_section                         0x00a7
#define BOAT_KEYBOARD_diaeresis                       0x00a8
#define BOAT_KEYBOARD_copyright                       0x00a9
#define BOAT_KEYBOARD_ordfeminine                     0x00aa
#define BOAT_KEYBOARD_guillemotleft                   0x00ab
#define BOAT_KEYBOARD_notsign                         0x00ac
#define BOAT_KEYBOARD_hyphen                          0x00ad
#define BOAT_KEYBOARD_registered                      0x00ae
#define BOAT_KEYBOARD_macron                          0x00af
#define BOAT_KEYBOARD_degree                          0x00b0
#define BOAT_KEYBOARD_plusminus                       0x00b1
#define BOAT_KEYBOARD_twosuperior                     0x00b2
#define BOAT_KEYBOARD_threesuperior                   0x00b3
#define BOAT_KEYBOARD_acute                           0x00b4
#define BOAT_KEYBOARD_mu                              0x00b5
#define BOAT_KEYBOARD_paragraph                       0x00b6
#define BOAT_KEYBOARD_periodcentered                  0x00b7
#define BOAT_KEYBOARD_cedilla                         0x00b8
#define BOAT_KEYBOARD_onesuperior                     0x00b9
#define BOAT_KEYBOARD_masculine                       0x00ba
#define BOAT_KEYBOARD_guillemotright                  0x00bb
#define BOAT_KEYBOARD_onequarter                      0x00bc
#define BOAT_KEYBOARD_onehalf                         0x00bd
#define BOAT_KEYBOARD_threequarters                   0x00be
#define BOAT_KEYBOARD_questiondown                    0x00bf
#define BOAT_KEYBOARD_Agrave                          0x00c0
#define BOAT_KEYBOARD_Aacute                          0x00c1
#define BOAT_KEYBOARD_Acircumflex                     0x00c2
#define BOAT_KEYBOARD_Atilde                          0x00c3
#define BOAT_KEYBOARD_Adiaeresis                      0x00c4
#define BOAT_KEYBOARD_Aring                           0x00c5
#define BOAT_KEYBOARD_AE                              0x00c6
#define BOAT_KEYBOARD_Ccedilla                        0x00c7
#define BOAT_KEYBOARD_Egrave                          0x00c8
#define BOAT_KEYBOARD_Eacute                          0x00c9
#define BOAT_KEYBOARD_Ecircumflex                     0x00ca
#define BOAT_KEYBOARD_Ediaeresis                      0x00cb
#define BOAT_KEYBOARD_Igrave                          0x00cc
#define BOAT_KEYBOARD_Iacute                          0x00cd
#define BOAT_KEYBOARD_Icircumflex                     0x00ce
#define BOAT_KEYBOARD_Idiaeresis                      0x00cf
#define BOAT_KEYBOARD_ETH                             0x00d0
#define BOAT_KEYBOARD_Eth                             0x00d0
#define BOAT_KEYBOARD_Ntilde                          0x00d1
#define BOAT_KEYBOARD_Ograve                          0x00d2
#define BOAT_KEYBOARD_Oacute                          0x00d3
#define BOAT_KEYBOARD_Ocircumflex                     0x00d4
#define BOAT_KEYBOARD_Otilde                          0x00d5
#define BOAT_KEYBOARD_Odiaeresis                      0x00d6
#define BOAT_KEYBOARD_multiply                        0x00d7
#define BOAT_KEYBOARD_Oslash                          0x00d8
#define BOAT_KEYBOARD_Ooblique                        0x00d8
#define BOAT_KEYBOARD_Ugrave                          0x00d9
#define BOAT_KEYBOARD_Uacute                          0x00da
#define BOAT_KEYBOARD_Ucircumflex                     0x00db
#define BOAT_KEYBOARD_Udiaeresis                      0x00dc
#define BOAT_KEYBOARD_Yacute                          0x00dd
#define BOAT_KEYBOARD_THORN                           0x00de
#define BOAT_KEYBOARD_Thorn                           0x00de
#define BOAT_KEYBOARD_ssharp                          0x00df
#define BOAT_KEYBOARD_agrave                          0x00e0
#define BOAT_KEYBOARD_aacute                          0x00e1
#define BOAT_KEYBOARD_acircumflex                     0x00e2
#define BOAT_KEYBOARD_atilde                          0x00e3
#define BOAT_KEYBOARD_adiaeresis                      0x00e4
#define BOAT_KEYBOARD_aring                           0x00e5
#define BOAT_KEYBOARD_ae                              0x00e6
#define BOAT_KEYBOARD_ccedilla                        0x00e7
#define BOAT_KEYBOARD_egrave                          0x00e8
#define BOAT_KEYBOARD_eacute                          0x00e9
#define BOAT_KEYBOARD_ecircumflex                     0x00ea
#define BOAT_KEYBOARD_ediaeresis                      0x00eb
#define BOAT_KEYBOARD_igrave                          0x00ec
#define BOAT_KEYBOARD_iacute                          0x00ed
#define BOAT_KEYBOARD_icircumflex                     0x00ee
#define BOAT_KEYBOARD_idiaeresis                      0x00ef
#define BOAT_KEYBOARD_eth                             0x00f0
#define BOAT_KEYBOARD_ntilde                          0x00f1
#define BOAT_KEYBOARD_ograve                          0x00f2
#define BOAT_KEYBOARD_oacute                          0x00f3
#define BOAT_KEYBOARD_ocircumflex                     0x00f4
#define BOAT_KEYBOARD_otilde                          0x00f5
#define BOAT_KEYBOARD_odiaeresis                      0x00f6
#define BOAT_KEYBOARD_division                        0x00f7
#define BOAT_KEYBOARD_oslash                          0x00f8
#define BOAT_KEYBOARD_ooblique                        0x00f8
#define BOAT_KEYBOARD_ugrave                          0x00f9
#define BOAT_KEYBOARD_uacute                          0x00fa
#define BOAT_KEYBOARD_ucircumflex                     0x00fb
#define BOAT_KEYBOARD_udiaeresis                      0x00fc
#define BOAT_KEYBOARD_yacute                          0x00fd
#define BOAT_KEYBOARD_thorn                           0x00fe
#define BOAT_KEYBOARD_ydiaeresis                      0x00ff

#define BOAT_KEYBOARD_ISO_Level3_Shift                0xfe03



#define KeyPress              2
#define KeyRelease            3
#define ButtonPress           4
#define ButtonRelease	      5
#define MotionNotify          6

#define Button1               1
#define Button2               2
#define Button3               3
#define Button4               4
#define Button5               5
#define Button6               6
#define Button7               7

#define CursorEnabled         1
#define CursorDisabled        0

typedef int (*BoatEventProcessor)();

typedef struct {
	long long time;
	char type;
    
    char mouse_button;
    int x;
    int y;
    
    int keycode;
    int keychar;
	
} BoatInputEvent;

typedef struct {
	
	ANativeWindow* window;
	void* display;
	
	JavaVM* android_jvm;
	jclass class_BoatInput;
	
	BoatEventProcessor current_event_processor;
	BoatInputEvent current_event;
	
} Boat;

#ifdef BUILD_BOAT
Boat mBoat;
#endif

ANativeWindow* boatGetNativeWindow();
void* boatGetNativeDisplay();
void boatSetCurrentEventProcessor(BoatEventProcessor);
void boatGetCurrentEvent(BoatInputEvent*);
void boatSetCursorMode(int);
//void boatSetCursorPos(int, int);

#endif
