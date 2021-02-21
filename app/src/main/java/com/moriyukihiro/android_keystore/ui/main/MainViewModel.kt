package com.moriyukihiro.android_keystore.ui.main

import androidx.lifecycle.*
import com.moriyukihiro.android_keystore.domain.text.TextRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
  private val repository: TextRepository
) : ViewModel() {

  val inputText: MutableLiveData<String> = MutableLiveData("")

  private val _loadedText: MutableLiveData<String> = MutableLiveData("")
  val loadedText: LiveData<String> = _loadedText

  init {
    loadText()
  }

  fun onSaveButtonClick() {
    viewModelScope.launch {
      val text = inputText.value ?: return@launch
      runCatching {
        repository.saveText(text)
      }.fold(
        onSuccess = {
          loadText()
        },
        onFailure = {
          Timber.e(it)
        }
      )
    }
  }

  private fun loadText() {
    viewModelScope.launch {
      runCatching {
        _loadedText.value = repository.loadText()
      }.fold(
        onSuccess = {
          //noop
        },
        onFailure = {
          Timber.e(it)
        }
      )
    }
  }

  class Factory(
    private val textRepository: TextRepository
  ) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return if (modelClass == MainViewModel::class.java) {
        MainViewModel(
          textRepository
        ) as T
      } else {
        throw IllegalStateException()
      }
    }
  }
}
