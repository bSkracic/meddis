package hr.bskracic.meddis.ui.feed

import android.app.NotificationManager
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.bskracic.meddis.MeddisApplication
import hr.bskracic.meddis.R
import hr.bskracic.meddis.adapters.FeedItemAdapter
import hr.bskracic.meddis.data.model.FeedItem
import hr.bskracic.meddis.data.model.FeedItemWithTherapyAndAlarms
import hr.bskracic.meddis.databinding.FragmentFeedBinding
import java.util.*

class FeedFragment : Fragment(R.layout.fragment_feed) {

    val viewModel: FeedViewModel by viewModels {
        FeedViewModelFactory((activity?.application as MeddisApplication).feedItemRepository)
    }

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val adapter = FeedItemAdapter(object : FeedItemAdapter.FeedItemListener {
            override fun onFeedItemChecked(feedItem: FeedItem) {
                feedItem.isChecked = true
                viewModel.update(feedItem)
            }
        })

        binding.feedToday.text = "Terapija odrađeno\n${DateFormat.format("dd/MM/yyyy", Date())}"

//        val checkedAdapter = FeedItemAdapter(object : FeedItemAdapter.FeedItemListener {
//            override fun onFeedItemChecked(feedItem: FeedItem) {
//            }
//        })

        view.findViewById<RecyclerView>(R.id.recyclerView_feeds).apply {
            this.adapter = adapter
            this.layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }

        viewModel.allFeedItems.observe(viewLifecycleOwner, {
            var todayUnchecked = mutableListOf<FeedItemWithTherapyAndAlarms>()
            var todayAllCount = 0
            val now = Date()
            for (item in it) {
                val timestamp = item.feedItem.timestamp
                if (DateFormat.format("MM", now).equals(DateFormat.format("MM", timestamp))
                    && DateFormat.format("dd", now).equals(DateFormat.format("dd", timestamp))
                ) {
                    todayAllCount++
                    if(!item.feedItem.isChecked) {
                      todayUnchecked.add(item)
                    }
                }
            }
            val todayCheckedCount = todayAllCount - todayUnchecked.size
            if(todayAllCount != 0) {
                binding.feedProgressBar.progress = ((todayCheckedCount.toFloat() / todayAllCount.toFloat()) * 100).toInt()
                binding.feedProgressText.text = "$todayCheckedCount / $todayAllCount"
            } else {
                binding.feedProgressBar.progress = 100
                binding.feedProgressText.text = "Slobodno vrijeme!"
            }
            adapter.submitList(todayUnchecked)
        })

//        view.findViewById<RecyclerView>(R.id.recyclerView_feeds_checked).apply {
//            this.adapter = checkedAdapter
//            this.layoutManager = object : LinearLayoutManager(context) {
//                override fun canScrollVertically(): Boolean {
//                    return false
//                }
//            }
//        }


            viewModel.allFeedItemsUnchecked.observe(viewLifecycleOwner, {
                adapter.submitList(it)
            })

//        viewModel.allFeedItemsChecked.observe(viewLifecycleOwner, {
//            checkedAdapter.submitList(it)
//        })

        // Cancel all notifications
        context?.let {
            (ContextCompat.getSystemService(
                it,
                NotificationManager::class.java
            ) as NotificationManager).cancelAll()
        }
    }

}