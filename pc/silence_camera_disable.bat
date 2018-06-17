@echo off

echo 폰의 개발자 모드를 키고 USB와 연결하세요

adb wait-for-device

adb shell cmd overlay disable --user 0 net.tarks.craftingmod.chuno

adb shell cmd overlay list

echo 위 목록에서 [ ] 표시가 net.tarks.craftingmod.chuno에 있는지 확인해주세요.

echo 안 되어 있을 때는 이걸 종료하시고 다시 돌려주세요.

pause

adb reboot