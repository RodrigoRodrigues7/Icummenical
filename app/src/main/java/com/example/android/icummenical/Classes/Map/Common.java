package com.example.android.icummenical.Classes.Map;

/**
 * Created by robso on 19/11/2018.
 */

public class Common {

    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/";
    public static IGoogleAPIService getGoogleAPIService()
    {
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);

    }
}
