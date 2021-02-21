package com.moriyukihiro.android_keystore.infra.text

import android.content.Context
import com.moriyukihiro.android_keystore.domain.text.TextRepository
import com.moriyukihiro.android_keystore.infra.common.CryptService
import com.moriyukihiro.android_keystore.infra.common.FileService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EncryptedFileTextRepositoryImpl(context: Context) : TextRepository {
  private val cryptService = CryptService()
  private val fileService = FileService(context)

  override suspend fun saveText(text: String): Unit = withContext(Dispatchers.IO) {
    val encryptedText = cryptService.encryptText(text)
    fileService.save(encryptedText)
  }

  override suspend fun loadText(): String = withContext(Dispatchers.IO) {
    val encryptedText = fileService.read()
    cryptService.decryptText(encryptedText)
  }
}
