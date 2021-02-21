package com.moriyukihiro.android_keystore.common

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {

  private val pending = AtomicBoolean(false)

  @MainThread
  override fun observe(
    owner: LifecycleOwner,
    observer: Observer<in T>
  ) {
    if (hasObservers()) {
      Timber.w("Multiple observers registered but only one will be notified of changes.")
    }

    super.observe(
      owner,
      Observer {
        // 新たに値がsetValueされているとき
        if (pending.compareAndSet(
            true,
            false
          )
        ) {
          observer.onChanged(it)
        }
      })
  }

  override fun observeForever(observer: Observer<in T>) {
    throw UnsupportedOperationException("use observe instead of observeForever.")
  }

  @MainThread
  override fun setValue(value: T?) {
    pending.set(true)
    super.setValue(value)
  }

  override fun postValue(value: T) {
    pending.set(true)
    super.postValue(value)
  }
}
