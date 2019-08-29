package com.app.common.annotations

import androidx.annotation.IntDef

///可配置类，方法，属性，配置后，将不会被混淆
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.BINARY)
annotation class KeepNotProguard

object Annotations {
    const val WX_FRIEND = 0
    const val WX_CIRCLE = 1
}

///星期
object Week {
    /**星期天*/
    const val SUNDAY = 0
    /**星期一*/
    const val MONDAY = 1
    /**星期二*/
    const val TUESDAY = 2
    /**星期三*/
    const val WEDNESDAY = 3
    /**星期四*/
    const val THURSDAY = 4
    /**星期五*/
    const val FRIDAY = 5
    /**星期六*/
    const val SATURDAY = 6
}

@IntDef(Week.SUNDAY, Week.MONDAY, Week.TUESDAY, Week.WEDNESDAY, Week.THURSDAY, Week.FRIDAY, Week.SATURDAY)
@Retention(AnnotationRetention.SOURCE)
annotation class WeekDays

///注解处理时间格式  @DataFormat(pattern=”yyyy-MM-dd”,timezone=”GMT+8”)

