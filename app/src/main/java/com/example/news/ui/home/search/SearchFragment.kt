package com.example.news.ui.home.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.news.R
import com.example.news.api.articlesModel.Article
import com.example.news.databinding.FragmentSearchBinding
import com.example.news.ui.home.MainActivity
import com.example.news.ui.home.articleDetails.ArticleDetailsFragment
import com.example.news.ui.home.articles.ArticlesAdapter
import com.example.news.util.Constants
import com.example.news.util.OnTryAgainClickListener
import com.example.news.util.showAlertDialog

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val adapter = ArticlesAdapter()
    lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        onArticleClick()
        initRecyclerView()
        val query = arguments?.getString(Constants.QUERY)
        query?.let {
            view.hideKeyboard()
            viewModel.searchArticles(it)
        }
        initObservers()

    }

    private fun initObservers() {
        viewModel.articlesLiveData.observe(viewLifecycleOwner) { articles ->
            adapter.updateArticles(articles as List<Article>)
            if (articles.isEmpty())
                binding.llNotFound.visibility = View.VISIBLE
            else
                binding.llNotFound.visibility = View.GONE
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { viewError ->
            handleError(viewError.message) {
                viewError.onTryAgainClickListener
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setCustomToolbarTitle(getString(R.string.search))
        enableBackArrowButton()

    }

    override fun onPause() {
        super.onPause()
        disableBackArrowButton()
    }

    private fun initRecyclerView() {
        binding.rvSearchedArticles.adapter = adapter
    }

    private fun setCustomToolbarTitle(title: String) {
        val activity = requireActivity()

        if (activity is AppCompatActivity) {
            val toolbarTitle = activity.findViewById<TextView>(R.id.toolbarTitle)
            toolbarTitle?.text = title

        }
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


    private fun onArticleClick() {
        adapter.onArticleClick = { article: Article ->
            val bundle = Bundle()
            bundle.putParcelable(Constants.ARTICLE, article)
            val fragment = ArticleDetailsFragment()
            fragment.arguments = bundle
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                .addToBackStack("").commit()
        }
    }


    private fun View.hideKeyboard() {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun handleError(
        message: String? = null,
        onClickListener: OnTryAgainClickListener
    ) {
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
}