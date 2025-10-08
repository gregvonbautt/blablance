package dev.gbautin.blablance.ui.activitymodal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dev.gbautin.blablance.MainActivity
import dev.gbautin.blablance.R
import dev.gbautin.blablance.databinding.FragmentActivityModalBinding
import dev.gbautin.blablance.ui.home.HomeViewModel

class ActivityModalFragment : Fragment() {

    private var _binding: FragmentActivityModalBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityModalBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar()
        setupRecyclerView()
    }

    private fun setupActionBar() {
        val isPositive = arguments?.getBoolean("isPositive", false) ?: false
        val titleRes = if (isPositive) R.string.title_positive else R.string.title_negative

        val activity = requireActivity() as MainActivity
        activity.supportActionBar?.title = getString(titleRes)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        val isPositive = arguments?.getBoolean("isPositive", false) ?: false
        val activities = if (isPositive) homeViewModel.positiveActivities else homeViewModel.negativeActivities

        val adapter = ActivityAdapter(activities) { selectedActivity ->
            homeViewModel.addActivityEntry(selectedActivity)
            findNavController().navigateUp()
        }

        binding.activitiesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.activitiesRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}