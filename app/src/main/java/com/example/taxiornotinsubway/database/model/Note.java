package com.example.taxiornotinsubway.database.model;

public class Note {
    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_START = "start";
    public static final String COLUMN_END = "end";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_START_X ="start_x";
    public static final String COLUMN_START_Y ="start_y";
    public static final String COLUMN_END_X  = "end_x";
    public static final String COLUMN_END_Y  = "end_y";


    private int id;
    private String start;
    private String end;
    private String timestamp;
    private String type; // 'history' or 'bookmark'
    private String start_x;
    private String start_y;
    private String end_x;
    private String end_y;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_START + " TEXT,"
                    + COLUMN_END + " TEXT,"
                    + COLUMN_START_X + " TEXT,"
                    + COLUMN_START_Y + " TEXT,"
                    + COLUMN_END_X + " TEXT,"
                    + COLUMN_END_Y + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + COLUMN_TYPE +" TEXT"
                    + ")";

    public Note() {
    }

    public Note(int id, String type, String start, String end, String start_x, String start_y, String end_x, String end_y, String timestamp) {
        this.id = id;
        this.type = type;
        this.start = start;
        this.end = end;
        this.start_x = start_x;
        this.start_y = start_y;
        this.end_x   = end_x;
        this.end_y = end_y;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public  String getType(){return type;}

    public void setType(String type){this.type = type;}
    public String getStart() {return start;    }

    public void setStart(String start) {        this.start = start;}

    public String getEnd() {        return end; }

    public void setEnd(String end) {        this.end = end; }

    public String getStart_x() {        return start_x; }

    public void setStart_x(String start_x) {        this.start_x = start_x; }

    public String getStart_y() {        return start_y; }

    public void setStart_y(String start_y) {        this.start_y = start_y; }

    public String getEnd_x() {        return end_x; }

    public void setEnd_x(String end_x) {        this.end_x = end_x; }

    public String getEnd_y() {        return end_y; }

    public void setEnd_y(String end_y) {        this.end_y = end_y; }
}
