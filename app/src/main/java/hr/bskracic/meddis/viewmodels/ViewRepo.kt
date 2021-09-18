package hr.bskracic.meddis.viewmodels

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Job

interface ViewRepo<T> {
    fun getById(itemId: Int): LiveData<T>
    fun insert(item: T): Job
    fun update(item: T): Job
    fun delete(item: T): Job
}