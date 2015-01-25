package com.quiz.pavel.quiz.model;

import android.content.Context;

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

/**
 * Created by pavelkozemirov on 24.01.15.
 */
public class IntentJSONSerializer {

    public static IntentJSONSerializer mIntentJSONSerializer;
    private Context mContext;
    private static final String FILENAME = "quiz.json";

    public static IntentJSONSerializer getInitialize(Context c){
        if(mIntentJSONSerializer == null){
            mIntentJSONSerializer = new IntentJSONSerializer(c);
        }
        return mIntentJSONSerializer;
    }

    public IntentJSONSerializer(Context c){
        mContext = c;
    }

    public void saveData(JSONObject json)
            throws JSONException, IOException {

        Writer writer = null;
        try{
            OutputStream out = mContext
                    .openFileOutput(FILENAME, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(json.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

    public JSONObject loadData()
            throws IOException, JSONException{
        BufferedReader reader = null;
        JSONObject json = null;
        try {
            InputStream in = mContext.openFileInput(FILENAME);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            json = (JSONObject) new JSONTokener(jsonString.toString()).nextValue();

        } catch (FileNotFoundException e){

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return json;
    }

    public boolean hasAccount(){
        String str = null;
        try {
            str = loadData().getString("token");

        } catch(JSONException e){
            return false;
        }
        catch (Exception e) {

        }
        if(str == null){
            return false;
        }
        return true;
    }

}
