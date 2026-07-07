import sys

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

if "import android.os.Build" not in content:
    content = content.replace("import android.os.Bundle", "import android.os.Bundle\nimport android.os.Build")
    with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
        f.write(content)

with open("app/src/main/java/com/example/workers/ReminderWorker.kt", "r") as f:
    worker = f.read()

worker = worker.replace("import com.example.data.SoloDatabase", "import com.example.data.SoloDatabase\nimport androidx.room.Room")
worker = worker.replace("SoloDatabase.getDatabase(context)", 'Room.databaseBuilder(context.applicationContext, SoloDatabase::class.java, "solo_db").fallbackToDestructiveMigration().build()')

with open("app/src/main/java/com/example/workers/ReminderWorker.kt", "w") as f:
    f.write(worker)

print("Fixed imports and database creation")
