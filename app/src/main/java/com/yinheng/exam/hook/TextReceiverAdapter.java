package com.yinheng.exam.hook;

import com.yinheng.xapr.ITextReceiver;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by guohao4 on 2018/1/20.
 * Email: Tornaco@163.com
 */
@AllArgsConstructor
@Getter
public abstract class TextReceiverAdapter extends ITextReceiver.Stub {

    private String pkg, entryName;

    @Override
    public boolean matchPackage(String who) {
        return who.equals(pkg);
    }

    @Override
    public boolean matchEntry(String which) {
        return which.equals(entryName);
    }
}
