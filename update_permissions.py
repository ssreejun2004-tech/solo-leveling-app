import sys

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Let's remove the ActivityCompat.requestPermissions logic in onCreate
idx = content.find("val permissionsToRequest = mutableListOf<String>()")
end_idx = content.find("com.example.workers.WorkManagerUtils.scheduleEveningReminder(this)")

if idx != -1 and end_idx != -1:
    content = content[:idx] + content[end_idx:]

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
print("Removed old permission logic")
