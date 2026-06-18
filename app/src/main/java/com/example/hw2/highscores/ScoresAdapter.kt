package com.example.hw2.highscores

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hw2.R
import com.example.hw2.data.HighScore
import com.example.hw2.databinding.ItemHighScoreBinding
import java.text.DateFormat
import java.util.Date

/**
 * Renders the top-ten high scores. Tapping a row notifies [onClick] so the map
 * fragment can re-centre on that score's location.
 */
class ScoresAdapter(
    private val onClick: (HighScore) -> Unit,
) : RecyclerView.Adapter<ScoresAdapter.ScoreViewHolder>() {

    private val items = mutableListOf<HighScore>()
    private var selectedId: Long = -1L

    @SuppressLint("NotifyDataSetChanged")
    fun submit(scores: List<HighScore>) {
        items.clear()
        items.addAll(scores)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelected(id: Long) {
        if (selectedId == id) return
        selectedId = id
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val binding = ItemHighScoreBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ScoreViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        holder.bind(items[position], position + 1, items[position].id == selectedId)
    }

    inner class ScoreViewHolder(
        private val binding: ItemHighScoreBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HighScore, rank: Int, selected: Boolean) {
            val ctx = binding.root.context
            binding.txtRank.text = ctx.getString(R.string.rank_format, rank)
            binding.txtScore.text = ctx.getString(R.string.score_format, item.score)
            binding.txtDetails.text = ctx.getString(
                R.string.score_details_format, item.distance, item.coins
            )
            binding.txtDate.text = DateFormat
                .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                .format(Date(item.timestamp))
            binding.root.isSelected = selected
            binding.root.setBackgroundResource(
                if (selected) R.color.row_selected else android.R.color.transparent
            )
            binding.root.setOnClickListener { onClick(item) }
        }
    }
}
