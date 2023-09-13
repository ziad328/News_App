package com.example.news.ui.home.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.news.R
import com.example.news.databinding.FragmentSettingsBinding
import com.example.news.util.LocaleManager
import com.example.news.util.getCurrentLanguage
import com.example.news.util.recreateActivity

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO: get from share preferences later
        val currentCountryCode = getCurrentLanguage(requireContext())
        val language = if (currentCountryCode == "en") "English" else "Arabic"
        setLanguageDropDownMenuState(language)
        onLanguageDropDownMenuClick()

    }

    override fun onResume() {
        super.onResume()
        setLanguageSettings()
        setCustomToolbarTitle(getString(R.string.menu_settings))

    }

    private fun setCustomToolbarTitle(title: String) {
        val activity = requireActivity()

        if (activity is AppCompatActivity) {
            val toolbarTitle = activity.findViewById<TextView>(R.id.toolbarTitle)
            toolbarTitle?.text = title

        }
    }

    private fun applyLanguageChange(languageCode: String) {
        LocaleManager.setLocale(requireContext(), languageCode)
        recreateActivity()
    }

    private fun setLanguageDropDownMenuState(selectedLanguage: String) {
        binding.autoCompleteTVLanguages.setText(selectedLanguage)
    }

    private fun setLanguageSettings() {
        val languages = resources.getStringArray(R.array.languages)
        val arrayAdapterLanguages =
            ArrayAdapter(requireContext(), R.layout.drop_down_item, languages)
        binding.autoCompleteTVLanguages.setAdapter(arrayAdapterLanguages)

    }

    private fun onLanguageDropDownMenuClick() {
        binding.autoCompleteTVLanguages.setOnItemClickListener { parent, view, position, id ->
            val selectedLanguage = parent.getItemAtPosition(position).toString()
            val languageCode = if (selectedLanguage == "English") "en" else "ar"
            setLanguageDropDownMenuState(selectedLanguage)
            applyLanguageChange(languageCode)

        }
    }
}