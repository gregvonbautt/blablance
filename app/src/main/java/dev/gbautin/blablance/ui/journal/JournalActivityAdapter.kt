package dev.gbautin.blablance.ui.journal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.gbautin.blablance.data.ActivityEntry
import dev.gbautin.blablance.databinding.ItemJournalActivityBinding
import java.time.format.DateTimeFormatter

class JournalActivityAdapter(
    private val onRemoveActivity: (String) -> Unit
) : ListAdapter<ActivityEntry, JournalActivityAdapter.ActivityViewHolder>(ActivityDiffCallback()) {

    private var expandedPosition = -1

    class ActivityViewHolder(private val binding: ItemJournalActivityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(activity: ActivityEntry, isExpanded: Boolean, onItemClick: () -> Unit, onRemoveClick: () -> Unit) {
            binding.activityName.text = activity.name
            binding.activityScore.text = if (activity.scoreDelta > 0) "+${activity.scoreDelta}" else activity.scoreDelta.toString()
            binding.activityDescription.text = activity.description

            val formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm")
            binding.activityTimestamp.text = activity.timestamp.format(formatter)

            binding.activityDescription.visibility = if (isExpanded) View.VISIBLE else View.GONE
            binding.removeActivityButton.visibility = if (isExpanded) View.VISIBLE else View.GONE

            binding.activityHeader.setOnClickListener { onItemClick() }
            binding.removeActivityButton.setOnClickListener { onRemoveClick() }

            val scoreColor = if (activity.scoreDelta > 0) {
                binding.root.context.getColor(android.R.color.holo_green_dark)
            } else {
                binding.root.context.getColor(android.R.color.holo_red_dark)
            }
            binding.activityScore.setTextColor(scoreColor)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ItemJournalActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = getItem(position)
        val isExpanded = position == expandedPosition

        holder.bind(
            activity = activity,
            isExpanded = isExpanded,
            onItemClick = {
                val previousExpanded = expandedPosition
                expandedPosition = if (isExpanded) -1 else position

                if (previousExpanded != -1) {
                    notifyItemChanged(previousExpanded)
                }
                notifyItemChanged(position)
            },
            onRemoveClick = {
                onRemoveActivity(activity.id)
            }
        )
    }

    class ActivityDiffCallback : DiffUtil.ItemCallback<ActivityEntry>() {
        override fun areItemsTheSame(oldItem: ActivityEntry, newItem: ActivityEntry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ActivityEntry, newItem: ActivityEntry): Boolean {
            return oldItem == newItem
        }
    }
}