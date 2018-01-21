package com.yinheng.exam.hook;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by guohao4 on 2018/1/16.
 * Email: Tornaco@163.com
 */

@Setter
@Getter
public class CoreHook implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    private Context context;

    private final BridgeService bridgeService = new BridgeService();

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if ("android".equals(lpparam.packageName)) {
            hookAMS(lpparam);
        }
    }

    private void onSystemReady() {
        bridgeService.onSystemReady();
    }

    private void hookAMS(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedLog.boot("CoreHook hookAMSStart...");
        try {
            Class ams = XposedHelpers.findClass("com.android.server.am.ActivityManagerService",
                    lpparam.classLoader);
            XposedBridge.hookAllMethods(ams, "start", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Context context = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
                    setContext(context);
                    XposedLog.boot("CoreHook start context: " + context);
                    bridgeService.onStart(context);
                }
            });

            XposedBridge.hookAllMethods(ams, "systemReady", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedLog.boot("systemReady" + context);
                    onSystemReady();
                }
            });
        } catch (Exception e) {
            XposedLog.boot(Log.getStackTraceString(e));
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        XposedBridge.hookAllMethods(XposedHelpers.findClass(TextView.class.getName(), null),
                "setText", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);

                        if (param.args.length != 4) {
                            return;
                        }

                        TextView textView = (TextView) param.thisObject;
                        int id = textView.getId();
                        Resources resources = textView.getResources();
                        String entry = resources.getResourceEntryName(id);

                        // Filt tvMessage for chongdingdahui.
                        if ("com.chongdingdahui.app".equals(AndroidAppHelper.currentPackageName())) {
                            ViewParent view = textView.getParent();
                            if (view == null) return;
                            if (!RelativeLayout.class.getName().equals(view.getClass().getName())) {
                                return;
                            }
                        }

                        XposedLog.debug("set text to : " + entry + ", content: "
                                + Arrays.toString(param.args) + ", from: " + AndroidAppHelper.currentPackageName());

                        if (BridgeManager.get().isServiceAvailable()) {
                            BridgeManager.get().onTextChange(AndroidAppHelper.currentPackageName(),
                                    entry, String.valueOf(param.args[0]));
                        }
                    }
                });
    }
}
