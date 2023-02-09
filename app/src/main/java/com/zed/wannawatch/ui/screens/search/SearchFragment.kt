package com.zed.wannawatch.ui.screens.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.zed.wannawatch.R
import com.zed.wannawatch.databinding.FragmentSearchBinding
import com.zed.wannawatch.services.MovieApplication
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.ui.util.OnClickListener


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory((requireActivity().application as MovieApplication).repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Create the fragments binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

//        https://www.bragitoff.com/2017/04/trigger-button-click-press-doneenter-key-keyboard-solved/
        binding.textInput.setOnEditorActionListener { textView, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(textView.windowToken, 0)

                if(textView.text.isNotEmpty()){
                    viewModel.search(textView.text.toString())
                }
            }
            false
        }

        viewModel.error.observe(viewLifecycleOwner) { wasError ->
            if (wasError) {
                val snack = Snackbar.make(requireView(), "Sorry there was an error while trying to query the API", Snackbar.LENGTH_SHORT)
                snack.animationMode = Snackbar.ANIMATION_MODE_FADE
                snack.show()
                viewModel.toggleErrorOff()
            }
        }


        val adapter = SearchListAdapter(OnClickListener {
            val movie = Movie(it.imdbID,it.Title, it.Poster)
            val action = SearchFragmentDirections.actionSearchFragmentToSearchDetailFragment(movie)
            findNavController().navigate(action)
        })

        // setup recycler view
        binding.resultList.adapter = adapter
        binding.resultList.layoutManager = GridLayoutManager(context, 3)

        // observe result data, submit new data to the recycler view on change
        viewModel.results.observe(viewLifecycleOwner) {
            // if the list is null, then there are no results for the query
            if(it == null) {
                Toast.makeText(context, "No results", Toast.LENGTH_SHORT).show()
            }
            adapter.submitList(it)
        }

        return binding.root
    }


}