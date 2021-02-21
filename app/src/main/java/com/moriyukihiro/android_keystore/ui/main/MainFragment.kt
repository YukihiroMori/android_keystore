package com.moriyukihiro.android_keystore.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.moriyukihiro.android_keystore.R
import com.moriyukihiro.android_keystore.databinding.MainFragmentBinding
import com.moriyukihiro.android_keystore.infra.text.EncryptedFileTextRepositoryImpl

class MainFragment : Fragment(R.layout.main_fragment) {

  companion object {
    fun newInstance() = MainFragment()
  }

  private lateinit var viewModel: MainViewModel

  override fun onViewCreated(
      view: View,
      savedInstanceState: Bundle?
  ) {
    super.onViewCreated(
        view,
        savedInstanceState
    )
    viewModel = ViewModelProvider(
        this,
        MainViewModel.Factory(EncryptedFileTextRepositoryImpl(requireContext()))
    ).get(MainViewModel::class.java)

    val binding = MainFragmentBinding.bind(view)
    binding.lifecycleOwner = this
    binding.viewModel = viewModel
  }
}
