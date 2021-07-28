package hr.bskracic.meddis.data.dao

import androidx.room.*
import hr.bskracic.meddis.data.model.Collection
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
    @Query("SELECT * FROM collections")
    fun getAll(): Flow<List<Collection>>

    @Query("SELECT * FROM collections WHERE id IN (:collectionIds)")
    fun getByIds(collectionIds: IntArray): Flow<List<Collection>>

//    @Transaction
//    @Query("SELECT * FROM collections WHERE id IN (:collectionIds)")
//    fun getTherapyForCollection(collectionIds: IntArray): Flow<List<CollectionWithTherapies>>

    @Update
    fun update(vararg collections: Collection)

    @Insert
    fun insert(vararg collections: Collection)

    @Delete
    fun delete(collection: Collection)
}