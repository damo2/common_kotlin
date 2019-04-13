package com.damo.libdb.objectbox.bean

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index


/**
 * Created by wr
 * Date: 2018/12/27  17:21
 * describe:
 */
@Entity
data class CacheInfoBean (@Id
                          var id: Long  = 0,
                          @Index
                          var key: String? = null,
                          var version: String? = null,
                          var data: String? = null,
                          var ext: String? = null,
                          var timeCreate: Long? = null,
                          var timeDuration: Long? = null)

