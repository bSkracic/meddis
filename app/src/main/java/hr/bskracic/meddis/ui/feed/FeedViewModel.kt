package hr.bskracic.meddis.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hr.bskracic.meddis.data.model.FeedItem
import hr.bskracic.meddis.repositories.FeedItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedViewModel(private val feedItemRepo: FeedItemRepository) : ViewModel() {
    val allFeedItemsChecked = feedItemRepo.getByCheckedWithAll(true)
    val allFeedItemsUnchecked = feedItemRepo.getByCheckedWithAll(false)
    val allFeedItems = feedItemRepo.getAllWithAll()

    fun update(feedItem: FeedItem) = CoroutineScope(Dispatchers.IO).launch {
        feedItemRepo.update(feedItem)
    }
}

class FeedViewModelFactory(private val feedItemRepo: FeedItemRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FeedViewModel(feedItemRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}