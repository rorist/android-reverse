#include "hook.h"
#include "dalvik_hook.h"

#define log(...) \
    {FILE *fp = fopen("/data/local/tmp/test.log", "a+");\
    fprintf(fp, __VA_ARGS__);\
    fclose(fp);}

struct dalvik_hook_t h;
static struct dexstuff_t libdhook;

static void my_log(char *msg) {
    log(msg)
}

static jstring hook_func_test(JNIEnv *env, jobject this){
    dalvik_prepare(&libdhook, &h, env);
    return (*env)->NewStringUTF(env, "IM HOOKING");
}

void __attribute__ ((constructor)) my_init(void);
void my_init(void) {

    log("TEST started\n");

    set_logfunction(my_log);
    dalvikhook_set_logfunction(my_log);

    dalvik_hook_setup(
       &h,
       "Lch/fixme/example/MainActivity;",
       "test",
       "()Ljava/lang/String;",
       1,
       hook_func_test
    );

    dalvik_hook(&libdhook, &h);
}

