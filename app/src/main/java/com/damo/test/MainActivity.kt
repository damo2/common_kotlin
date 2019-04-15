package com.damo.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.app.common.api.RequestFileManager
import com.app.common.api.download.FileDownLoadObserver
import com.app.common.utils.StorageUtils
import com.damo.libdb.Dao
import com.damo.libdb.objectbox.ObjectBoxInit
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {
    var name by Dao<String>(String::class.java, "name")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        ObjectBoxInit.build(applicationContext);
        tvPutCache.setOnClickListener {
            name = "张三${Random().nextInt(100)}"
        }
        tvGetCache.setOnClickListener {
            Toast.makeText(applicationContext, name, Toast.LENGTH_SHORT).show();
        }

        //需要读写权限
        tvDownload.setOnClickListener {
            RequestFileManager.downloadFile("http://wangru.oss-cn-qingdao.aliyuncs.com/test/erp-v1.0.0-20190404.apk", StorageUtils.getPublicStorageFile("test/wanban.apk")!!, object : FileDownLoadObserver<File>() {
                override fun onDownLoadSuccess(t: File) {
                    Toast.makeText(applicationContext, "下载成功", Toast.LENGTH_SHORT).show();
                }

                override fun onDownLoadFail(throwable: Throwable) {

                }
            }, { totalLength, contentLength, done ->

            });
        }

    }
}
