package dev.gbautin.blablance.ui.adjustmenu

import android.app.AlertDialog
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
import dev.gbautin.blablance.databinding.DialogAdjustScoreBinding
import dev.gbautin.blablance.databinding.DialogFullResetBinding
import dev.gbautin.blablance.databinding.FragmentAdjustMenuBinding
import dev.gbautin.blablance.ui.home.Activity
import dev.gbautin.blablance.ui.home.HomeViewModel

class AdjustMenuFragment : Fragment() {

    private var _binding: FragmentAdjustMenuBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdjustMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar()
        setupRecyclerView()
    }

    private fun setupActionBar() {
        val activity = requireActivity() as MainActivity
        activity.supportActionBar?.title = getString(R.string.adjust_menu_title)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        binding.adjustOptionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe score and activity entries to update button states
        homeViewModel.score.observe(viewLifecycleOwner) { score ->
            updateAdjustOptions(score, homeViewModel.activityEntries.value?.size ?: 0)
        }

        homeViewModel.activityEntries.observe(viewLifecycleOwner) { entries ->
            updateAdjustOptions(homeViewModel.score.value ?: 0, entries.size)
        }
    }

    private fun updateAdjustOptions(score: Int, entryCount: Int) {
        val adjustOptions = listOf(
            AdjustOption("adjust_score", R.string.adjust_option_adjust_score, isEnabled = true),
            AdjustOption("reset_to_zero", R.string.adjust_option_reset_to_zero, isEnabled = score != 0),
            AdjustOption("full_reset", R.string.adjust_option_full_reset, isEnabled = entryCount > 0)
        )

        val adapter = AdjustOptionAdapter(adjustOptions) { selectedOption ->
            when (selectedOption.id) {
                "adjust_score" -> showAdjustScoreDialog()
                "reset_to_zero" -> showResetToZeroDialog()
                "full_reset" -> showFullResetDialog()
            }
        }

        binding.adjustOptionsRecyclerView.adapter = adapter
    }

    private fun showAdjustScoreDialog() {
        val dialogBinding = DialogAdjustScoreBinding.inflate(layoutInflater)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_adjust_score_title)
            .setView(dialogBinding.root)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                val scoreText = dialogBinding.adjustScoreInput.text.toString()
                if (scoreText.isNotEmpty()) {
                    val scoreDelta = scoreText.toIntOrNull()
                    if (scoreDelta != null) {
                        val adjustmentActivity = Activity(
                            id = -1, // Special ID for adjustment entries
                            name = getString(R.string.activity_name_adjustment),
                            description = "",
                            scoreDelta = scoreDelta
                        )
                        homeViewModel.addActivityEntry(adjustmentActivity)
                        findNavController().navigateUp()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showResetToZeroDialog() {
        val currentScore = homeViewModel.score.value ?: 0

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_reset_to_zero_title)
            .setMessage(R.string.dialog_reset_to_zero_message)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                val adjustmentActivity = Activity(
                    id = -1,
                    name = getString(R.string.activity_name_adjustment),
                    description = "",
                    scoreDelta = -currentScore
                )
                homeViewModel.addActivityEntry(adjustmentActivity)
                findNavController().navigateUp()
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showFullResetDialog() {
        val dialogBinding = DialogFullResetBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_full_reset_title)
            .setView(dialogBinding.root)
            .setPositiveButton(android.R.string.ok, null)
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.isEnabled = false

            dialogBinding.resetConfirmationCheckbox.setOnCheckedChangeListener { _, isChecked ->
                positiveButton.isEnabled = isChecked
            }

            positiveButton.setOnClickListener {
                homeViewModel.clearAllEntries()
                findNavController().navigateUp()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
