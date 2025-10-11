package dev.gbautin.blablance.ui.journal

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dev.gbautin.blablance.R
import dev.gbautin.blablance.data.ActivityEntry
import dev.gbautin.blablance.databinding.FragmentJournalBinding
import dev.gbautin.blablance.ui.home.HomeViewModel

class JournalFragment : Fragment() {

    private var _binding: FragmentJournalBinding? = null
    private lateinit var activityAdapter: JournalActivityAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJournalBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()

        homeViewModel.activityEntries.observe(viewLifecycleOwner) { activities ->
            activityAdapter.submitList(activities)
            updateEmptyState(activities.isEmpty())
        }

        return root
    }

    private fun setupRecyclerView() {
        activityAdapter = JournalActivityAdapter { entry ->
            showDeleteConfirmation(entry)
        }
        binding.eventsRecyclerView.apply {
            adapter = activityAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showDeleteConfirmation(entry: ActivityEntry) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_remove_activity_title)
            .setMessage(getString(R.string.dialog_remove_activity_message, entry.name))
            .setPositiveButton(R.string.button_remove) { _, _ ->
                homeViewModel.removeActivityEntry(entry.id)
            }
            .setNegativeButton(R.string.button_cancel, null)
            .show()
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.emptyStateText.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.eventsRecyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}