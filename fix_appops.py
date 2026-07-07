import sys

with open("app/src/main/java/com/example/managers/LocationTrackerManager.kt", "r") as f:
    content = f.read()

check_code = """
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
        val mode = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(android.app.AppOpsManager.OPSTR_FINE_LOCATION, android.os.Process.myUid(), context.packageName)
        } else {
            appOps.checkOpNoThrow(android.app.AppOpsManager.OPSTR_FINE_LOCATION, android.os.Process.myUid(), context.packageName)
        }
        if (mode != android.app.AppOpsManager.MODE_ALLOWED) {
            return
        }
"""
idx = content.find("val locationManager =")
if "OPSTR_FINE_LOCATION" not in content and idx != -1:
    content = content[:idx] + check_code + "        " + content[idx:]

with open("app/src/main/java/com/example/managers/LocationTrackerManager.kt", "w") as f:
    f.write(content)
print("Added AppOps check")
