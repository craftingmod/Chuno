@echo off

echo ���� ������ ��带 Ű�� USB�� �����ϼ���

adb wait-for-device

adb reboot

echo ������� ��ٸ��� ���Դϴ�

adb wait-for-device

adb shell cmd overlay enable --user 0 net.tarks.craftingmod.chuno

adb shell cmd overlay list

echo �� ��Ͽ��� [x] ǥ�ð� net.tarks.craftingmod.chuno�� üũ�Ǿ��ִ��� Ȯ�����ּ���.

echo �� �Ǿ� ���� ���� �̰� �����Ͻð� �ٽ� �����ּ���.

pause

adb reboot