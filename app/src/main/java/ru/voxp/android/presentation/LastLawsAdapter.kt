package ru.voxp.android.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.voxp.android.R
import ru.voxp.android.data.api.model.Law

class LastLawsAdapter : Adapter<LawsListItem>() {

    private lateinit var items: List<Law>

    fun setItems(items: List<Law>) {
        this.items = items
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawsListItem {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.law_list_view, parent, false)
        return LawsListItem(view)
    }

    override fun onBindViewHolder(holder: LawsListItem, position: Int) {
        holder.bind(items[position])
    }
}

class LawsListItem(itemView: View) : ViewHolder(itemView) {

    private val title: TextView = itemView.findViewById(R.id.law_list_view_title)
    private val subtitle: TextView = itemView.findViewById(R.id.law_list_view_subtitle)
    private val date: TextView = itemView.findViewById(R.id.law_list_view_date)

    fun bind(law: Law) {
        title.text = law.name
        if (law.comments == null) {
            subtitle.visibility = View.GONE
        } else {
            subtitle.visibility = View.VISIBLE
            subtitle.text = law.comments
        }
        if (law.introductionDate == null) {
            date.visibility = View.GONE
        } else {
            date.text = law.introductionDate
            date.visibility = View.VISIBLE
        }
    }
}