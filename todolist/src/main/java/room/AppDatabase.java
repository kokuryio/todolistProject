package room;


import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import models.Todo;

/**This code is copied from Prof. Dr.-Ing. Martin Schafföner from Brandenburg
 * University of Applied Sciences (martin.schaffoener@th-brandenburg.de), and adjusted to this particular application only
 * by changing certain variable names
 */
@Database(entities = {Todo.class}, version=4)
@TypeConverters(LocalDateTimeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase sInstance;

    public abstract RoomTodoDAO todoDao();

    public static AppDatabase getInstance(final Context aContext) {
        if ( sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(aContext.getApplicationContext(), AppDatabase.class, "todo-database").fallbackToDestructiveMigration().build();
                }
            }
        }
        return sInstance;
    }
}
