package hr.bskracic.meddis.ui.feed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.bskracic.meddis.MeddisApplication
import hr.bskracic.meddis.R
import hr.bskracic.meddis.adapters.FeedItemAdapter
import hr.bskracic.meddis.data.model.FeedItem

class FeedFragment : Fragment(R.layout.fragment_feed) {

    val viewModel: FeedViewModel by viewModels {
        FeedViewModelFactory((activity?.application as MeddisApplication).feedItemRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val adapter = FeedItemAdapter(object : FeedItemAdapter.FeedItemListener {
            override fun onFeedItemChecked(feedItem: FeedItem) {
                feedItem.isChecked = true
                viewModel.update(feedItem)
            }
        })

        val checkedAdapter = FeedItemAdapter(object : FeedItemAdapter.FeedItemListener {
            override fun onFeedItemChecked(feedItem: FeedItem) {
            }
        })

        view.findViewById<RecyclerView>(R.id.recyclerView_feeds).apply {
            this.adapter = adapter
            this.layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }

        view.findViewById<RecyclerView>(R.id.recyclerView_feeds_checked).apply {
            this.adapter = checkedAdapter
            this.layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }

        viewModel.allFeedItemsUnchecked.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        viewModel.allFeedItemsChecked.observe(viewLifecycleOwner, {
            checkedAdapter.submitList(it)
        })
    }

}