#!/bin/sh
echo "폰의 개발자 모드를 키고 USB와 연결하세요"
adb wait-for-device;
adb shell "cmd overlay disable --user 0 net.tarks.craftingmod.chuno";
adb shell "cmd overlay list";
echo "[ ] net.tarks.craftingmod.chuno 가 있는지 확인해주세요."
echo "[x] 처럼 x가 됐을때는 이걸 종료하시고 다시 돌려주세요."
read -p "..." -n1 -s;
adb reboot;