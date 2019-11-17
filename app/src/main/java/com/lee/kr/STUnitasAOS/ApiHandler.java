package com.lee.kr.STUnitasAOS;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ApiHandler extends Handler {

    public HandlerType handlerType = HandlerType.LIST;

    public ApiHandler(@Nullable Callback callback) {
        super(callback);
    }


}
