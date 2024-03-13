package dev.proptit.messenger

import android.app.Application
import android.content.Context
import com.orhanobut.hawk.Hawk
import dev.proptit.messenger.data.AppDatabase

class MainApplication: Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    companion object {
        @Volatile // giúp các thread luôn đọc giá trị mới nhất của INSTANCE
        private lateinit var INSTANCE: MainApplication

        @Synchronized // tại mỗi thời điểm, chỉ có một luồng thực thi có thể nhập khối mã này => đảm bảo cơ sở dữ liệu chỉ được khởi tạo một lần.
        fun getInstance(): MainApplication {
            if (!this::INSTANCE.isInitialized) {
                INSTANCE = MainApplication()
                return INSTANCE
            }
            return INSTANCE
        }

        fun context(): Context {
            return getInstance().applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Hawk.init(this).build()
    }
}