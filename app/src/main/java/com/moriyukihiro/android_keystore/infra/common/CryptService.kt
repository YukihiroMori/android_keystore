package com.moriyukihiro.android_keystore.infra.common

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.spec.MGF1ParameterSpec
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource


class CryptService {
  companion object {
    private const val ALIAS = "android_keystore"
    private const val PROVIDER = "AndroidKeyStore"
    private const val ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"
  }

  private val keyStore: KeyStore = KeyStore.getInstance(PROVIDER).apply {
    load(null)
  }
  private val spec = OAEPParameterSpec(
      "SHA-256",
      "MGF1",
      MGF1ParameterSpec.SHA1,
      PSource.PSpecified.DEFAULT
  )

  init {
    if (!keyStore.containsAlias(ALIAS)) {
      generateKeyPair()
    }
  }

  fun encryptText(plainText: String): String {
    val publicKey = keyStore.getCertificate(ALIAS).publicKey
    val cipher = Cipher.getInstance(ALGORITHM)
    cipher.init(
        Cipher.ENCRYPT_MODE,
        publicKey,
        spec
    )

    val outputStream = ByteArrayOutputStream()
    val cipherOutputStream = CipherOutputStream(
        outputStream,
        cipher
    )
    cipherOutputStream.write(plainText.toByteArray())
    cipherOutputStream.close()

    val bytes: ByteArray = outputStream.toByteArray()
    return Base64.encodeToString(
        bytes,
        Base64.DEFAULT
    )
  }

  fun decryptText(encryptedText: String): String {
    val privateKey = keyStore.getKey(
        ALIAS,
        null
    ) as PrivateKey
    val cipher = Cipher.getInstance(ALGORITHM)
    cipher.init(
        Cipher.DECRYPT_MODE,
        privateKey,
        spec
    )

    val cipherInputStream = CipherInputStream(
        ByteArrayInputStream(
            Base64.decode(
                encryptedText,
                Base64.DEFAULT
            )
        ),
        cipher
    )

    val outputStream = ByteArrayOutputStream()
    var byte: Int
    while (cipherInputStream.read().also { byte = it } != -1) {
      outputStream.write(byte)
    }
    outputStream.close()
    return outputStream.toString("UTF-8")
  }

  private fun generateKeyPair() {
    val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(
        KeyProperties.KEY_ALGORITHM_RSA,
        PROVIDER
    )
    val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
        ALIAS,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    ).run {
      setDigests(
          KeyProperties.DIGEST_SHA256,
          KeyProperties.DIGEST_SHA512
      )
      setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
      build()
    }

    kpg.initialize(parameterSpec)
    kpg.generateKeyPair()
  }
}
