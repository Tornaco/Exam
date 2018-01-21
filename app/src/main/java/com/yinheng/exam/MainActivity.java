package com.yinheng.exam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yinheng.xapr.ITextReceiver;

public class MainActivity extends Activity {

    private ITextReceiver chongdingdahui = new ITextReceiver.Stub() {
        @Override
        public void onTextChanged(final String text) throws RemoteException {

            mHandler.removeCallbacksAndMessages(null);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        sendMessage(text);
                    } catch (Throwable e) {
                        Log.d("Nick", "Error send message..." + e);
                    }
                    Log.d("Nick", "text changed---" + text);
                }
            });
        }

        @Override
        public boolean matchPackage(String who) throws RemoteException {
            return "com.chongdingdahui.app".equals(who)
                    || "com.inke.trivia".equals(who)
                    || "com.ss.android.article.video".equals(who)
                    || "com.netease.newsreader.activity".equals(who);
        }

        @Override
        public boolean matchEntry(String which) throws RemoteException {
            return
                    "tvMessage".equals(which)  // CHONGDINGDAHUI
                            || "tv_question".equals(which) // ZHISHI
                            || "gj".equals(which) // XIGUA
                            || "bib".equals(which); // WANGYI
        }
    };

    void sendMessage(String message) {
        Intent intent = new Intent("tornaco.send");
        intent.putExtra("msg", message.replace("自由女神像是哪国送给美国的礼物？", ""));
        intent.setClass(getApplicationContext(), MessageSendingService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (BridgeManager.get().isServiceAvailable()) {
            try {
                BridgeManager.get().unRegisterTextReceiver(chongdingdahui);
            } catch (RemoteException ignored) {

            }
        }
    }

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();
        if (BridgeManager.get().isServiceAvailable()) {
            try {
                BridgeManager.get().registerTextReceiver("com.chongdingdahui.app", null, chongdingdahui);
                Toast.makeText(getApplicationContext(), "REGISTER OK", Toast.LENGTH_LONG).show();
            } catch (RemoteException e) {
                Toast.makeText(getApplicationContext(), "REGISTER FAIL", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "BRIDGE MISSING", Toast.LENGTH_LONG).show();
        }

        final EditText editText = findViewById(R.id.edit_text);
        final EditText addressText = findViewById(R.id.address);
        Button connectBtn = findViewById(R.id.connect);
        Button sendBtn = findViewById(R.id.send);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString().trim();
                Intent intent = new Intent("tornaco.send");
                intent.putExtra("msg", message);
                intent.setClass(getApplicationContext(), MessageSendingService.class);
                startService(intent);
            }
        });

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressText.getText().toString().trim();
                String ip = address.substring(0, address.indexOf(":"));
                int port = Integer.parseInt(address.substring(ip.length() + 1, address.length()));
                Log.i("Nick", "ip " + ip);
                Log.i("Nick", "port " + port);
                Intent intent = new Intent("tornaco.connect");
                intent.putExtra("ip", ip);
                intent.putExtra("port", port);
                intent.setClass(getApplicationContext(), MessageSendingService.class);
                startService(intent);
            }
        });
    }


}
