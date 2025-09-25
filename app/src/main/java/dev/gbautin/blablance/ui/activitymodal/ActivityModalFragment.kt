package dev.gbautin.blablance.ui.activitymodal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.gbautin.blablance.MainActivity
import dev.gbautin.blablance.R
import dev.gbautin.blablance.databinding.FragmentActivityModalBinding

class ActivityModalFragment : Fragment() {

    private var _binding: FragmentActivityModalBinding? = null
    private val binding get() = _binding!!

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
    }

    private fun setupActionBar() {
        val isPositive = arguments?.getBoolean("isPositive", false) ?: false
        val titleRes = if (isPositive) R.string.title_positive else R.string.title_negative

        val activity = requireActivity() as MainActivity
        activity.supportActionBar?.title = getString(titleRes)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}