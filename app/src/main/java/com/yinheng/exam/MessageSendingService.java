package com.yinheng.exam;

import android.annotation.Nullable;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.xuhao.android.libsocket.sdk.ConnectionInfo;
import com.xuhao.android.libsocket.sdk.OkSocket;
import com.xuhao.android.libsocket.sdk.OkSocketOptions;
import com.xuhao.android.libsocket.sdk.SocketActionAdapter;
import com.xuhao.android.libsocket.sdk.bean.ISendable;
import com.xuhao.android.libsocket.sdk.connection.IConnectionManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by guohao4 on 2018/1/19.
 * Email: Tornaco@163.com
 */

public class MessageSendingService extends Service {

    enum State {
        IDLE,
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        DISCONNECTED
    }

    private State mState;

    private Handler mHandler;

    private IConnectionManager mConnectionanager;

    private ExecutorService mExe = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if ("tornaco.connect".equals(intent.getAction())) {
            String ip = intent.getStringExtra("ip");
            int port = intent.getIntExtra("port", -1);
            if (ip != null && port > 0) {
                connect(ip, port);
            }
        } else if ("tornaco.send".equals(intent.getAction())) {
            sendMessage(intent.getStringExtra("msg"));
        }
        return START_STICKY;
    }

    void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    void connect(final String ip, final int port) {
        disconnect();
        initConnection(ip, port);
    }

    void disconnect() {
        if (mConnectionanager != null) {
            try {
                if (mConnectionanager.isConnect()) mConnectionanager.disConnect();
            } catch (Throwable ignored) {

            }
        }
    }


    void sendMessage(final String message) {

        if (mConnectionanager != null && mConnectionanager.isConnect()) {
            mConnectionanager.send(new ISendable() {
                @Override
                public byte[] parse() {
                    return message.getBytes();
                }
            });
        }
    }

    void initConnection(String ip, int port) {
        setState(State.CONNECTING);

        ConnectionInfo info = new ConnectionInfo(ip, port);
        final IConnectionManager manager = OkSocket.open(info);

        mConnectionanager = manager;

        OkSocketOptions options = manager.getOption();
        OkSocketOptions.Builder builder = new OkSocketOptions.Builder(options);
        builder.setSinglePackageBytes(1024 * 1024);
        manager.option(builder.build());
        manager.registerReceiver(new SocketActionAdapter() {
            @Override
            public void onSocketConnectionSuccess(Context context, ConnectionInfo info, String action) {
                super.onSocketConnectionSuccess(context, info, action);
                post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "The connection is successful", LENGTH_SHORT).show();
                    }
                });

                manager.send(new ISendable() {
                    @Override
                    public byte[] parse() {
                        return ("Hello world:" + System.currentTimeMillis()).getBytes();
                    }
                });

                setState(State.CONNECTED);
            }
        });
        manager.connect();
    }

    public State getState() {
        return mState;
    }

    public void setState(State s) {
        this.mState = s;
    }
}
