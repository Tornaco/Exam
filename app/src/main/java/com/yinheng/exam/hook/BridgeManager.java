package com.yinheng.exam.hook;

import android.os.RemoteException;
import android.os.ServiceManager;

import com.yinheng.xapr.ITextBridgeService;
import com.yinheng.xapr.ITextReceiver;

import lombok.Getter;

/**
 * Created by guohao4 on 2018/1/20.
 * Email: Tornaco@163.com
 */

public class BridgeManager {

    private static final BridgeManager sMe = new BridgeManager();

    @Getter
    private ITextBridgeService service;

    public static BridgeManager get() {
        return sMe;
    }

    private BridgeManager() {
        this.service = ITextBridgeService.Stub.asInterface(ServiceManager.getService("user.exam"));
    }

    public boolean isServiceAvailable() {
        return this.service != null;
    }

    public void registerTextReceiver(String packageName, String entryName, ITextReceiver receiver) throws RemoteException {
        service.registerTextReceiver(packageName, entryName, receiver);
    }

    public void unRegisterTextReceiver(ITextReceiver receiver) throws RemoteException {
        service.unRegisterTextReceiver(receiver);
    }

    public void onTextChange(String packageName, String entryName, String text) throws RemoteException {
        service.onTextChange(packageName, entryName, text);
    }
}
