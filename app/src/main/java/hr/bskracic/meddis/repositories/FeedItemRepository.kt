package hr.bskracic.meddis.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import hr.bskracic.meddis.data.dao.FeedItemDao
import hr.bskracic.meddis.data.model.FeedItem
import hr.bskracic.meddis.data.model.FeedItemWithTherapyAndAlarms
import hr.bskracic.meddis.data.model.TherapyAndMedicationsWithAlarms

class FeedItemRepository(private val feedItemDao: FeedItemDao) {
    val feedItems = feedItemDao.getAll()

    @WorkerThread
    fun getAllWithAll(): LiveData<List<FeedItemWithTherapyAndAlarms>> {
        return feedItemDao.getAllWithAll()
    }

    @WorkerThread
    fun getByChecked(isChecked: Boolean): LiveData<List<FeedItem>> {
        return feedItemDao.getByChecked(isChecked)
    }

    @WorkerThread
    fun getByCheckedWithAll(isChecked: Boolean): LiveData<List<FeedItemWithTherapyAndAlarms>> {
        return feedItemDao.getByCheckedWithAll(isChecked)
    }

    @WorkerThread
    fun getById(feedItemId: Int): LiveData<FeedItem> {
        return feedItemDao.getById(feedItemId)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun insert(feedItem: FeedItem) {
        feedItemDao.insert(feedItem)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun update(feedItem: FeedItem) {
        feedItemDao.update(feedItem)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun delete(feedItem: FeedItem) {
        feedItemDao.delete(feedItem)
    }
}