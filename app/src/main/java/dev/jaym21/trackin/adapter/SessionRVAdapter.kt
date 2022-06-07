package dev.jaym21.trackin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.jaym21.trackin.R
import dev.jaym21.trackin.model.Session
import dev.jaym21.trackin.util.Utilities

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
        val root: ConstraintLayout = itemView.findViewById(R.id.clSessionRoot)
        val image: ImageView = itemView.findViewById(R.id.ivSessionImage)
        val date: TextView = itemView.findViewById(R.id.tvSessionDate)
        val distance: TextView = itemView.findViewById(R.id.tvSessionDistance)
        val speed: TextView = itemView.findViewById(R.id.tvSessionAverageSpeed)
        val divider: View = itemView.findViewById(R.id.divider)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        return SessionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.session_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.apply {
            Glide.with(itemView.context).load(currentItem.sessionImage).into(image)

            val formattedDate = Utilities.convertDateFormat(currentItem.timestamp)
            date.text = formattedDate

            val distanceInKm = currentItem.distanceInMeters / 1000L

            if (distanceInKm == 0L) {
                distance.text = "${currentItem.distanceInMeters} m"
            } else {
                distance.text = "$distanceInKm km"
            }

            speed.text = "${currentItem.avgSpeedInKMH} km/h"

            root.setOnClickListener {
                listener.onSessionClick(currentItem)
            }

            if (position == currentList.size - 1) {
                divider.visibility = View.INVISIBLE
            }
        }
    }
}

interface ISessionRVAdapter {
    fun onSessionClick(session: Session)
}