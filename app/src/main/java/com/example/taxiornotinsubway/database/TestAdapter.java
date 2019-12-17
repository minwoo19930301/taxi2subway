package com.example.taxiornotinsubway.database;

import java.io.IOException;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TestAdapter{
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DatabaseHelperStation mDbHelper;

    public TestAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new DatabaseHelperStation(mContext);
    }

    public TestAdapter createDatabase() throws SQLException
    {
        try
        {
            mDbHelper.createDataBase();
        }
        catch (IOException mIOException)
        {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public TestAdapter open() throws SQLException
    {
        try
        {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }

    public Cursor getStationData(int lineNumber){
        try
        {
            String sql ="SELECT DISTINCT * FROM station WHERE line_no = " + lineNumber;

            Cursor mCur = mDb.rawQuery(sql, null);
            if(mCur != null){
                mCur.moveToFirst();
            }
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    } //end of getStationData

    public Cursor getLocationData(String stationName){
        try
        {
            String sql ="SELECT DISTINCT latitude, longitude FROM station WHERE name = \'" + stationName + "\'";

            Cursor mCur = mDb.rawQuery(sql, null);
            if(mCur != null){
                mCur.moveToFirst();
            }
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    } //end of getLocationData

    public Cursor getStationId(String stationName){
        try
        {
            String sql ="SELECT DISTINCT code FROM station WHERE name = \'" + stationName + "\'";

            Cursor mCur = mDb.rawQuery(sql, null);
            if(mCur != null){
                mCur.moveToFirst();
            }
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    } //end of getLocationData
}