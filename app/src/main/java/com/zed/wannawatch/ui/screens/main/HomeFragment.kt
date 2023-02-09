package com.zed.wannawatch.ui.screens.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zed.wannawatch.services.MovieApplication



class HomeFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory((requireActivity().application as MovieApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                HomeScreen(viewModel = viewModel,
                    movieClicked = { movie ->
                        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(movie)
                        findNavController().navigate(action)
                    },
                    searchClicked = {
                        val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
                        findNavController().navigate(action)
                    }
                )
            }
        }

    }
}