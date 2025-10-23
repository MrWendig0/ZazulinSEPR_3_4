import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.zazulinsepr_1_2.data.Note
import com.example.zazulinsepr_1_2.data.NoteDao

/**
 * Главный класс базы данных
 *
 * @Database - помечает класс как базу данных Room
 * entities = [Note::class] - подключаем нашу таблицу заметок
 * version = 1 - версия базы (увеличиваем когда меняем структуру)
 */
@Database(
    entities = [Note::class],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {
    // Подключаем наш DAO чтобы работать с заметками
    abstract fun noteDao(): NoteDao
    companion object {
        /**
         * Singleton - создаем базу данных только один раз
         */
        fun getDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context, // Контекст приложения
                AppDatabase::class.java, // Класс базы данных
                "notes_database" // Имя файла базы на телефоне
            ).build()
        }
    }
}