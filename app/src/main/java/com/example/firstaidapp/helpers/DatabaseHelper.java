package com.example.firstaidapp.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.firstaidapp.models.Ailment;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "OtherAilmentsDatabase.db";
    public static final String DBLOCATION = "data/data/com.example.firstaidapp/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;


    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();

        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }

        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    public void closeDatabase() {

        if (mDatabase != null) {
            mDatabase.close();
        }

    }

    public boolean copyDatabase() {

        try {

            InputStream inputStream = mContext.getAssets().open(DBNAME);
            String outFileName = DBLOCATION + DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);

            byte[]buff = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public List<Ailment> getData() {
        Ailment ailment = null;
        List<Ailment> ailmentList = new ArrayList<>();
        openDatabase();

        Cursor cursor = null;

        try {
            cursor = mDatabase.rawQuery("SELECT * FROM OtherAilmentsTable ORDER BY Title ASC", null);

            if (cursor != null) {
                while (cursor.moveToNext()) {

                    ailment = new Ailment(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                    ailmentList.add(ailment);

                }
                return ailmentList;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null) {
                cursor.close();
            }
            closeDatabase();
        }

        return  null;
    }

    public List<Ailment> getDataWhereSymptomsLike(String[] symptoms) {
        Ailment ailment = null;
        List<Ailment> ailmentList = new ArrayList<>();
        openDatabase();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM OtherAilmentsTable WHERE Symptoms ");

        for (int i = 0; i < symptoms.length; i++) {
            if (i == 0) {
                stringBuilder.append("LIKE \"%" + symptoms[i] + "%\"");
            } else {
                stringBuilder.append(" OR Symptoms LIKE \"%" + symptoms[i] + "%\"");
            }

        }

        Cursor cursor = null;

        try {

            cursor = mDatabase.rawQuery(stringBuilder.toString(), null);

            if (cursor != null) {
                while (cursor.moveToNext()) {

                    ailment = new Ailment(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                    ailmentList.add(ailment);

                }
                return ailmentList;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            closeDatabase();
        }

        return  null;
    }

    public String[] getSymtomsDistinct() {

        openDatabase();

        List<String[]> symptomsList = new ArrayList<>();
        String[] symptoms = null;

        Cursor cursor = null;

        try{
            cursor = mDatabase.rawQuery("SELECT Symptoms FROM OtherAilmentsTable", null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    symptoms = cursor.getString(0).split(";");
                    symptomsList.add(symptoms);
                }

                int totalLength = 0;
                for (String[] symptomArray : symptomsList) {
                    totalLength += symptomArray.length;
                }

                String[] resultArray = new String[totalLength];
                int currentIndex = 0;

                for (String[] symptomArray : symptomsList) {
                    System.arraycopy(symptomArray, 0, resultArray, currentIndex, symptomArray.length);
                    currentIndex += symptomArray.length;
                }
                return resultArray;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            closeDatabase();
        }

        return null;
    }

    public int getDataCount() {
        openDatabase();

        int dataCount = 0;
        Cursor cursor = null;

        try {
            cursor = mDatabase.rawQuery("SELECT COUNT(*) FROM OtherAilmentsTable", null);

            if (cursor != null && cursor.moveToFirst()) {
                dataCount = cursor.getInt(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            closeDatabase();
        }

        return dataCount;
    }

}
