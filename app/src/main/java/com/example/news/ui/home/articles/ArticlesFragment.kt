package com.example.news.ui.home.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.news.R
import com.example.news.api.articlesModel.Article
import com.example.news.api.sourcesModel.Source
import com.example.news.databinding.FragmentArticlesBinding
import com.example.news.ui.home.MainActivity
import com.example.news.ui.home.articleDetails.ArticleDetailsFragment
import com.example.news.util.Constants
import com.example.news.util.OnTryAgainClickListener
import com.example.news.util.TabPreferences
import com.example.news.util.showAlertDialog
import com.google.android.material.tabs.TabLayout


class ArticlesFragment : Fragment() {

    private lateinit var binding: FragmentArticlesBinding
    private val adapter = ArticlesAdapter()
    private lateinit var viewModel: ArticlesViewModel
    private lateinit var tabPreferences: TabPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ArticlesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentArticlesBinding.inflate(inflater, container, false)

        tabPreferences = TabPreferences(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        val category = arguments?.getString(Constants.CATEGORY).toString()
        viewModel.getSources(category)
        initRecyclerView()
        initObservers()
    }

    private fun setSelectedTabFromSharedPrefrences() {
        val selectedTab = tabPreferences.getSelectedTab()
        binding.tabLayout.getTabAt(selectedTab)?.select()

    }

    private fun initObservers() {
        viewModel.errorLiveData.observe(viewLifecycleOwner) { viewError ->
            handleError(viewError.message) {
                viewError.onTryAgainClickListener
            }
        }
        viewModel.articlesList.observe(viewLifecycleOwner) { articles ->
            adapter.updateArticles(articles as List<Article>)
        }

        viewModel.sourcesList.observe(viewLifecycleOwner) { sources ->
            bindTabs(sources)
        }

    }

    private fun initRecyclerView() {

        binding.articlesRv.adapter = adapter
        adapter.onArticleClick = { article: Article ->
            val bundle = Bundle()
            bundle
                .putParcelable(Constants.ARTICLE, article)
            val fragment = ArticleDetailsFragment()
            fragment.arguments = bundle
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                .addToBackStack("").commit()
        }
    }

    private fun handleError(message: String? = null, onClickListener: OnTryAgainClickListener) {
        showAlertDialog(message
            ?: "something went wrong",
            posActionName = "try again",
            posAction = { dialog, which ->
                dialog.dismiss()
                onClickListener.onTryAgainClick()
            },
            negActionName = "cancel",
            negAction = { dialog, which ->
                dialog.dismiss()
            })
    }


    private fun bindTabs(sources: List<Source?>?) {
        if (sources == null)
            return
        sources.forEach { source ->
            val tab = binding.tabLayout.newTab()
            tab.text = source?.name
            tab.tag = source?.id
            binding.tabLayout.addTab(tab)
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let {
                    tabPreferences.saveSelectedTab(it)
                }

                viewModel.getArticles(tab?.tag.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                viewModel.getArticles(tab?.tag.toString())
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                viewModel.getArticles(tab?.tag.toString())
            }
        })

        setSelectedTabFromSharedPrefrences()


    }

    override fun onResume() {
        super.onResume()
        setCustomToolbarTitle(arguments?.getString(Constants.CATEGORY).toString())
        enableBackArrowButton()
        setSelectedTabFromSharedPrefrences()
    }


    override fun onPause() {
        super.onPause()
        disableBackArrowButton()
    }


    private fun enableBackArrowButton() {
        val activity = requireActivity() as MainActivity
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)

        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun disableBackArrowButton() {
        val activity = requireActivity() as MainActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.supportActionBar?.setDisplayShowHomeEnabled(false)
    }


    private fun setCustomToolbarTitle(title: String) {
        val activity = requireActivity()

        if (activity is AppCompatActivity) {
            val toolbarTitle = activity.findViewById<TextView>(R.id.toolbarTitle)
            toolbarTitle?.text = title

        }
    }




}