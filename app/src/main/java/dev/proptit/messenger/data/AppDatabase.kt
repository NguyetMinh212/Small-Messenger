package dev.proptit.messenger.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.proptit.messenger.BuildConfig
import dev.proptit.messenger.R
import dev.proptit.messenger.data.contact.dao.ContactDao
import dev.proptit.messenger.data.contact.entity.ContactEntity
import dev.proptit.messenger.data.messge.dao.MessageDao
import dev.proptit.messenger.data.messge.entity.MessageEntity

@Database(
    entities = [ContactEntity::class, MessageEntity::class],
    version =2,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase(){

    abstract fun contactDao(): ContactDao
    abstract fun messageDao(): MessageDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase?= null

        val path = "android.resource://${BuildConfig.APPLICATION_ID}/"

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                ).allowMainThreadQueries().addCallback(object : Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
//                        db.execSQL("Insert into contact (avatar, name) values ('${path + R.drawable.image_people5}', 'Joshua Lawrence')")
//                        db.execSQL("Insert into contact (avatar, name) values ('${path + R.drawable.image_people3}', 'Karen Castillo')")
//                        db.execSQL("Insert into contact (avatar, name) values ('${path + R.drawable.image_people1}', 'Martin Randolph')")
//                        db.execSQL("Insert into contact (avatar, name) values ('${path + R.drawable.image_people2}', 'Andrew Parker')")
                    }
                }).build()
                INSTANCE  = instance
                instance
            }
        }
    }
}