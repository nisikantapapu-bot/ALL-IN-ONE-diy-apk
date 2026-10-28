package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.ShopDao
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.CustomerProfileEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.StoreConfigEntity
import com.example.data.local.entity.WishlistEntity

@Database(
    entities = [
        ProductEntity::class,
        CategoryEntity::class,
        OrderEntity::class,
        StoreConfigEntity::class,
        CustomerProfileEntity::class,
        WishlistEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun shopDao(): ShopDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "all_in_one_shop.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
