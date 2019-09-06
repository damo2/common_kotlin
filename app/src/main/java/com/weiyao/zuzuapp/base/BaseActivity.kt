package com.weiyao.zuzuapp.base


import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.app.common.base.AppBaseActivity
import pub.devrel.easypermissions.EasyPermissions

abstract class BaseActivity(private val isPortrait: Boolean = true) : AppBaseActivity(), EasyPermissions.PermissionCallbacks {
    protected lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = App.instance
        if (isPortrait) {
            //竖屏
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        super.onCreate(savedInstanceState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}