package com.example.news.ui.home.articleDetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.news.R
import com.example.news.api.articlesModel.Article
import com.example.news.databinding.FragmentArticleDetailsBinding
import com.example.news.ui.home.MainActivity
import com.example.news.util.Constants
import com.example.news.util.parcelable


class ArticleDetailsFragment : Fragment() {
    private lateinit var binding: FragmentArticleDetailsBinding
    private var article: Article? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentArticleDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        article = arguments?.parcelable(Constants.ARTICLE)
        binding.article = article
        onReadMoreClick()
    }

    private fun onReadMoreClick() {
        binding.articleReadMore.setOnClickListener {
            val url = article?.url
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }



    override fun onResume() {
        super.onResume()
        article?.title?.let { setCustomToolbarTitle(it) }
        enableBackArrowButton()

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


    private fun setCustomToolbarTitle(title: String) {
        val activity = requireActivity()

        if (activity is AppCompatActivity) {
            val toolbarTitle = activity.findViewById<TextView>(R.id.toolbarTitle)
            toolbarTitle?.text = title

        }
    }

}