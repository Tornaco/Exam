package com.yinheng.exam;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

/**
 * Created by guohao4 on 2018/1/20.
 * Email: Tornaco@163.com
 */

public class AccessService extends AccessibilityService {

    @SuppressWarnings("ConstantConditions")
    public static boolean activated(Context context) {
        for (AccessibilityServiceInfo id : ((AccessibilityManager)
                context.getSystemService(ACCESSIBILITY_SERVICE))
                .getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)) {
            if (id.getId().contains(AccessService.class.getSimpleName())) {
                return true;
            }
        }
        return false;

    }

    private static final String CHONGDINGDAHUI_MESSAGE_VIEW_ID = "tvMessage";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo == null) return;
        Log.w("Nick", "id= " + event.getClassName() + ", " + event.getText());
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w("Nick", "Access create");
        Toast.makeText(this, "ACCESS OK", Toast.LENGTH_LONG).show();
    }
}
