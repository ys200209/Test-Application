package com.seyeong.testapplication

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

abstract class BaseActivity: AppCompatActivity() {
    // 반복적인 권한 처리 코드들을 하나로 만들어 각각의 액티비티에서 상속 받아 사용하도록. (효율적)
    // 때문에 독립적으로는 사용되어선 안되며 상속받아 사용되어져야 하므로 추상 클래스로 작성한다.

    abstract fun permissionGranted(requestCode: Int) // requestCode : 권한 요청이 어디서 일어났는지.
    abstract fun permissionDenied(requestCode: Int)

    fun requirePermissions(permissions: Array<String>, requestCode: Int) { // permissions : 권한 배열
        // 권한 요청 시 자식 액티비티에서 직접 호출하는 requirePermissions 메서드
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // M(마시멜로우) 버전 미만이면 permissionGranted 호출
            permissionGranted(requestCode)
        } else {
            val isAllPermissionsGreanted = permissions.all { // 권한이 모두 승인된 것을 확인한다.
                checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }
            // 권한 배열 permissions에는 요청된 권한 배열이 들어있는데 .all 메서드를 통해 배열 속에 들어 있는 모든 값을
            // 체크할 수 있다.

            if (isAllPermissionsGreanted) {
                permissionGranted(requestCode) // 권한이 모두 승인 됐다면 permissionGranted() 호출
            } else {
                ActivityCompat.requestPermissions(this, permissions, requestCode)
                // 권한이 승인되지 않았다면 권한 승인을 요청합니다.
            }
        }
    }

    // 위의 requirePermissions 메서드를 선행하고 사용자가 권한을 승인하거나 거부한 다음에 호출되는 결과 메서드
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            // grantResults에 .all 메서드를 이용하여 결과값이 모두 승인된 것인지를 확인한다.
            permissionGranted(requestCode)
        } else {
            permissionDenied(requestCode)
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}