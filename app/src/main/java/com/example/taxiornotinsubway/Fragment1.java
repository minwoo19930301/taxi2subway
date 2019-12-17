package com.example.taxiornotinsubway;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.example.taxiornotinsubway.database.TestAdapter;

public class Fragment1 extends Fragment implements FragmentLifecycle{
    public ODsayService odsayService;
    ArrayList<String> exchangeList = new ArrayList<>();
    public Context context;
    public String subwayTravelTime = "0";
    public String exchangeStation = "";
    public String taxiTravelTime = "0";
    double startX, startY, endX, endY;
    int sid, eid;


    //Handler
    private Button button;
    private Spinner spinner1;
    String SearchStartStation, SearchEndStation;
    List<String> startStation = new ArrayList<>();
    List<String> destinationStation = new ArrayList<>();
    private static final String[] Line = new String[]{"1","2","3","4"};
    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();

        return fragment;
    }
    private Spinner spinner2;
    //지하철 노선 시간 확인
    private Spinner spinner3;
    private Spinner spinner4;

    public void DatabaseSearchData(){
        //String SearchStartStationSearchStartStation, SearchEndStation;
        TestAdapter mDbHelper = new TestAdapter(getActivity());
        mDbHelper.createDatabase();
        mDbHelper.open();
        Cursor StartLocationData = mDbHelper.getLocationData(SearchStartStation);
        Cursor StartStationID = mDbHelper.getStationId(SearchStartStation);
        Cursor EndLocationData = mDbHelper.getLocationData(SearchEndStation);
        Cursor EndStationID = mDbHelper.getStationId(SearchEndStation);
        StartLocationData.moveToFirst();
        StartStationID.moveToFirst();
        EndLocationData.moveToFirst();
        EndStationID.moveToFirst();
        //double startX, startY, endX, endY;
        //int sid, eid;
        startX = StartLocationData.getDouble(0);
        startY = StartLocationData.getDouble(1);
        endX = EndLocationData.getDouble(0);
        endY = EndLocationData.getDouble(1);
        sid = StartStationID.getInt(0);
        eid = EndStationID.getInt(0);
        Log.d("DATA LIST", "VALUES"+String.valueOf(startX)+" "+ String.valueOf(startY)+" "+String.valueOf(endX)+" "+String.valueOf(endY)+" "+String.valueOf(sid)+" "+String.valueOf(eid));
        mDbHelper.close();
    }

    public void PathSeeking(int sid, int eid) {
        odsayService = ODsayService.init(getActivity(), "ohyAzciqIm641X57gUSvS8GNcWZscObNioWn+1HlQHE");
        odsayService.setReadTimeout(5000);
        odsayService.setConnectionTimeout(5000);
        //SID는 DB에서 맞는 지하철역 정보를 받아와 DB에서 ID를 받아올 것
        odsayService.requestSubwayPath("1000", String.valueOf(sid), String.valueOf(eid), "1", onResultCallbackListener);
        //stationName에 조회할 View를 넣어야함
    }
    //Handler
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
                    exchangeStation = odsayData.getJson().getJSONObject("result").getJSONObject("exChangeInfoSet").getJSONArray("exChangeInfo").getJSONObject(0).getString("exName");

                    for (int i=0; i<exchangeStations.length(); i++){
                        Log.d("Exchange :", exchangeStations.getJSONObject(i).getString("exName"));
                        exchangeList.add(exchangeStations.getJSONObject(i).getString("exName"));
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

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        Log.d("skku","fragment1 create");
        View view =  inflater.inflate(R.layout.fragment1, container,false);
        spinner1= (Spinner)view.findViewById(R.id.spinner_start);
        spinner2= (Spinner)view.findViewById(R.id.spinner_start_station);
        spinner3= (Spinner)view.findViewById(R.id.spinner_destination);
        spinner4= (Spinner)view.findViewById(R.id.spinner_destination_station);
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item,Line);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item,Line);
        final ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item,Line);
        final ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,Line);

        //adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setPrompt("출발 호선 선택");
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //if (adapter1.getItem(position)=="호선"){
                //    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item, new String[]{"역이름"});
                //    spinner2.setAdapter(adapter2);
                //}

                //String[] startStation = new String[4];
                //startStation[0] = (String)adapter1.getItem(position);
                //startStation[1] = (String)adapter1.getItem(position);
                //startStation[2] = (String)adapter1.getItem(position);
                //startStation[3] = (String)adapter1.getItem(position);
                //Log.d("check",startStation[0]);
                int lineNumber = Integer.parseInt(adapter1.getItem(position));
                startStation.clear();
                TestAdapter mDbHelper = new TestAdapter(view.getContext());
                mDbHelper.createDatabase();
                mDbHelper.open();
                Cursor testdata = mDbHelper.getStationData(lineNumber);
                //Log.d(TAG, mCur.getColumnName(1));
                int indexing = testdata.getColumnIndex("name");
                //Log.d(TAG, String.valueOf(indexing));
                int repeatNumber = testdata.getCount() - 1;
                for(int i = 0; repeatNumber > i; i++)
                {
                    testdata.moveToNext();
                    String StationName = testdata.getString(indexing);
                    startStation.add(StationName);
                }
                mDbHelper.close();
                final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item, startStation);
                spinner2.setAdapter(adapter2);
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        SearchStartStation = (String)adapter2.getItem(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
//                Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_SHORT); //본인이 원하는 작업.
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        spinner3= (Spinner)view.findViewById(R.id.spinner_destination);
        //adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int lineNumber = Integer.parseInt(adapter1.getItem(position));
                destinationStation.clear();
                TestAdapter mDbHelper = new TestAdapter(view.getContext());
                mDbHelper.createDatabase();
                mDbHelper.open();
                Cursor testdata = mDbHelper.getStationData(lineNumber);
                //Log.d(TAG, mCur.getColumnName(1));
                int indexing = testdata.getColumnIndex("name");
                //Log.d(TAG, String.valueOf(indexing));
                int repeatNumber = testdata.getCount() - 1;
                for(int i = 0; repeatNumber > i; i++)
                {
                    testdata.moveToNext();
                    String StationName = testdata.getString(indexing);
                    destinationStation.add(StationName);
                }
                mDbHelper.close();
                final ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item, destinationStation);
                spinner4.setAdapter(adapter4);
                spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        SearchEndStation = (String)adapter4.getItem(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
//                Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_SHORT); //본인이 원하는 작업.
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //double startX, startY, endX, endY;
                //int sid, eid;
                DatabaseSearchData();
                PathSeeking(sid, eid);
                //HttpThread httpThread = new HttpThread("https://apis.openapi.sk.com/tmap/routes?version=1&format=json&appKey=0f31e295-9ada-43b5-9292-5133678f2a00&startX=126.9850380932383&startY=37.566567545861645&endX=127.10331814639885&endY=37.403049076341794&totalValue=2");
                HttpThread httpThread = new HttpThread("https://apis.openapi.sk.com/tmap/routes?version=1&format=json&appKey=0f31e295-9ada-43b5-9292-5133678f2a00&startX="+String.valueOf(startY)+"&startY="+String.valueOf(startX)+"&endX="+String.valueOf(endY)+"&endY="+String.valueOf(endX)+"&totalValue=2");
                httpThread.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("taxi and subway","Taxi Time : "+taxiTravelTime + " Subway Time: " + subwayTravelTime);
                        if(false){
                            Intent in = new Intent(getActivity(), ResultActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("some",new Data(taxiTravelTime, subwayTravelTime, startX,endX,startY,endY));
                            in.putExtras(bundle);
                            startActivity(in);
                        }else{

                            //if(Integer.parseInt(subwayTravelTime) < Integer.parseInt(taxiTravelTime)){
                            //임시
                            Log.d("taxi and subway","Taxi Time : "+taxiTravelTime + " Subway Time: " + subwayTravelTime + " " + startX + " " + endX+ " " + startY + " " +endY + " "+ exchangeList.size());
                            Log.d("error,,", "1");
                            if(exchangeList!=null) {
                                Log.d("error,,", "2");
                                Intent in = new Intent(getActivity(), ResultActivity2.class); //ResultActivity를 2개로 나눠서
                                in.putExtra("some", new Data(taxiTravelTime, subwayTravelTime, SearchStartStation, SearchEndStation, exchangeList));
                                startActivity(in);
                                exchangeList = new ArrayList<>();
                            }
                        }
                    }
                }, 2000);


            }
        });
        return view;
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
    @Override
    public void onPauseFragment() {
        Log.d("ss", "onPauseFragment()1");
        Toast.makeText(getActivity(), "onPauseFragment():" + "ss", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResumeFragment() {
        Log.d("ss", "onResumeFragment()1");
        Toast.makeText(getActivity(), "onResumeFragment():" + "ss", Toast.LENGTH_SHORT).show();
    }

}