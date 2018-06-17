@echo off

echo 폰의 개발자 모드를 키고 USB와 연결하세요

adb wait-for-device

adb reboot

echo 재부팅을 기다리는 중입니다

adb wait-for-device

adb shell cmd overlay enable --user 0 net.tarks.craftingmod.chuno

adb shell cmd overlay list

echo 위 목록에서 [x] 표시가 net.tarks.craftingmod.chuno에 체크되어있는지 확인해주세요.

echo 안 되어 있을 때는 이걸 종료하시고 다시 돌려주세요.

pause

adb reboot