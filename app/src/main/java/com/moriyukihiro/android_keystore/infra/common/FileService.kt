package com.moriyukihiro.android_keystore.infra.common

import android.content.Context
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class FileService(context: Context) {
  companion object {
    private const val FILE_NAME = "encrypted_file"
  }

  private val file = File(
      context.filesDir,
      FILE_NAME
  )

  fun save(text: String) {
    FileWriter(file).use { writer -> writer.write(text) }
  }

  fun read(): String {
    return FileReader(file).readText()
  }
}
