package com.mio.boat;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

public class SignatureUtils {
    public static int getSignatureHashCode(Context context){
        Signature signature = getSignature(context);
        int hashCode = signature.hashCode();
        return hashCode;
    }
    public static Signature getSignature(Context argContext) {
        Signature signature = null;
        try {
            String packageName = argContext.getPackageName();
            PackageManager packageManager = argContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName,packageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;
            signature = signatures[0];
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return signature;
    }
}

