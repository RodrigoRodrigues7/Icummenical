package com.example.android.icummenical.Classes.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by robso on 19/11/2018.
 */

public interface IGoogleAPIService {
    @GET
    Call<Places>getNearByPlaces(@Url String url);
}
