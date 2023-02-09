package com.zed.wannawatch.ui.screens.detail

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zed.wannawatch.R

//https://developer.android.com/guide/navigation/navigation-pass-data#Safe-args

class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()


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


        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DetailScreen(args.movie.copy())
            }
        }
    }

    /**
     * Shows a confirmation dialog to the user
     */
    private fun deleteAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Movie")
        builder.setMessage("Are you sure you want to delete this movie?")

        builder.setPositiveButton("Yes") { _, _ ->
//            viewModel.delete(data)
            Toast.makeText(requireContext(), "Deleted Movie", Toast.LENGTH_SHORT).show()

            // navigate back home
            val action = DetailFragmentDirections.actionDetailFragmentToHomeFragment()
            findNavController().navigate(action)
        }
        builder.setNegativeButton("No", null)


        builder.show()
    }
}