package dev.jaym21.trackin.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.jaym21.trackin.model.Session

class SessionRVAdapter(private val listener: ISessionRVAdapter): ListAdapter<Session, SessionRVAdapter.SessionViewHolder>(SessionDiffUtil()) {

    class SessionDiffUtil: DiffUtil.ItemCallback<Session>() {
        override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Session, newItem: Session): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    inner class SessionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {

    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {

    }
}

interface ISessionRVAdapter {
    fun onSessionClick(session: Session)
}