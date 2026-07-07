import sys

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# request POST_NOTIFICATIONS permission
permission_block = """
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
"""
idx = content.find("if (permissionsToRequest.isNotEmpty())")
if "POST_NOTIFICATIONS" not in content and idx != -1:
    content = content[:idx] + permission_block + "        " + content[idx:]

# Schedule WorkManager
schedule_call = """
        com.example.workers.WorkManagerUtils.scheduleEveningReminder(this)
"""
idx_enable = content.find("enableEdgeToEdge()")
if "scheduleEveningReminder" not in content and idx_enable != -1:
    content = content[:idx_enable] + schedule_call + "        " + content[idx_enable:]

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
print("Updated MainActivity.kt")
