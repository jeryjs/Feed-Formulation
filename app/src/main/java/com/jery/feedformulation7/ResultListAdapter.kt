package com.jery.feedformulation7

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.*

class ResultListAdapter(feed: ArrayList<ResultList>, context: Context) :
    ArrayAdapter<ResultList>(context, R.layout.result_list, feed) {

    private var feedSet: ArrayList<ResultList> = feed
    private var mContext: Context = context

    private inner class ViewHolder {
        var name: TextView? = null
        var cost: TextView? = null
        var amount: TextView? = null
        var total: TextView? = null
    }

    private var lastPosition = -1

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val viewHolder: ViewHolder
        val result: View

        if (convertView == null) {
            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.result_list, parent, false)
            viewHolder.name = convertView.findViewById(R.id.feed_name)
            viewHolder.cost = convertView.findViewById(R.id.feed_cost)
            viewHolder.amount = convertView.findViewById(R.id.feed_weight)
            viewHolder.total = convertView.findViewById(R.id.feed_total)

            result = convertView

            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
            result = convertView
        }

        lastPosition = position

        val feedModel = getItem(position)

        viewHolder.name?.text = feedModel?.name
        if (feedModel?.amount != -1.0) {
            viewHolder.cost?.visibility = View.VISIBLE
            viewHolder.amount?.visibility = View.VISIBLE

            viewHolder.cost?.text = "${context.getString(R.string.cost)} = ${feedModel?.cost}"
            viewHolder.amount?.text = "${context.getString(R.string.weight)} = ${feedModel?.amount}"
            viewHolder.total?.text = "${context.getString(R.string.total)} = ${feedModel?.total}"
        } else {
            Log.d("Cost: ", feedModel.amount.toString())
            viewHolder.name?.setTypeface(Typeface.MONOSPACE, Typeface.BOLD)
            viewHolder.total?.setTypeface(null, Typeface.BOLD)
            viewHolder.cost?.visibility = View.GONE
            viewHolder.amount?.visibility = View.GONE
            viewHolder.total?.text = feedModel.cost.toString()
        }

        return convertView!!
    }
}
