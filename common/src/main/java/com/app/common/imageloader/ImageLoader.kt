package com.app.common.imageloader


object ImageLoader {

    fun loader(): ILoader {
        return GlideLoader
    }
}