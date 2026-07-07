import sys

with open("app/src/main/AndroidManifest.xml", "r") as f:
    content = f.read()

perm = '<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />\n'
if "POST_NOTIFICATIONS" not in content:
    idx = content.find("<application")
    content = content[:idx] + perm + "    " + content[idx:]
    with open("app/src/main/AndroidManifest.xml", "w") as f:
        f.write(content)
