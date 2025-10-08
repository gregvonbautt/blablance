package dev.gbautin.blablance.ui.journal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
        }

        return root
    }

    private fun setupRecyclerView() {
        activityAdapter = JournalActivityAdapter { entryId ->
            homeViewModel.removeActivityEntry(entryId)
        }
        binding.eventsRecyclerView.apply {
            adapter = activityAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}