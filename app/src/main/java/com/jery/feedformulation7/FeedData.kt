package com.jery.feedformulation7

class FeedData {

    companion object {
        private var feedData: FeedData? = null

        fun getInstance(): FeedData? {
            if (feedData == null) feedData = FeedData()
            return feedData
        }

        fun setInstance(feed: FeedData?) {
            feedData = feed
        }
    }


    var feedsSelected = ArrayList<Int>()
    var feedCost = ArrayList<Double>()

    fun addFeedsSelected(x: Int) {
        feedsSelected.add(x)
    }

    fun removeAllFeedsSelected() {
        feedsSelected.clear()
        feedCost.clear()
    }

    fun addCostEntered(x: Double) {
        feedCost.add(x)
    }

    fun done() {
        //this.feedsSelected = this.feedsSelected.substring(0, this.feedsSelected.length() - 1);
        //this.feedCost = this.feedCost.substring(0, this.feedCost.length() - 1);
    }

    fun names(): String {
        val sb = StringBuilder()
        for (s in feedsSelected) {
            sb.append(s)
            sb.append("\t")
        }
        return sb.toString()
    }

    fun costs(): String {
        val sb = StringBuilder()
        for (s in feedCost) {
            sb.append(s)
            sb.append("\t")
        }
        return sb.toString()
    }
}