package dev.gbautin.blablance.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev.gbautin.blablance.R
import dev.gbautin.blablance.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel: HomeViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.score.observe(viewLifecycleOwner) { score ->
            binding.scoreDisplay.text = score.toString()
        }

        binding.positiveButton.setOnClickListener {
            val bundle = Bundle().apply {
                putBoolean("isPositive", true)
            }
            findNavController().navigate(R.id.action_home_to_activity_modal, bundle)
        }

        binding.negativeButton.setOnClickListener {
            val bundle = Bundle().apply {
                putBoolean("isPositive", false)
            }
            findNavController().navigate(R.id.action_home_to_activity_modal, bundle)
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}