package ru.voxp.android.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.voxp.android.BR
import ru.voxp.android.R

class LastLawsAdapter : Adapter<LawsListItem>() {

    private lateinit var items: List<LawListState>

    fun setItems(items: List<LawListState>) {
        this.items = items
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawsListItem {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.law_list_view, parent, false)
        return LawsListItem(view)
    }

    override fun onBindViewHolder(holder: LawsListItem, position: Int) {
        holder.setState(items[position])
    }
}

class LawsListItem(itemView: View) : ViewHolder(itemView) {
    private val binding: ViewDataBinding? = DataBindingUtil.bind(itemView)

    fun setState(law: Any) {
        binding?.setVariable(BR.state, law)
    }
}