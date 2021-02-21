package com.moriyukihiro.android_keystore.domain.text

interface TextRepository {
  suspend fun saveText(text: String)
  suspend fun loadText(): String
}
