@echo off

echo ���� ������ ��带 Ű�� USB�� �����ϼ���

adb wait-for-device

adb shell pm grant net.tarks.craftingmod.chuno android.permission.WRITE_SECURE_SETTINGS

echo �Ϸ�Ǿ����ϴ�.

pause