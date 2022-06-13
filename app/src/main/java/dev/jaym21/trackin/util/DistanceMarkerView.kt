package dev.jaym21.trackin.util

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import dev.jaym21.trackin.R

class DistanceMarkerView(context: Context, layoutResource: Int): MarkerView(context, layoutResource) {

    private var mOffset: MPPointF? = null

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)

        val date = findViewById<TextView>(R.id.tvDistanceDate)
        val distance = findViewById<TextView>(R.id.tvDistanceValue)

        date.text = "${e?.x?.let { Utilities.convertDateFormat(it.toLong()) }}"

        val distanceInKm = e?.y?.div(1000L)

        distance.text = "$distanceInKm km"
    }

    override fun getOffset(): MPPointF {
        if (mOffset == null) {
            mOffset = MPPointF((-(width/2)).toFloat(), (-height).toFloat())
        }
        return mOffset as MPPointF
    }
}