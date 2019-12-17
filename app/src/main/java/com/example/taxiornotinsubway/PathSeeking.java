package com.example.taxiornotinsubway;

import androidx.appcompat.app.AppCompatActivity;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class PathSeeking extends AppCompatActivity {
    private ODsayService odsayService;
    private Context context;
    private JSONObject jsonObject;
    private String firstStationId, SecondStationId;

    private void PathSeeking() {
        context = this;
        odsayService = ODsayService.init(PathSeeking.this, "ohyAzciqIm641X57gUSvS8GNcWZscObNioWn+1HlQHE");
        odsayService.setReadTimeout(5000);
        odsayService.setConnectionTimeout(5000);

        odsayService.requestSearchStation("영등포", "1000", "2", "1", "", "", onResultCallbackListenerFirst);
        //stationName에 조회할 View를 넣어야함
        odsayService.requestSearchStation("시청", "1000", "2", "1", "", "", onResultCallbackListenerSecond);
        //stationName에 조회할 View를 넣어야함
    }

    OnResultCallbackListener onResultCallbackListenerFirst = new OnResultCallbackListener() {
        // 호출 성공 시 실행
        @Override
        public void onSuccess(ODsayData odsayData, API api) {
            try {
                // API Value 는 API 호출 메소드 명을 따라갑니다.
                if (api == API.SEARCH_STATION) {
                    firstStationId= odsayData.getJson().getJSONObject("result").getString("stationId");
                    Log.d("First Station Id : %s", firstStationId);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 호출 실패 시 실행
        @Override
        public void onError(int i, String s, API api) {
            if (api == API.SEARCH_STATION) {}
        }
    };
    OnResultCallbackListener onResultCallbackListenerSecond = new OnResultCallbackListener() {
        // 호출 성공 시 실행
        @Override
        public void onSuccess(ODsayData odsayData, API api) {
            try {
                // API Value 는 API 호출 메소드 명을 따라갑니다.
                if (api == API.SEARCH_STATION) {
                    SecondStationId = odsayData.getJson().getJSONObject("result").getString("stationId");
                    Log.d("Second Station Id : %s", SecondStationId);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 호출 실패 시 실행
        @Override
        public void onError(int i, String s, API api) {
            if (api == API.SEARCH_STATION) {}
        }
    };

}
