package com.yinheng.exam.hook;

import android.content.Context;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ServiceManager;

import com.yinheng.xapr.ITextBridgeService;
import com.yinheng.xapr.ITextReceiver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by guohao4 on 2018/1/20.
 * Email: Tornaco@163.com
 */

public class BridgeService extends ITextBridgeService.Stub {

    private final RemoteCallbackList<ITextReceiver> receiverRemoteCallbackList
            = new RemoteCallbackList<>();

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public void onStart(Context context) {
        ServiceManager.addService("user.exam", asBinder());
    }

    public void onSystemReady() {
    }

    @Override
    public void registerTextReceiver(String packageName, String entryName, ITextReceiver receiver)
            throws RemoteException {
        receiverRemoteCallbackList.register(receiver);
    }

    @Override
    public void unRegisterTextReceiver(ITextReceiver receiver) throws RemoteException {
        receiverRemoteCallbackList.unregister(receiver);
    }

    @Override
    public void onTextChange(final String packageName, final String entryName, final String text) throws RemoteException {
        int N = receiverRemoteCallbackList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            final ITextReceiver receiver = receiverRemoteCallbackList.getBroadcastItem(i);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (receiver.matchPackage(packageName) && receiver.matchEntry(entryName))
                            receiver.onTextChanged(text);
                    } catch (Exception e) {
                        XposedLog.wtf(e.toString());
                    }
                }
            });
        }
        receiverRemoteCallbackList.finishBroadcast();
    }
}
