package hr.bskracic.meddis.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collections")
data class Collection (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name="name") val name: String
        )