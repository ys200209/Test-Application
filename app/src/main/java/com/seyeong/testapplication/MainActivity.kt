package com.seyeong.testapplication

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
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
        val adapter = MusicRecyclerAdapter()
        adapter.musicList.addAll(getMusicList())

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun getMusicList(): List<Music> {
        val listUrl = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI // 음원 정보의 주소를 받아온다.

        val proj = arrayOf( // 음원 정보 테이블에서 읽어올 컬럼명
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION
        )

        val cursor = contentResolver.query(listUrl, proj, null, null, null)
        // ContentResolver의 query() 메서드에 주소와 컬럼명을 담아 호츌한다.

        val musicList = mutableListOf<Music>()

        while(cursor?.moveToNext() == true) {
            val id = cursor.getString(0)
            val title = cursor.getString(1)
            val artist = cursor.getString(2)
            val albumId = cursor.getString(3)
            val duration = cursor.getLong(4)

            val music = Music(id, title, artist, albumId, duration)
            musicList.add(music)
        }

        return musicList
    }

}