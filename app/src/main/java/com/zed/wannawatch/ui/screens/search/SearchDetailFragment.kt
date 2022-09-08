package com.zed.wannawatch.ui.screens.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.zed.wannawatch.R
import com.zed.wannawatch.databinding.FragmentSearchDetailBinding
import com.zed.wannawatch.services.MovieApplication


class SearchDetailFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory((requireActivity().application as MovieApplication).repository)
    }
    private val args: SearchDetailFragmentArgs by navArgs()

    lateinit var binding: FragmentSearchDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_detail, container, false)
        val selectedMovie = args.clickedMovie

        binding.imageView.load(selectedMovie.posterUrl)
        binding.searchDetailTitle.text = selectedMovie.title

        binding.addMovieButton.setOnClickListener {
            // add movie to database
            viewModel.addMovie(selectedMovie)
            val action = SearchDetailFragmentDirections.actionSearchDetailFragmentToHomeFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

}