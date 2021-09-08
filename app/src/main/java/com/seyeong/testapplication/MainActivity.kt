package com.seyeong.testapplication

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.seyeong.testapplication.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        requirePermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 999) // 외부 저장소 권한 요청
    }

   override fun permissionGranted(requestCode: Int) {
        startProcess() // 아무것도 없는 startProcess() 메서드
    }

    override fun permissionDenied(requestCode: Int) {
        Toast.makeText(this, "외부 저장소 권한 승인이 필요합니다. 앱을 종료합니다.", Toast.LENGTH_LONG).show()
        finish()
    }

    fun startProcess() {

    }
}