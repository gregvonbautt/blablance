package dev.gbautin.blablance.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import dev.gbautin.blablance.R
import dev.gbautin.blablance.databinding.FragmentConfigureActivitiesBinding
import dev.gbautin.blablance.ui.home.Activity

class ConfigureActivitiesFragment : Fragment() {

    private var _binding: FragmentConfigureActivitiesBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var adapter: SettingsActivityAdapter

    private var currentTab = 0 // 0 for positive, 1 for negative

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentConfigureActivitiesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupTabs()
        observeViewModel()

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.configure_activities_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_activity -> {
                showAddDialog()
                true
            }
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        adapter = SettingsActivityAdapter(
            activities = emptyList(),
            onEdit = { activity ->
                showEditDialog(activity)
            },
            onDelete = { activity ->
                showDeleteConfirmation(activity)
            }
        )
        binding.activitiesRecyclerView.adapter = adapter
    }

    private fun showEditDialog(activity: Activity) {
        showActivityDialog(activity = activity, isEditMode = true)
    }

    private fun showAddDialog() {
        showActivityDialog(activity = null, isEditMode = false)
    }

    private fun showActivityDialog(activity: Activity?, isEditMode: Boolean) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_activity, null)
        val nameInput = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.edit_activity_name)
        val descriptionInput = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.edit_activity_description)
        val scoreInput = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.edit_activity_score)

        // Pre-fill fields if editing
        if (isEditMode && activity != null) {
            nameInput.setText(activity.name)
            descriptionInput.setText(activity.description)
            scoreInput.setText(activity.scoreDelta.toString())
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(if (isEditMode) R.string.dialog_edit_activity_title else R.string.dialog_add_activity_title)
            .setView(dialogView)
            .setPositiveButton(R.string.button_save, null) // Set to null initially
            .setNegativeButton(R.string.button_cancel, null)
            .create()

        dialog.setOnShowListener {
            val saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            saveButton.setOnClickListener {
                val newName = nameInput.text.toString().trim()
                val newDescription = descriptionInput.text.toString().trim()
                val newScore = scoreInput.text.toString().toIntOrNull()

                // Validate inputs
                if (newName.isEmpty()) {
                    nameInput.error = "Name is required"
                    return@setOnClickListener
                }
                if (newDescription.isEmpty()) {
                    descriptionInput.error = "Description is required"
                    return@setOnClickListener
                }
                if (newScore == null) {
                    scoreInput.error = "Valid score is required"
                    return@setOnClickListener
                }

                if (isEditMode && activity != null) {
                    val updatedActivity = activity.copy(
                        name = newName,
                        description = newDescription,
                        scoreDelta = newScore
                    )
                    settingsViewModel.updateActivity(updatedActivity)
                } else {
                    val newActivity = Activity(
                        id = settingsViewModel.getNextActivityId(),
                        name = newName,
                        description = newDescription,
                        scoreDelta = newScore
                    )
                    settingsViewModel.addActivity(newActivity)

                    // Switch to appropriate tab after adding
                    val targetTab = if (newScore > 0) 0 else 1
                    if (currentTab != targetTab) {
                        binding.tabLayout.getTabAt(targetTab)?.select()
                    }
                }

                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun showDeleteConfirmation(activity: Activity) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_delete_activity_title)
            .setMessage(getString(R.string.dialog_delete_activity_message, activity.name))
            .setPositiveButton(R.string.button_delete) { _, _ ->
                settingsViewModel.deleteActivity(activity)
            }
            .setNegativeButton(R.string.button_cancel, null)
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
