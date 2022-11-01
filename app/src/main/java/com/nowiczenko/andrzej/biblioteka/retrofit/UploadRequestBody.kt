package com.nowiczenko.andrzej.biblioteka.retrofit

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class UploadRequestBody(
    private val image: File,
    private val contentType: String,
): RequestBody(){


    override fun contentType() = MediaType.parse("$contentType/*")

    override fun contentLength() = image.length()

    override fun writeTo(sink: BufferedSink) {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val fileInputStream = FileInputStream(image)

        fileInputStream.use { inputStream ->
            var read: Int
            val handler = Handler(Looper.getMainLooper())

            while(inputStream.read(buffer).also { read = it } != -1){
                sink.write(buffer, 0, read)
            }
        }
    }

    companion object{
        private const val DEFAULT_BUFFER_SIZE = 1048
    }

}
