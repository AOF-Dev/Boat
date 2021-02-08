#include <string.h>
#include <jni.h>
#include <fstream>
using namespace std;
extern "C" {
    static int isInit=0;//用于判断是否已初始化
    JNIEXPORT jstring JNICALL Java_com_mio_boat_MioLauncher_getText(JNIEnv* env, jobject thiz,jstring filepath) {
    const char *fp = env->GetStringUTFChars(filepath, 0);
    ifstream infile;
    infile.open(fp);
    string s;
    string txt;
    while(getline(infile,s)){
        txt+=s+"\n";
    }
    if(!isInit){//未初始化则执行死循环无限空指针将软件卡崩溃
        while(true){
                    int* p = NULL;
                    *p=3;
                    while(true){
                    int* pa= NULL;
                    *pa=3;
                    while(true){
                        int* pb= NULL;
                        *pb=3;
                    }
                    while(true){
                        int* pc= NULL;
                        *pc=3;
                    }
                    }
                }
    }
    return env->NewStringUTF(txt.data());
}
//注意更改方法的命名
    JNIEXPORT jboolean JNICALL Java_com_mio_boat_MioLauncher_init(JNIEnv* env, jobject thiz,jstring pw,jobject context) {
        const char *cpw = env->GetStringUTFChars(pw, 0);
        char *mio="-2033952260";//这是签名的哈希值，自行更改
        char* app_packageName="com.mio.boat";//自行更换为自己的包名
            jint app_signature_hash_code=-2033952260;
            //Context的类
            jclass context_clazz=env->GetObjectClass(context);
            //得到getPackageManager方法的ID
            jmethodID methodID_getPackageManager = env->GetMethodID(
                                    context_clazz,"getPackageManager",
                                    "()Landroid/content/pm/PackageManager;");
            //获得PackageManager对象
            jobject packageManager = env->CallObjectMethod(context,methodID_getPackageManager);
            // //获得PackageManager类
            jclass packageManager_clazz=env->GetObjectClass(packageManager);
            //得到getPackageInfo方法的ID
            jmethodID methodID_getPackageInfo=env->GetMethodID(packageManager_clazz,"getPackageInfo",
                                    "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
            // //得到getPackageName方法的ID
            jmethodID methodID_getPackageName = env->GetMethodID(context_clazz,"getPackageName","()Ljava/lang/String;");
            //获得当前应用的包名
            jstring application_package = (jstring)env->CallObjectMethod(context,methodID_getPackageName);
            const char* package_name = env->GetStringUTFChars(application_package,0);
            //获得PackageInfo
            jobject packageInfo = env->CallObjectMethod(packageManager,methodID_getPackageInfo,application_package,64);
            jclass packageinfo_clazz = env->GetObjectClass(packageInfo);
            //获取签名
            jfieldID fieldID_signatures = env->GetFieldID(packageinfo_clazz,"signatures","[Landroid/content/pm/Signature;");
            jobjectArray signature_arr = (jobjectArray)env->GetObjectField(packageInfo,fieldID_signatures);
            //Signature数组中取出第一个元素
            jobject signature = env->GetObjectArrayElement(signature_arr,0);
            //读signature的hashcode
            jclass signature_clazz = env->GetObjectClass(signature);
            jmethodID methodID_hashcode = env->GetMethodID(signature_clazz,"hashCode","()I");
            jint hashCode = env->CallIntMethod(signature,methodID_hashcode);
            if(strcmp(package_name,app_packageName) != 0) {
                while(true){
                    int* p = NULL;
                    *p=3;
                    while(true){
                    int* pa= NULL;
                    *pa=3;
                    while(true){
                        int* pb= NULL;
                        *pb=3;
                    }
                    while(true){
                        int* pc= NULL;
                        *pc=3;
                    }
                    }
                }
            }
            if(hashCode != app_signature_hash_code) {
                while(true){
                    int* p = NULL;
                    *p=3;
                    while(true){
                    int* pa= NULL;
                    *pa=3;
                    while(true){
                        int* pb= NULL;
                        *pb=3;
                    }
                    while(true){
                        int* pc= NULL;
                        *pc=3;
                    }
                    }
                }
            }
            jclass mt=env->FindClass("bin/mt/apksignaturekillerplus/HookApplication");
            if(mt != NULL){
                while(true){
                    int* p = NULL;
                    *p=3;
                    while(true){
                    int* pa= NULL;
                    *pa=3;
                    while(true){
                        int* pb= NULL;
                        *pb=3;
                    }
                    while(true){
                        int* pc= NULL;
                        *pc=3;
                    }
                    }
                }
            }
        if(strcmp(cpw,mio)==0) {
            isInit=1;
            return true;
        } else {
            return false;
        }
	}

}

