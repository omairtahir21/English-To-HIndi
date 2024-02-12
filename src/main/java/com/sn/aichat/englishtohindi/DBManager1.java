package com.sn.aichat.englishtohindi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBManager1 {


    private static final String DATABASE_NAME = "EnglishHindi.db";
    public static final String FLD_SRNO = "SrNo";
    public static final String FLD_ENG = "enword";
    public static final String FLD_Hindi = "enpronounce";
    public static final String FLD_synonyms = "synonyms";
    public static final String FLD_word_id = "wordid";
    public static final String FLD_en_means = "enmeans";
    public static final String FLD_wd_type = "wdtype";
    public static final String FLD_hn_means = "hnmeans";
    public static final String FLD_Favourte = "fvrtwords";
    public static final String TBL_tblen = "tblen";
    public static final String TBL_tblmeans = "tblmeans";
    public static final String TBL_Favourte = "tblfavourte";
    private static final int DATABASE_VERSION = 4;
    private final Context context;
    private static DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private static DBManager1 manager;

    public static DBManager1 getInstance(Context ctx) {
        if (manager == null) {
            manager = new DBManager1(ctx);
        }
        return manager;
    }

    public DBManager1(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();
        try {
            createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public void close() {
        try {
            if (DBHelper != null) {
                DBHelper.close();
            }
        } catch (SQLiteException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if (!dbExist) {
            DBHelper.getReadableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = context.getDatabasePath(DATABASE_NAME).getPath();
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null;
    }

    public void copyDataBase() throws IOException {
        String DB_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        OutputStream myOutput = new FileOutputStream(DB_PATH);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public List<NameModel> getEnglishWords() {
        List<NameModel> words = new ArrayList<>();
        SQLiteDatabase database = null;
        Cursor cursor = null;

        try {
            database = DBHelper.getReadableDatabase();
            cursor = database.query(TBL_tblen, new String[]{FLD_ENG, FLD_Hindi, FLD_synonyms}, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    NameModel word = new NameModel();
                    word.setWords(cursor.getString(cursor.getColumnIndex(FLD_ENG)));
                    word.setMeaning(cursor.getString(cursor.getColumnIndex(FLD_Hindi)));
                    word.setEnglishMeaning(cursor.getString(cursor.getColumnIndex(FLD_synonyms)));
                    // Set other necessary properties for NameModel

                    words.add(word);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }

        return words;
    }

    public List<NameModel> getHindiWords() {
        List<NameModel> words = new ArrayList<>();
        SQLiteDatabase database = null;
        Cursor cursor = null;

        try {
            database = DBHelper.getReadableDatabase();
            cursor = database.query(TBL_tblen, new String[]{FLD_ENG, FLD_Hindi, FLD_synonyms}, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    NameModel word = new NameModel();
                    word.setWords(cursor.getString(cursor.getColumnIndex(FLD_Hindi))); // Set Hindi word
                    word.setMeaning(cursor.getString(cursor.getColumnIndex(FLD_ENG))); // Set English word
                    word.setEnglishMeaning(cursor.getString(cursor.getColumnIndex(FLD_synonyms))); // Set English meaning as synonyms for now, modify accordingly
                    // Set other necessary properties for NameModel

                    words.add(word);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }

        return words;
    }

    public void removeFavoriteWord(String englishWord) {
        SQLiteDatabase database = DBHelper.getWritableDatabase();
        database.delete(TBL_Favourte, "fvrtwords" + "=?", new String[]{englishWord});
        database.close();
    }


    public List<NameModel> getFavoriteWords() {
        List<NameModel> favoriteWords = new ArrayList<>();
        SQLiteDatabase database = null;
        Cursor cursor = null;

        try {
            database = DBHelper.getReadableDatabase();
            cursor = database.query(TBL_Favourte, new String[]{FLD_Favourte}, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    NameModel word = new NameModel();
                    word.setWords(cursor.getString(cursor.getColumnIndex(FLD_Favourte)));

                    favoriteWords.add(word);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }

        return favoriteWords;
    }

    public void addFavorite(String englishWord)  {
        SQLiteDatabase database = null;
        try {
            database = DBHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("fvrtwords", englishWord);

            long result = database.insert(TBL_Favourte, null, values);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }


    public NameModel getWordDetails(String word) {
        NameModel details = new NameModel();
        SQLiteDatabase database = null;
        Cursor cursor = null;

        try {
            database = DBHelper.getReadableDatabase();
            cursor = database.query(TBL_tblen, new String[]{FLD_SRNO,FLD_ENG, FLD_Hindi},
                    FLD_ENG + " = ?", new String[]{word}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                details.setId(cursor.getInt(cursor.getColumnIndex(FLD_SRNO)));
                details.setWords(cursor.getString(cursor.getColumnIndex(FLD_ENG)));
                details.setMeaning(cursor.getString(cursor.getColumnIndex(FLD_Hindi)));
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }

        return details;
    }


    public NameModel getMeaningDetailsByPosition(int position) {
        NameModel details = new NameModel();
        SQLiteDatabase database = null;
        Cursor cursor = null;

        try {
            database = DBHelper.getReadableDatabase();
            cursor = database.query(
                    TBL_tblmeans,
                    new String[]{ FLD_en_means, FLD_wd_type, FLD_hn_means},
                    FLD_word_id + " = ?",
                    new String[]{String.valueOf(position)},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                details.setEnglishMeaning(cursor.getString(cursor.getColumnIndex(FLD_en_means)));
                details.setType(cursor.getString(cursor.getColumnIndex(FLD_wd_type)));
                details.setHindiMeaning(cursor.getString(cursor.getColumnIndex(FLD_hn_means)));
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }

        return details;
    }


}
