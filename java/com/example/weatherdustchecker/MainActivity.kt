package com.example.weatherdustchecker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//      상단 제목 표시줄 숨기기
        supportActionBar?.hide()

//      ToDo : WeatherPageFragment 표시하기 (FrameLayout에 넣어주기)
        val transaction = supportFragmentManager.beginTransaction()
//      TODO : newInstance 클래스 메서드 정의해서 staturs값 (문자열)
//      temperature 값(Double) 전달할 수 있도록 해주기
//      해당 값은 모두 프래그먼트 번들 객체에 저장되어야함

//        transaction.add(R.id.fragment_container,
//            WeatherPageFragment.newInstance(37.58, 126.98)) //위도 경도
//

        transaction.add(R.id.fragment_container, DustTestFragment.newInstance(37.58, 126.98))
            //instance는 생성 후 쓰는 것!

        transaction.commit()

    }


}