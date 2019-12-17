package com.example.taxiornotinsubway;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxiornotinsubway.database.DatabaseHelper;
import com.example.taxiornotinsubway.database.model.Note;
import com.example.taxiornotinsubway.ui.main.SectionsPagerAdapter;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.taxiornotinsubway.database.TestAdapter;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private Context context;
    private List<Note> notesList;
    private DatabaseHelper db;
    private Fragment2 f2;
    private Fragment3 f3;
    public ODsayService odsayService;
    public String taxiTravelTime;
    public String subwayTravelTime;
    ArrayList<String> exchangeList;



    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 0){
                Log.d("response","request fail");
            } else if(msg.what == 1){
                try {
                    Log.d("handle",(String)msg.obj);
                    JSONObject taxiInformation = new JSONObject((String)msg.obj);
                    String taxiTime_stringFormat = taxiInformation.getJSONArray("features").getJSONObject(0).getJSONObject("properties").getString("totalTime");
                    taxiTravelTime = String.valueOf(Integer.parseInt(taxiTime_stringFormat)/60);
                    Log.d("taxiTime",taxiTravelTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    //지하철 경유 시간 및 환승 지점 확인
    OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {
        // 호출 성공 시 실행
        @Override
        public void onSuccess(ODsayData odsayData, API api) {
            try {
                // API Value 는 API 호출 메소드 명을 따라갑니다.
                if (api == API.SUBWAY_PATH) {
                    subwayTravelTime = odsayData.getJson().getJSONObject("result").getString("globalTravelTime");
                    JSONArray exchangeStations = odsayData.getJson().getJSONObject("result").getJSONObject("exChangeInfoSet").getJSONArray("exChangeInfo");
                    for (int i=0; i<exchangeStations.length(); i++){
                        Log.d("Exchange :", exchangeStations.getJSONObject(i).getString("exName"));
                        exchangeList.add( exchangeStations.getJSONObject(i).getString("exName"));
                    }


                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 호출 실패 시 실행
        @Override
        public void onError(int i, String s, API api) {
            if (api == API.SUBWAY_PATH) {}
        }
    };

    public void PathSeeking() {

        odsayService = ODsayService.init(context, "ohyAzciqIm641X57gUSvS8GNcWZscObNioWn+1HlQHE");
        odsayService.setReadTimeout(5000);
        odsayService.setConnectionTimeout(5000);
        //SID는 DB에서 맞는 지하철역 정보를 받아와 DB에서 ID를 받아올 것
        odsayService.requestSubwayPath("1000", "221", "420", "1", onResultCallbackListener);
        //stationName에 조회할 View를 넣어야함
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView start;
        public TextView end;
        public TextView ivDelete;
        public ImageView ivStar;
        public ImageView ivSearch;
        public TextView timestamp;


        public MyViewHolder(View view) {
            super(view);
            start = view.findViewById(R.id.start);
            end = view.findViewById(R.id.end);
            ivStar = view.findViewById(R.id.iv_star);
            ivSearch = view.findViewById(R.id.iv_search);
            ivDelete = view.findViewById(R.id.delete);
            timestamp = view.findViewById(R.id.timestamp);
        }
    }


    public NotesAdapter(Context context, List<Note> notesList) {
        this.context = context;
        this.notesList = notesList;
        db = new DatabaseHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Note note = notesList.get(position);
        holder.start.setText(note.getStart());
        holder.end.setText(note.getEnd());
        holder.timestamp.setText(formatDate(note.getTimestamp()));

        // 북마크 추가
        holder.ivStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PathSeeking();
                HttpThread httpThread = new HttpThread("https://apis.openapi.sk.com/tmap/routes?version=1&format=json&appKey=0f31e295-9ada-43b5-9292-5133678f2a00&startX=126.9850380932383&startY=37.566567545861645&endX=127.10331814639885&endY=37.403049076341794&totalValue=2");
                httpThread.start();
                Toast.makeText(context, "선택 ID start : " + note.getId(), Toast.LENGTH_LONG).show();
                note.setType("bookmark");
                db.updateNote(note);
                notesList.remove(position);
                notifyDataSetChanged();
            }
        });

        // 검색
        holder.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("time","Taxi Time : "+taxiTravelTime + " Subway Time: " + subwayTravelTime);
                double start_x =  Double.parseDouble(note.getStart_x());
                double start_y =  Double.parseDouble(note.getStart_y());
                double end_x =  Double.parseDouble(note.getEnd_x());
                double end_y =  Double.parseDouble(note.getEnd_y());
                //if(Integer.parseInt(subwayTravelTime) < Integer.parseInt(taxiTravelTime)){
                //임시
                if(false){
                    Intent i = new Intent(context, ResultActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("some",new Data(taxiTravelTime, subwayTravelTime, 37.582191,127.001915,37.500628,127.036392));
                    i.putExtras(bundle);
                    context.startActivity(i);
                }else{
                    Intent i = new Intent(context, ResultActivity2.class); //ResultActivity를 2개로 나눠서
                    i.putExtra("some", new Data(taxiTravelTime, subwayTravelTime, "역삼","혜화", exchangeList));
                    context.startActivity(i);
                }
                //Toast.makeText(context, "선택 ID search : " + start_x +" / "+ start_y+" / "+ end_x+" / "+ end_y, Toast.LENGTH_LONG).show();
                //double[] latlong ={start_x, start_y, end_x,end_y};
                //in.putExtra("some", latlong);
                ///context.startActivity(in);
            }
        });

        // 삭제
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "선택 ID delete: " + note.getId(), Toast.LENGTH_LONG).show();
                db.deleteNote(note);
                notesList.remove(note);
                notifyDataSetChanged();
            }
        });

    }
    private void createNote(String start, String end, String start_x, String start_y, String end_x,String end_y) {
        String type = "history";
        long id = db.insertNote(type,start,end,start_x,start_y,end_x,end_y);
        Note n = db.getNote(id);

        if (n != null) {
            notesList.add(0, n);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("yyyy.mm.dd   HH:mm");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }
        return "";
    }
    class HttpThread extends Thread {
        private String url;

        public HttpThread(String url) {
            this.url = url;
        }//end of HttpThread init

        @Override
        public void run() {
            try{
                URL serverUrl = new URL(this.url);
                HttpURLConnection http = (HttpURLConnection)serverUrl.openConnection();
                http.setRequestMethod("GET");
                http.setConnectTimeout(10 * 10000);
                http.setReadTimeout(10 * 10000);
                http.setDoInput(true);
                http.setDoOutput(false);
                BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
                StringBuffer sb = new StringBuffer();
                String strLine = null;
                while((strLine = in.readLine()) != null){
                    sb.append(strLine);
                }
                Message msg = new Message();
                msg.what = 1;

                msg.obj = sb.toString();
                handler.sendMessage(msg);

            } catch (Exception e ){
                Log.e("Main", "ERROR : ", e);
                handler.sendEmptyMessage(0);
            } // end of TryCatch
        } // end of run
    } // end of HttpThread
}
