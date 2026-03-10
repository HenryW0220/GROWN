package com.example.myapplication

import android.content.Context
import androidx.room.*

// ==========================================
// Room Entity - stores each plant's care guide
// ==========================================
@Entity(tableName = "care_guides")
data class CareGuideEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val plantName: String,
    val plantAge: String,
    val plantNickname: String,
    val aiAdvice: String,
    val createdAt: Long = System.currentTimeMillis()
)

// ==========================================
// DAO - database access methods
// ==========================================
@Dao
interface CareGuideDao {
    @Query("SELECT * FROM care_guides ORDER BY createdAt DESC")
    suspend fun getAllGuides(): List<CareGuideEntity>

    @Query("SELECT * FROM care_guides WHERE id = :id")
    suspend fun getGuideById(id: Int): CareGuideEntity?

    @Insert
    suspend fun insertGuide(guide: CareGuideEntity): Long

    @Delete
    suspend fun deleteGuide(guide: CareGuideEntity)

    @Query("SELECT COUNT(*) FROM care_guides")
    suspend fun getGuideCount(): Int
}

// ==========================================
// Room Database singleton
// ==========================================
@Database(entities = [CareGuideEntity::class], version = 1, exportSchema = false)
abstract class PlantCareDatabase : RoomDatabase() {
    abstract fun careGuideDao(): CareGuideDao

    companion object {
        @Volatile
        private var INSTANCE: PlantCareDatabase? = null

        fun getDatabase(context: Context): PlantCareDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlantCareDatabase::class.java,
                    "plant_care_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
