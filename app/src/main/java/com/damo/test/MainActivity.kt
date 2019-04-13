package com.damo.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.damo.libdb.Dao
import com.damo.libdb.objectbox.ObjectBoxInit
import kotlinx.android.synthetic.main.activity_main.*
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
    }
}
