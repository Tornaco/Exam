// ITextBridgeService.aidl
package com.yinheng.xapr;

import com.yinheng.xapr.ITextReceiver;

interface ITextBridgeService {
    void registerTextReceiver(String packageName, String entryName, in ITextReceiver receiver);
    void unRegisterTextReceiver(in ITextReceiver receiver);

    void onTextChange(String packageName, String entryName, String text);
}
