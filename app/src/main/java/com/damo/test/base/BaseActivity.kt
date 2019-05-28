package com.damo.test.base


import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.app.common.base.AppBaseActivity
import com.evernote.android.state.StateSaver
import pub.devrel.easypermissions.EasyPermissions

abstract class BaseActivity(private var isInject: Boolean = false) : AppBaseActivity(), EasyPermissions.PermissionCallbacks {
    protected lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = App.instance
        super.onCreate(savedInstanceState)
        StateSaver.restoreInstanceState(this, savedInstanceState)
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

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        StateSaver.saveInstanceState(this, outState)
    }
}