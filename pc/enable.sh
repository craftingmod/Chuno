#!/bin/sh
echo "폰의 개발자 모드를 키고 USB와 연결하세요"
adb wait-for-device;
adb shell "pm grant \"net.tarks.craftingmod.chuno\" \"android.permission.WRITE_SECURE_SETTINGS\"";
adb shell "cmd overlay enable --user 0 net.tarks.craftingmod.chuno";
adb shell "cmd overlay enable --user 0 net.tarks.craftingmod.chuno";
adb shell "cmd overlay list";
echo "[x] net.tarks.craftingmod.chuno 가 있는지 확인해주세요."
echo "[ ] 처럼 x가 안됐을때는 이걸 종료하시고 다시 돌려주세요."
read -p "..." -n1 -s;
adb reboot;