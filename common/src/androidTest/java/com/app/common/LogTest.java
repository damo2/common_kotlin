package com.app.common;

import android.test.AndroidTestCase;
import com.app.common.log.util.LogInfo;
import com.app.common.log.util.LogToDevice;
import com.app.common.path.SDPathUtils;

import static org.junit.Assert.assertEquals;

/**
 * Created by wangru
 * Date: 2018/6/4  10:50
 * mail: 1902065822@qq.com
 * describe:
 */

public class LogTest extends AndroidTestCase {
    private static final String TAG = "LogTest";
    public void test(){
        LogToDevice.setIsDebug(true);
    }
}
