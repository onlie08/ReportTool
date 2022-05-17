package com.ch.report.network;

import cn.leancloud.LCObject;

public interface AVListener {

    void success(LCObject obj);

    void fail(String error);
}
