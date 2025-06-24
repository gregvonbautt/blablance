package dev.gbautin.blablance.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.gbautin.blablance.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.score.observe(viewLifecycleOwner) { score ->
            binding.scoreDisplay.text = score.toString()
        }

        setupButtons(homeViewModel)

        return root
    }

    private fun setupButtons(homeViewModel: HomeViewModel) {
        // Clear existing buttons
        binding.negativeButtonsContainer.removeAllViews()
        binding.positiveButtonsContainer.removeAllViews()

        // Add negative buttons (left side)
        homeViewModel.negativeButtons.forEach { scoreButton ->
            val button = createButton(scoreButton, homeViewModel)
            binding.negativeButtonsContainer.addView(button)
        }

        // Add positive buttons (right side)
        homeViewModel.positiveButtons.forEach { scoreButton ->
            val button = createButton(scoreButton, homeViewModel)
            binding.positiveButtonsContainer.addView(button)
        }
    }

    private fun createButton(scoreButton: ScoreButton, homeViewModel: HomeViewModel): Button {
        val button = Button(requireContext())
        
        // Set button properties
        button.text = scoreButton.title
        button.textSize = 14f
        
        // Set layout parameters
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 0, 0, 8) // 8dp bottom margin
        button.layoutParams = layoutParams
        
        // Set click listener
        button.setOnClickListener {
            homeViewModel.adjustScore(scoreButton.scoreDelta)
        }
        
        return button
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}