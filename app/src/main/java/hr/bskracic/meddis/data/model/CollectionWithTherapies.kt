package hr.bskracic.meddis.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class CollectionWithTherapies(
    @Embedded val collection: Collection,
    @Relation(
        parentColumn = "id",
        entityColumn = "collection_id"
    )
    val therapies: List<Therapy>
)