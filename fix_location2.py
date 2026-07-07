import sys

with open("app/src/main/java/com/example/managers/LocationTrackerManager.kt", "r") as f:
    content = f.read()

# Add check for android:monitor_location
check_code = """
        val modeMonitor = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow("android:monitor_location", android.os.Process.myUid(), context.packageName)
        } else {
            appOps.checkOpNoThrow("android:monitor_location", android.os.Process.myUid(), context.packageName)
        }
        if (modeMonitor != android.app.AppOpsManager.MODE_ALLOWED) {
            return
        }
"""
idx = content.find("val locationManager =")
if "modeMonitor" not in content and idx != -1:
    content = content[:idx] + check_code + "        " + content[idx:]

with open("app/src/main/java/com/example/managers/LocationTrackerManager.kt", "w") as f:
    f.write(content)
print("Added monitor_location check")
