package dev.gbautin.blablance.ui.activitymodal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.gbautin.blablance.databinding.ItemActivityBinding
import dev.gbautin.blablance.ui.home.Activity

class ActivityAdapter(
    private val activities: List<Activity>,
    private val onActivitySelected: (Activity) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    private var expandedPosition = -1

    class ActivityViewHolder(private val binding: ItemActivityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(activity: Activity, isExpanded: Boolean, onItemClick: () -> Unit, onAddClick: () -> Unit) {
            binding.activityName.text = activity.name
            binding.activityScore.text = if (activity.scoreDelta > 0) "+${activity.scoreDelta}" else activity.scoreDelta.toString()
            binding.activityDescription.text = activity.description

            binding.activityDescription.visibility = if (isExpanded) View.VISIBLE else View.GONE
            binding.addActivityButton.visibility = if (isExpanded) View.VISIBLE else View.GONE

            binding.activityHeader.setOnClickListener { onItemClick() }
            binding.addActivityButton.setOnClickListener { onAddClick() }

            val scoreColor = if (activity.scoreDelta > 0) {
                binding.root.context.getColor(android.R.color.holo_green_dark)
            } else {
                binding.root.context.getColor(android.R.color.holo_red_dark)
            }
            binding.activityScore.setTextColor(scoreColor)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ItemActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
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
            onAddClick = {
                onActivitySelected(activity)
            }
        )
    }

    override fun getItemCount() = activities.size
}