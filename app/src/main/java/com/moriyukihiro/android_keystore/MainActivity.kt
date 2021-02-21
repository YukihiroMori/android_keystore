package com.moriyukihiro.android_keystore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moriyukihiro.android_keystore.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(
          R.id.container,
          MainFragment.newInstance()
        )
        .commitNow()
    }
  }
}
