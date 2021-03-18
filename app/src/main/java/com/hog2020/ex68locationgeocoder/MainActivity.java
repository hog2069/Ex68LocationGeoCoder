package com.hog2020.ex68locationgeocoder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText etaddress;
    EditText etlat,etlng;

    String address;
    double lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etaddress=findViewById(R.id.et_address);
        etlat=findViewById(R.id.et_lat);
        etlng=findViewById(R.id.et_lng);
    }

    public void clickbtn(View view) {
        //주소--> 좌표로 변환(지오코딩)
        String addr= etaddress.getText().toString();

        //지오코딩 작업을 수행하는 객체 생성
        Geocoder geocoder= new Geocoder(this, Locale.KOREA);

        //지오코딩 정보를 구글서버에서 받아오기 무조건 예외처리 필요-인터넷 퍼미션
        try {
            List<Address> addresses= geocoder.getFromLocationName(addr,3);

            StringBuffer buffer =new StringBuffer();
            for(Address t : addresses){
                buffer.append(t.getLatitude()+","+t.getLongitude()+"\n");
            }

            //그 결과를 다이얼로그에 보이기
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(buffer.toString()).setPositiveButton("확인",null).create().show();


            //구글지도에 보여주기 위해 검색된 위도,경도 중 1개를 멤버변수로 대입
            lat= addresses.get(0).getLatitude();
            lng=addresses.get(0).getLongitude();
            address=addr;
        } catch (IOException e) {
            Toast.makeText(this, "검색 실패", Toast.LENGTH_SHORT).show();
        }
    }

    public void clickbtn2(View view) {
        //좌표--> 주소로 변환(역 지오코딩)
        double latitude=Double.parseDouble(etlat.getText().toString());
        double longitude=Double.parseDouble(etlng.getText().toString());

        //지오코딩해주는 객체생성
        Geocoder geocoder =new Geocoder(this,Locale.KOREA);
        try {
            List<Address> addresses =geocoder.getFromLocation(latitude,longitude,3);

            StringBuffer buffer =new StringBuffer();
            for(Address t: addresses){
                buffer.append(t.getCountryName()+"\n");//나라이름
                buffer.append(t.getCountryCode()+"\n");//나라코드
                buffer.append(t.getPostalCode()+"\n");//우편번호
                buffer.append(t.getAddressLine(0)+"\n");//주소1 : 도로명 건물번호까지
                buffer.append(t.getAddressLine(1)+"\n");//주소2 : 상세주소- 없으면 null
                buffer.append(t.getAddressLine(2)+"\n");//주소3 : 상세주소2- 없으면 null
                buffer.append("-------------------------\n\n");
            }
            new AlertDialog.Builder(this).setMessage(buffer.toString()).setPositiveButton("OK", null).create().show();

        } catch (IOException e) {
            Snackbar.make(view,"실패",Snackbar.LENGTH_SHORT).show();
        }
    }

    public void clickshowmap(View view) {
        //구글지도 앱을 실행하며 검색된 좌표를 보여주기

        Intent intent =new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        //Uri uri= Uri.parse("geo:"+lat+","+lng+"?z=16");//마커없는곳
        Uri uri= Uri.parse("geo:"+lat+","+lng+"?q="+lat+","+lng+"("+address+")"+"&z=10");//마커 있는곳
        intent.setData(uri);

        startActivity(intent);

    }
}