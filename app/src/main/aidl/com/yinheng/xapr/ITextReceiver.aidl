// ITextReceiver.aidl
package com.yinheng.xapr;

// Declare any non-default types here with import statements

interface ITextReceiver {
    void onTextChanged(String text);
    boolean matchPackage(String who);
    boolean matchEntry(String which);
}
