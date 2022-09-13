package com.zed.wannawatch.ui.screens.detail

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.zed.wannawatch.R
import com.zed.wannawatch.databinding.FragmentDetailBinding
import com.zed.wannawatch.services.MovieApplication
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.ui.screens.search.SearchViewModelFactory

//https://developer.android.com/guide/navigation/navigation-pass-data#Safe-args

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()
    lateinit var data: Movie

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory((requireActivity().application as MovieApplication).repository)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.deleteOption -> {
                // show alert dialog
                deleteAlert()
                return true
            }
        }
        // cant return true by default otherwise the back button will not work in the toolbar
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout binding for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        data = args.movie.copy()

        binding.movieTitle.text = data.title
        binding.posterImage.load(data.posterUrl) {
            crossfade(true)
        }

        binding.notesTextInput.setText(data.notes)

        // watch for text edit events, after the user has finished typing update the database
        binding.notesTextInput.addTextChangedListener(afterTextChanged = {
            viewModel.update(data.copy(notes = it.toString().trim()))
        })

        if (data.watched) {
            binding.watchedButton.text = "Unwatch"
        }

        binding.watchedButton.setOnClickListener {
            // TODO make this more user friendly
            val notif: String
            if(data.watched) {
                data.watched = false
                notif = "Un-watched movie"
                binding.watchedButton.text = "Watched!"
            } else {
                data.watched = true
                notif = "Watched movie!"
                binding.watchedButton.text = "Unwatch"
            }
            viewModel.update(data.copy())
            Toast.makeText(context, notif, Toast.LENGTH_SHORT).show()
        }

        // if a rating has already been applied, toggle the correct button
        if(data.rating != -1) {
            when(data.rating) {
                1 -> binding.oneStar.toggle()
                2 -> binding.twoStar.toggle()
                3 -> binding.threeStar.toggle()
                4 -> binding.fourStar.toggle()
                5 -> binding.fiveStar.toggle()
            }
        }

        binding.movieTitle.text = data.title

        // handles the radio group button change notifications
        binding.ratingEmojiGroup.setOnCheckedChangeListener { _, id ->
            var rating = data.rating
            when (id) {
                R.id.oneStar -> rating = 1
                R.id.twoStar -> rating = 2
                R.id.threeStar -> rating = 3
                R.id.fourStar -> rating = 4
                R.id.fiveStar -> rating = 5
            }

            viewModel.update(data.copy(rating = rating))
        }

        return binding.root
    }

    /**
     * Shows a confirmation dialog to the user
     */
    private fun deleteAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Movie")
        builder.setMessage("Are you sure you want to delete this movie?")

        builder.setPositiveButton("Yes") { _, _ ->
            viewModel.delete(data)
            Toast.makeText(requireContext(), "Deleted Movie", Toast.LENGTH_SHORT).show()

            // navigate back home
            val action = DetailFragmentDirections.actionDetailFragmentToHomeFragment()
            findNavController().navigate(action)
        }
        builder.setNegativeButton("No", null)


        builder.show()
    }
}