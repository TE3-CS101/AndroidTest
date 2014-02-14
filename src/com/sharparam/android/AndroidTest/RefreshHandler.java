package com.sharparam.android.AndroidTest;

import android.os.Handler;
import android.os.Message;
import com.sharparam.android.AndroidTest.views.BallView;

/**
 * Created by on 2014-02-14.
 *
 * @author Sharparam
 */
public class RefreshHandler extends Handler {
    private final BallView owner;

    public RefreshHandler(BallView owner) {
        this.owner = owner;
    }

    @Override
    public void handleMessage(Message msg) {
        owner.onUpdate();
        owner.invalidate();
    }

    public void sleep(long ms) {
        removeMessages(0);
        sendMessageDelayed(obtainMessage(0), ms);
    }
}
