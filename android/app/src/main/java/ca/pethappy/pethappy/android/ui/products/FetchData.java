package ca.pethappy.pethappy.android.ui.products;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchData extends AsyncTask<Void,Void,Void> {
    String data;
    String dataParsed;
    String singleParsed;
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ProductDetails.data.setText(dataParsed);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("http://localhost:8090/products/11");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            String basicAuth = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJndWVzdEBwZXRoYXBweS5jYSIsImZpcnN0TmFtZSI6Ikd1ZXN0IiwibGFzdE5hbWUiOiJVc2VyIiwicm9sZXMiOlsiUk9MRV9HVUVTVCJdLCJpc3MiOiJQZXQgSGFwcHkgU2VydmVyIiwiaWQiOjIsImV4cCI6MTU0Mjc1NjI3MCwiaWF0IjoxNTQyNjY5ODcwLCJlbWFpbCI6Imd1ZXN0QHBldGhhcHB5LmNhIn0.X-LHNqtqQHnwYZPQ9UwZe0JC7k6saUVdl232VNTocTI";
            httpURLConnection.setRequestProperty ("Authorization", basicAuth);
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data+line;
            }

            JSONArray JA = new JSONArray(data);
            for(int i=0;i<JA.length();i++){
                JSONObject JO = (JSONObject) JA.get(i);
                singleParsed = "Name:"+ JO.get("name")+"\n"+
                        "Description:"+ JO.get("description")+"\n"+
                        "Weight:"+ JO.get("weightKg"+"Kg")+"\n"+
                        "Price:"+ JO.get("$"+"price")+"\n"+
                        "Quantity Remaining:"+ JO.get("quantity")+"\n";
                dataParsed = dataParsed+ singleParsed;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
