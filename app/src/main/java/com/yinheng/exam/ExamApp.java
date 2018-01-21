package com.yinheng.exam;

import android.app.Application;

import com.xuhao.android.libsocket.sdk.OkSocket;

/**
 * Created by guohao4 on 2018/1/19.
 * Email: Tornaco@163.com
 */

public class ExamApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OkSocket.initialize(this);
    }
}
