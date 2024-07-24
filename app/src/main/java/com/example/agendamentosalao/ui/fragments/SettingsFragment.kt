package com.example.agendamentosalao.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.agendamentosalao.R
import com.example.agendamentosalao.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        binding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                binding.switch1.text = "Ativadas"
            }else{
                binding.switch1.text = "Desativadas"
            }
        }
    }
}