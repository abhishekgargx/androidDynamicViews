package abhishek.garg.dynamic.android.views;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

import zyme.zyme.R;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button submitBtn;
    private ArrayList<String> data = new ArrayList<>(); ;
    private static final String URL = "http://numbersapi.com/1..";
    private LinearLayout childHolderLayout;
    private int dataSize = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submitBtn = findViewById(R.id.submitBtn);
        editText = findViewById(R.id.editText);
       childHolderLayout = findViewById(R.id.linearChildParent);

        data.add("Sample 1");
        data.add("Sample 2 ");
        data.add("Sample 3");
        data.add("Sample 4");
        data.add("Sample 5");
        data.add("Sample 6");
        data.add("Sample 7");
        data.add("Sample 8");

//        dynamicUiGeneratorHelper(data,true);
//        dynamicUiGeneratorHelper(data,false);
//        dynamicUiGeneratorHelper(data,true);
//        dynamicUiGeneratorHelper(data,false);
//
//        dynamicUiGeneratorHelper(data,true);
//        dynamicUiGeneratorHelper(data,false);
//        dynamicUiGeneratorHelper(data,true);
//        dynamicUiGeneratorHelper(data,false);
//
//        dynamicUiGeneratorHelper(data,true);
//        dynamicUiGeneratorHelper(data,false);
//        dynamicUiGeneratorHelper(data,true);
//        dynamicUiGeneratorHelper(data,false);

        setSubmitBtnClick();

    }

    private View cardItemView(String str){
        CardView cardView = new CardView(this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(3,4,3,4);
        cardView.setLayoutParams(layoutParams);
        cardView.setRadius(8);
        cardView.setCardElevation(8);


        cardView.setUseCompatPadding(true);

        // text view
        TextView textView = new TextView(this);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        textView.setLayoutParams(params1);
        textView.setText(str);
        textView.setPadding(24,24,24,24);

        cardView.addView(textView);

        return cardView;

    }

    private void dynamicUiGeneratorHelper(int startIndex, int endIndex, Boolean isHorizontal){

        HorizontalScrollView horizontalScrollView =  new HorizontalScrollView(this);
        ScrollView verticalScrollView = new ScrollView(this);
        LinearLayout linearLayout = new LinearLayout(this);

        //scroll view params
        LinearLayout.LayoutParams scrollViewlayoutParms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        scrollViewlayoutParms.setMargins(0,30,0,30);

        // linear layout parms
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        linearLayout.setLayoutParams(linearParams);

        if(isHorizontal){
            // initialise
            horizontalScrollView.setLayoutParams(scrollViewlayoutParms);
            // horizontal items
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            // attach ll
            horizontalScrollView.addView(linearLayout);

        }else{
            // initialise
            verticalScrollView.setLayoutParams(scrollViewlayoutParms);
            // verticle items
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            // attach ll
            verticalScrollView.addView(linearLayout);
        }

        for(int i=startIndex; i< endIndex; i++)
        {
            if(i < dataSize){
                linearLayout.addView(cardItemView(data.get(i)));
            }

        }

        if(isHorizontal)
            childHolderLayout.addView(horizontalScrollView);
        else
            childHolderLayout.addView(verticalScrollView);

    }


    private void setSubmitBtnClick(){
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputVal = editText.getText().toString();

                if(inputVal == null || Integer.valueOf(inputVal) == 0 ){
                    Toast.makeText(MainActivity.this,"Enter a valid input", Toast.LENGTH_SHORT).show();
                }else{
                    loadUrlData(inputVal);
                }
            }
        });
    }



    private void loadUrlData(String value){
        hideKeyboard(editText);
        data = new ArrayList<String>();
        final ProgressDialog  progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        final String newURl = URL + value;

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                newURl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try
                        {
                            JSONObject jsonObject  = new JSONObject(response);

                            Log.d("response", jsonObject.toString());

                            for(int i=1; i<jsonObject.length(); i++){
                                data.add(jsonObject.getString(String.valueOf(i)));
                            }


                            Log.e("arrayList", data.toString());

                           // dataAdapter.notifyDataSetChanged();

                            listSpilter();


                        }catch (Exception e){
                            Toast.makeText(MainActivity.this,"Exception in json", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e("error", error.getMessage());
                        Toast.makeText(MainActivity.this,"Error in api", Toast.LENGTH_SHORT).show();
                    }
                }

        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void listSpilter(){

        boolean horizontal = true;
        int itemCount = 0;


        if(data != null && data.size() > 1){

            dataSize = data.size();

            while(itemCount < dataSize){
                int temp = 0;

                if(horizontal)
                {
                    temp = itemCount;
                    itemCount = itemCount + 5;
                    dynamicUiGeneratorHelper(temp, itemCount,horizontal);
                    horizontal = false;
                }
                else
                    {
                    temp = itemCount;
                    itemCount = itemCount + 10;
                    dynamicUiGeneratorHelper(temp, itemCount,horizontal);
                    horizontal = true;
                }
            }

        }else{
            Toast.makeText(MainActivity.this,"Enter N value greater than 2",Toast.LENGTH_LONG).show();
        }
    }



    public  void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }






}
