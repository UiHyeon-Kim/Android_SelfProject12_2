package com.example.selfproject12_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    myDBHelper myHelper;    // 정의한 DB 클래스의 멤버변수
    EditText edtName, edtNumber, edtNameResult, edtNumberResult;
    Button btnInit, btnInsert, btnUpdate, btnDelet, btnSelect;
    SQLiteDatabase sqlDB;   // DB 컨트롤 멤버변수
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setIcon(R.drawable.firefox);
        setTitle("가수 그룹 관리 DB(수정)");

        edtName = (EditText) findViewById(R.id.edtName);
        edtNumber = (EditText) findViewById(R.id.edtNumber);
        edtNameResult = (EditText) findViewById(R.id.edtNameResult);
        edtNumberResult = (EditText) findViewById(R.id.edtNumberResult);
        btnInit = (Button) findViewById(R.id.btnInit);
        btnInsert = (Button) findViewById(R.id.btnInsert);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelet = (Button) findViewById(R.id.btnDelete);
        btnSelect =(Button) findViewById(R.id.btnSelect);

        myHelper = new myDBHelper(this);    //DB 객체 생성

        btnInit.setOnClickListener(new View.OnClickListener() {     // 초기화 버튼
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase(); // 쓰기용으로 열기
                myHelper.onUpgrade(sqlDB, 1, 2);
                sqlDB.close();
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {   // 입력 버튼
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO groupTBL VALUES ('"
                    + edtName.getText().toString()+ "' , "
                    + edtNumber.getText().toString()+ ");");
                sqlDB.close();
                Toast.makeText(getApplicationContext(), "입력됨", Toast.LENGTH_SHORT).show();
                btnSelect.callOnClick();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {   // 수정 버튼
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("UPDATE groupTBL SET gNumber ="
                        + edtNumber.getText() + " WHERE gName = '"
                        + edtName.getText().toString() + "';");
                sqlDB.close();
                Toast.makeText(getApplicationContext(), "수정됨", Toast.LENGTH_SHORT).show();
                btnSelect.callOnClick();
            }
        });

        btnDelet.setOnClickListener(new View.OnClickListener() {    // 삭제 버튼
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();

                sqlDB.execSQL("DELETE FROM groupTBL WHERE gName = '"
                        + edtName.getText().toString() + "';");
                sqlDB.close();
                Toast.makeText(getApplicationContext(), "삭제됨", Toast.LENGTH_SHORT).show();
                btnSelect.callOnClick();
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {   // 삭제 버튼
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getReadableDatabase(); // 읽기 용으로 열기
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;", null);

                String strNames = "그룹 이름" + "\r\n" + "--------" + "\r\n";
                String strNumbers = "인원" + "\r\n" + "--------" + "\r\n";

                while (cursor.moveToNext()){
                    strNames += cursor.getString(0) + "\r\n";
                    strNumbers += cursor.getString(1) + "\r\n";
                }
                edtNameResult.setText(strNames);
                edtNumberResult.setText(strNumbers);
                cursor.close();
                sqlDB.close();
            }
        });
    }


    public class myDBHelper extends SQLiteOpenHelper{
        public myDBHelper(Context context){
            super(context, "groupDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {   // 데이터베이스 테이블 생성
            db.execSQL("CREATE TABLE groupTBL (gName CHAR(20) PRIMARY KEY, gNumber INTEGER);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {  // 테이블 수정
            db.execSQL("DROP TABLE IF EXISTS groupTBL");    // 테이블 삭제 후
            onCreate(db);                                   // 테이블 생성
        }
    }
}