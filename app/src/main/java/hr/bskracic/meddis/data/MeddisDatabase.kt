package hr.bskracic.meddis.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import hr.bskracic.meddis.data.dao.AlarmDao
import hr.bskracic.meddis.data.dao.MedicationDao
import hr.bskracic.meddis.data.dao.TherapyDao
import hr.bskracic.meddis.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Medication::class, Therapy::class, Alarm::class], version = 1)
@TypeConverters(RepeatTypeConverter::class)
abstract class MeddisDatabase : RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
    abstract fun therapyDao(): TherapyDao
    abstract fun alarmDao(): AlarmDao

    private class MeddisDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val medicationDao = database.medicationDao()
                    val therapyDao = database.therapyDao()
                    val alarmDao = database.alarmDao()

                    medicationDao.deleteAll()

                    medicationDao.insert(
                        Medication(0, "lijek 1", "novi lijek", 10, 10, "tableta"),
                        Medication(0, "lijek 2", "stari lijek", 100, 100, "tableta")
                        )

                    therapyDao.insert(
                        Therapy(0, 1, 1),
                        Therapy(0, 2, 1)
                    )

                    alarmDao.insert(
                        Alarm(0, 1, "8:00", RepeatType.DAILY),
                        Alarm(0, 1, "20:00", RepeatType.DAILY),
                        Alarm(0, 2, "15:00", RepeatType.DAILY)
                    )

                }
            }
        }
    }

        companion object {
            // Singleton prevents multiple instances of database opening at the
            // same time.
            @Volatile
            private var INSTANCE: MeddisDatabase? = null

            fun getDatabase(context: Context, scope: CoroutineScope): MeddisDatabase {
                // if the INSTANCE is not null, then return it,
                // if it is, then create the database
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MeddisDatabase::class.java,
                        "meddis_database"
                    )
                        .addCallback(MeddisDatabaseCallback(scope))
                        .build()
                    INSTANCE = instance
                    // return instance
                    instance
                }
            }
        }
}