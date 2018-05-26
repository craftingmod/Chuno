@echo off

echo 폰의 개발자 모드를 키고 USB와 연결하세요

adb wait-for-device

adb shell pm grant net.tarks.craftingmod.chuno android.permission.WRITE_SECURE_SETTINGS

echo 완료되었습니다.

pause