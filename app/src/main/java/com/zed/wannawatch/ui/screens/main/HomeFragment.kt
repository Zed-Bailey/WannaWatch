package com.zed.wannawatch.ui.screens.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.zed.wannawatch.R
import com.zed.wannawatch.databinding.FragmentHomeBinding
import com.zed.wannawatch.services.MovieApplication
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.ui.util.OnClickListener


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory((requireActivity().application as MovieApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val recycler = binding.movieList
        // pass the click listener to the adapter
        val movieListAdapter = MovieListAdapter(OnClickListener { movieClicked(it) })

        recycler.adapter = movieListAdapter
        recycler.layoutManager = GridLayoutManager(context, 3)

        binding.addButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(action)
        }

        // toggle visibility of the noMoviesText if no movies have been added
        if(viewModel.movies.value?.isEmpty() == true) {
            binding.noMoviesText.visibility = View.VISIBLE
        }

        viewModel.movies.observe(viewLifecycleOwner) {
            movieListAdapter.submitList(it)

            // toggle visibility of the noMoviesText if movies have been added
            if(viewModel.movies.value?.isEmpty() == true) {
                binding.noMoviesText.visibility = View.VISIBLE
            } else {
                binding.noMoviesText.visibility = View.INVISIBLE
            }
        }

        return binding.root
    }

    /**
     * Callback function for the on click listener
     * @param movie the clicked movie
     */
    private fun movieClicked(movie: Movie) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(movie)
        findNavController().navigate(action)
    }
}