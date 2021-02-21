package com.moriyukihiro.android_keystore.infra.text

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.moriyukihiro.android_keystore.domain.text.TextRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EncryptedSharedPreferencesTextRepositoryImpl(context: Context) : TextRepository {
  companion object {
    private const val FILE_NAME = "encrypted_shared_preferences"
    private const val KEY = "text"
  }

  private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
  private val alias = MasterKeys.getOrCreate(keyGenParameterSpec)

  private val sharedPreferences = EncryptedSharedPreferences
    .create(
      FILE_NAME,
      alias,
      context,
      EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
      EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

  override suspend fun saveText(text: String): Unit = withContext(Dispatchers.IO) {
    val editor = sharedPreferences.edit()
    editor.putString(
      KEY,
      text
    )
    editor.apply()
  }

  override suspend fun loadText(): String = withContext(Dispatchers.IO) {
    sharedPreferences.getString(
      KEY,
      null
    ) ?: ""
  }
}
