package com.example.news.ui.home.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.news.R
import com.example.news.databinding.FragmentCategoriesBinding
import com.example.news.ui.home.MainActivity
import com.example.news.ui.home.articles.ArticlesFragment
import com.example.news.util.Constants


class CategoriesFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCategoriesListeners()
        enableHamburgerButton()

    }

    private fun setCategoriesListeners() {
        binding.sportsCategory.setOnClickListener(this)
        binding.technologyCategory.setOnClickListener(this)
        binding.healthCategory.setOnClickListener(this)
        binding.businessCategory.setOnClickListener(this)
        binding.entertainmentCategory.setOnClickListener(this)
        binding.scienceCategory.setOnClickListener(this)
    }

    private fun enableHamburgerButton() {
        val activity = requireActivity() as MainActivity

        val drawerLayout = activity.findViewById<DrawerLayout>(R.id.drawer_layout)
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)

        val toggle = ActionBarDrawerToggle(
            activity,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onResume() {
        super.onResume()
        setCustomToolbarTitle(getString(R.string.app_name))
    }

    private fun setCustomToolbarTitle(title: String) {
        val activity = requireActivity()

        if (activity is AppCompatActivity) {
            val toolbarTitle = activity.findViewById<TextView>(R.id.toolbarTitle)
            toolbarTitle?.text = title
        }

    }

    override fun onClick(v: View?) {
        val bundle = Bundle()
        bundle.putString(Constants.CATEGORY, v?.tag.toString())

        val articlesFragment = ArticlesFragment()
        articlesFragment.arguments = bundle
        parentFragmentManager.beginTransaction().replace(R.id.fragment_container, articlesFragment)
            .addToBackStack("").commit()
    }

}