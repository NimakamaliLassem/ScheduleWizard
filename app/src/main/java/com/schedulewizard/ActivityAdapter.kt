package com.schedulewizard

import android.content.Context
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DoubleTapListener(context: Context, private val doubleTapListener: OnDoubleTapListener) : View.OnTouchListener {
    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(motionEvent)
    }

    interface OnDoubleTapListener {
        fun onDoubleTap()
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            doubleTapListener.onDoubleTap()
            return true
        }
    }
}

class ActivityAdapter(private val activities: List<String>) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {
    private var itemDoubleTapListener: ItemDoubleTapListener? = null

    interface ItemDoubleTapListener {
        fun onItemDoubleTap(activity: String)
    }

    fun setItemDoubleTapListener(listener: ItemDoubleTapListener) {
        itemDoubleTapListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)
        return ActivityViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activityName = activities[position]
        holder.bind(activityName)
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val activityNameTextView: TextView = itemView.findViewById(R.id.activity_name_text_view)

        fun bind(activityName: String) {
            activityNameTextView.text = activityName

            itemView.setOnClickListener {
                // Handle single-tap event if needed
            }

            itemView.setOnTouchListener(DoubleTapListener(itemView.context, object : DoubleTapListener.OnDoubleTapListener {
                override fun onDoubleTap() {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val activity = activities[position]
                        itemDoubleTapListener?.onItemDoubleTap(activity)
                    }
                }
            }))
        }
    }
}

