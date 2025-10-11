package dev.gbautin.blablance.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import dev.gbautin.blablance.databinding.FragmentSettingsBinding
import dev.gbautin.blablance.ui.home.Activity

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var adapter: SettingsActivityAdapter

    private var currentTab = 0 // 0 for positive, 1 for negative

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupTabs()
        observeViewModel()

        return root
    }

    private fun setupRecyclerView() {
        adapter = SettingsActivityAdapter(
            activities = emptyList(),
            onEdit = { activity ->
                // TODO: Implement edit functionality
            },
            onDelete = { activity ->
                showDeleteConfirmation(activity)
            }
        )
        binding.activitiesRecyclerView.adapter = adapter
    }

    private fun showDeleteConfirmation(activity: Activity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Activity")
            .setMessage("Are you sure you want to delete \"${activity.name}\"?")
            .setPositiveButton("Delete") { _, _ ->
                settingsViewModel.deleteActivity(activity)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentTab = tab?.position ?: 0
                updateActivityList()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun observeViewModel() {
        settingsViewModel.positiveActivities.observe(viewLifecycleOwner) {
            if (currentTab == 0) {
                adapter.updateActivities(it)
            }
        }

        settingsViewModel.negativeActivities.observe(viewLifecycleOwner) {
            if (currentTab == 1) {
                adapter.updateActivities(it)
            }
        }
    }

    private fun updateActivityList() {
        when (currentTab) {
            0 -> settingsViewModel.positiveActivities.value?.let { adapter.updateActivities(it) }
            1 -> settingsViewModel.negativeActivities.value?.let { adapter.updateActivities(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
