package dev.gbautin.blablance.ui.adjustmenu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.gbautin.blablance.databinding.ItemAdjustOptionBinding

class AdjustOptionAdapter(
    private val options: List<AdjustOption>,
    private val onOptionSelected: (AdjustOption) -> Unit
) : RecyclerView.Adapter<AdjustOptionAdapter.AdjustOptionViewHolder>() {

    class AdjustOptionViewHolder(private val binding: ItemAdjustOptionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(option: AdjustOption, onOptionClick: () -> Unit) {
            binding.optionText.setText(option.titleResId)
            binding.optionText.isEnabled = option.isEnabled
            binding.optionText.alpha = if (option.isEnabled) 1.0f else 0.4f
            binding.root.isClickable = option.isEnabled
            binding.root.setOnClickListener {
                if (option.isEnabled) {
                    onOptionClick()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdjustOptionViewHolder {
        val binding = ItemAdjustOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdjustOptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdjustOptionViewHolder, position: Int) {
        val option = options[position]
        holder.bind(option) {
            onOptionSelected(option)
        }
    }

    override fun getItemCount() = options.size
}
