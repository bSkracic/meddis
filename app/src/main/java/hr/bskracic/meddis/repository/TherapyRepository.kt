package hr.bskracic.meddis.repository

import hr.bskracic.meddis.data.dao.TherapyDao
import hr.bskracic.meddis.data.model.Therapy
import kotlinx.coroutines.flow.Flow

class TherapyRepository(private val therapyDao: TherapyDao) {
    val therapies: Flow<List<Therapy>> = therapyDao.getAll()

}