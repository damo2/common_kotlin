package com.app.common.file

enum class FileMetaData(val extension: String, val type: FileType, vararg val headers: String) {

    UNKNOWN("", FileType.UNKNOWN, ""),
    //图片
    PNG("png", FileType.IMAGE, "89504E", "89504E47"),
    JPG("jpg", FileType.IMAGE, "FFD8FF", "FFD8FFE0", "FFD8FFE1", "FFD8FFE8"),
    GIF("gif", FileType.IMAGE, "474946", "47494638", "89504E47"),
    TIFF("tiff", FileType.IMAGE, "49492A00", "4D4D002A"),
    WEBP("webp", FileType.IMAGE, "52494646"),
    BMP("bmp", FileType.IMAGE, "424D", "424D46"),
    PSD("psd", FileType.IMAGE, "38425053"),
    //音频
    WAV("wav", FileType.AUDIO, "57415645"),
    AVI("avi", FileType.AUDIO, "41564920"),
    MID("mid", FileType.AUDIO, "4D546864"),
    //视频
    RM("rm", FileType.VIDEO, "2E524D46"),
    MPG("mpg", FileType.VIDEO, "000001BA", "000001B3"),
    MOV("mov", FileType.VIDEO, "6D6F6F76"),
    ASF("asf", FileType.VIDEO, "3026B2758E66CF11"),
    //压缩文件
    RAR("rar", FileType.COMPRESS, "526172", "52617221"),
    ZIP("zip", FileType.COMPRESS, "504B03", "504B0304"),
    GZ("gz", FileType.COMPRESS, "1F8B08"),
    //文本
    TXT("txt", FileType.OTHER, "75736167"),
    RTF("rtf", FileType.OTHER, "7B5C727466"), // 日记本
    DOCX("docx", FileType.OTHER, "504B0304"),
    PDF("pdf", FileType.OTHER, "255044462D312E"),
    DOC("doc", FileType.OTHER, "D0CF11E0"),
    MDB("mdb", FileType.OTHER, "5374616E64617264204A");

}

enum class FileType(val type: Int) {
    UNKNOWN(0),
    IMAGE(1),
    AUDIO(2),
    VIDEO(3),
    COMPRESS(4),
    OTHER(5)
}

