package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.utils

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Random

class FileUtils {
    companion object {
        fun getTempFilename(
            context: Context,
            prefix : String = "image",
            suffix : String = ".img"
        ) : String = File.createTempFile(
            prefix, suffix,
            context.externalCacheDir
        ).absolutePath

        fun getInternalFilename(
            context: Context,
            prefix : String = "image",
            suffix : String = ".img"
        ) : String = File.createTempFile(
            prefix, suffix,
            context.filesDir
        ).absolutePath

        fun createFileFromUri(
            context: Context,
            uri : Uri,
            filename : String = getInternalFilename(context)
        ) : String {
            FileOutputStream(filename).use { outputStream ->
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            return filename
        }
        fun copyFile(
            originalPath : String,
            newFilename : String
        ) : String {
            FileOutputStream(newFilename).use { outputStream ->
                FileInputStream(originalPath).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            return newFilename
        }
        fun copyFile(
            context: Context,
            originalPath : String,
            newFilename : String = getInternalFilename(context)
        ) : String {
            FileOutputStream(newFilename).use { outputStream ->
                FileInputStream(originalPath).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            return newFilename
        }

        fun generateAlphaNumericCode(lenght: Int = 6) : String
        {
            val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            val random = Random()
            return (1..lenght)
                .map { chars[random.nextInt(chars.length)] }
                .joinToString("");

        }
        fun getFileExtension(context: Context, uri: Uri): String? {
            val contentResolver = context.contentResolver
            val mimeType = contentResolver.getType(uri)
            return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        }
    }
}