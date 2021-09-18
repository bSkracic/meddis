package hr.bskracic.meddis.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import hr.bskracic.meddis.data.model.FeedItem
import hr.bskracic.meddis.data.model.FeedItemWithTherapyAndAlarms
import hr.bskracic.meddis.data.model.TherapyAndMedicationsWithAlarms

@Dao
interface FeedItemDao {

    @Query("SELECT * FROM feeditems")
    fun getAll(): LiveData<FeedItem>

    @Transaction
    @Query("SELECT * FROM feeditems WHERE feedItems.is_checked=:isChecked")
    fun getByCheckedWithAll(isChecked: Boolean): LiveData<List<FeedItemWithTherapyAndAlarms>>

    @Query("SELECT * FROM feeditems WHERE feeditems.is_checked=:isChecked ORDER BY feedItems.timestamp DESC")
    fun getByChecked(isChecked: Boolean): LiveData<List<FeedItem>>

    @Query("SELECT * FROM feeditems WHERE feeditems.id=:feedItemId")
    fun getById(feedItemId: Int): LiveData<FeedItem>

    @Insert
    fun insert(vararg feedItem: FeedItem)

    @Update
    fun update(vararg feedItem: FeedItem)

    @Delete
    fun delete(feedItem: FeedItem)
}