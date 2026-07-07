import sys

with open("app/src/main/java/com/example/managers/LocationTrackerManager.kt", "r") as f:
    content = f.read()

check_code = """
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) && 
            !locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
            return
        }
"""
idx = content.find("try {")
if "isProviderEnabled" not in content and idx != -1:
    content = content[:idx] + check_code + "        " + content[idx:]

with open("app/src/main/java/com/example/managers/LocationTrackerManager.kt", "w") as f:
    f.write(content)
print("Added location provider check")
