@echo off

echo ���� ������ ��带 Ű�� USB�� �����ϼ���

adb wait-for-device

adb shell cmd overlay disable --user 0 net.tarks.craftingmod.chuno

adb shell cmd overlay list

echo �� ��Ͽ��� [ ] ǥ�ð� net.tarks.craftingmod.chuno�� �ִ��� Ȯ�����ּ���.

echo �� �Ǿ� ���� ���� �̰� �����Ͻð� �ٽ� �����ּ���.

pause

adb reboot