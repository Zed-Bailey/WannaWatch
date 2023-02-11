package com.zed.wannawatch.ui.screens.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zed.wannawatch.services.MovieApplication


class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory((requireActivity().application as MovieApplication).repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                SearchScreen(
                    viewModel = viewModel,
                    itemClicked = {
                        Log.i("com.zed.wannawatch", "search item clicked")
                    }
                )
            }
        }
    }


}