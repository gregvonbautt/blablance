package dev.gbautin.blablance.ui.journal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.gbautin.blablance.R
import dev.gbautin.blablance.data.ScoreEvent
import java.time.format.DateTimeFormatter

class EventAdapter : ListAdapter<ScoreEvent, EventAdapter.EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.event_title)
        private val scoreText: TextView = itemView.findViewById(R.id.event_score)
        private val timestampText: TextView = itemView.findViewById(R.id.event_timestamp)

        fun bind(event: ScoreEvent) {
            titleText.text = event.buttonTitle
            
            // Format score with + or - sign
            val scoreStr = if (event.scoreDelta > 0) "+${event.scoreDelta}" else "${event.scoreDelta}"
            scoreText.text = scoreStr
            
            // Set color based on positive/negative score
            val scoreColor = if (event.scoreDelta > 0) {
                ContextCompat.getColor(itemView.context, android.R.color.holo_green_dark)
            } else {
                ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark)
            }
            scoreText.setTextColor(scoreColor)
            
            // Format timestamp
            val formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm")
            timestampText.text = event.timestamp.format(formatter)
        }
    }

    class EventDiffCallback : DiffUtil.ItemCallback<ScoreEvent>() {
        override fun areItemsTheSame(oldItem: ScoreEvent, newItem: ScoreEvent): Boolean {
            return oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: ScoreEvent, newItem: ScoreEvent): Boolean {
            return oldItem == newItem
        }
    }
}