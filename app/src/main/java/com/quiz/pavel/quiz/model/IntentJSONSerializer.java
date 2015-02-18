package com.quiz.pavel.quiz.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by pavelkozemirov on 24.01.15.
 * TODO: Should replace with preferences store
 */
public class IntentJSONSerializer {
    private static final String TAG = "IntentJSONSerializer";
    public static IntentJSONSerializer mIntentJSONSerializer;
    private Context mContext;
    private static final String FILENAME1 = "quiz.json";
    private static final String FILENAME2 = "quiz2.json";

    public static IntentJSONSerializer getInitialize(){
        if(mIntentJSONSerializer == null){
            mIntentJSONSerializer = new IntentJSONSerializer();
        }
        return mIntentJSONSerializer;
    }

    public void setContext(Context c){
        mContext = c;
    }














    public void saveCatTopicJsonAr(JSONArray jsonArray)
            throws IOException {

        Writer writer = null;
        try{
            OutputStream out = mContext
                    .openFileOutput(FILENAME2, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsonArray.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
    public ArrayList<Category> loadCatArray() throws IOException, JSONException {

        BufferedReader reader = null;
        JSONArray json = null;
        try {
            InputStream in = mContext.openFileInput(FILENAME2);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            json = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

        } catch (FileNotFoundException e){

        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        ArrayList<Category> ar = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            ar.add(new Category(json.getJSONObject(i)));
        }

        return ar;
    }

}
