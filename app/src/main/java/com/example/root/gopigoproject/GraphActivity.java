package com.example.root.gopigoproject;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;


public class GraphActivity extends Activity  {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.0.13:3000");
        } catch (URISyntaxException e) {
            Log.d("error ",""+e);
        }
    }
    Boolean active = false;
    Paint paint, paintPoint, paintDraw, paintDraw2,paintDraw3;
    Path path, pathPoint, pathValue1,pathValue2,pathValue3;
    LinkedList<Point> listPoint1;
    LinkedList<Point> listPoint2;
    LinkedList<Point> listPoint3;
    ArrayList<Point> pointGraphX;
    ArrayList<Point> pointGraphY;
    int[] valuePointX;
    InterfaceRetrofit interfce;
    Boolean scrollActive = false;

    TextView valdistance;
    Button Forword;
    Button left;
    Button right;
    Button stop;
    Button sonor, buttonBeacon;
    int largeur, hauteur, compteur,compteur2,compteur3;

    int sleepValue = 100;

    FrameLayout preview;
    SurfaceHolder mHolder;
    GraphViewPerso graphPerso;
    String valbeacon="";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);
        Forword = (Button) findViewById(R.id.forword);
        right = (Button) findViewById(R.id.right);
        left =  (Button) findViewById(R.id.left);
        stop = (Button) findViewById(R.id.stop);
        sonor = (Button) findViewById(R.id.distance);
        buttonBeacon = (Button) findViewById(R.id.beacon);
        valdistance = findViewById(R.id.textvie);
       this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mSocket.on("distance_robot", onNewMessage);
        mSocket.connect();
        preview = (FrameLayout) findViewById(R.id.graph);
        preview.setBackgroundColor(Color.BLACK);
        graphPerso = new GraphViewPerso(this);
        preview.addView(graphPerso);

        interfce = new RestAdapter.Builder()
                .setEndpoint(InterfaceRetrofit.ENDPOINT)
                .build()
                .create(InterfaceRetrofit.class);

        Forword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfce.searchReposAsync("w", new Callback<Response>() {
                    @Override
                    public void success(Response response, retrofit.client.Response response2) {
                        Log.d("reponse","ok");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("error",""+error);
                    }
                });
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfce.searchReposAsync("d", new Callback<Response>() {
                    @Override
                    public void success(Response response, retrofit.client.Response response2) {
                        Log.d("reponse","ok");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("error",""+error);
                    }
                });
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfce.searchReposAsync("a", new Callback<Response>() {
                    @Override
                    public void success(Response response, retrofit.client.Response response2) {
                        Log.d("reponse","ok");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("error",""+error);
                    }
                });
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfce.searchReposAsync("x", new Callback<Response>() {
                    @Override
                    public void success(Response response, retrofit.client.Response response2) {
                        Log.d("reponse","ok");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("error",""+error);
                    }
                });
            }
        });
        sonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfce.searcDistaceAsync(new Callback<Response>() {
                    @Override
                    public void success(Response response, retrofit.client.Response response2) {
                        valdistance.setText(""+response.getResponse()+".00 cm");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("error",""+error);
                    }
                });
            }
        });
        buttonBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfce.searcBeaconAsync(new Callback<Response>() {
                    @Override
                    public void success(Response response, retrofit.client.Response response2) {
                        Log.d("reponse","ok");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("error",""+error);
                    }
                });
            }
        });
    }


    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        JSONObject beacon= (JSONObject) data.getJSONObject("beacon");
                        Log.d("beacon name",""+beacon.getString("name"));

                        if(beacon.getString("name").contains("5373")) {
                            valbeacon = "5373";
                        }
                        if(beacon.getString("name").contains("CBA5")) {
                            valbeacon = "CBA5";
                        }
                        if(beacon.getString("name").contains("89CE")) {
                            valbeacon = "89CE";
                        }
                            Double distance = data.getDouble("distance");
                            Log.d("strIncom", ""+distance);

                            float graphHauteur = pointGraphY.get(pointGraphY.size() - 1).y;

                            if (distance!=0) {

                                if (compteur > pointGraphX.get(valuePointX.length - 1).x - 160 || compteur2 > pointGraphX.get(valuePointX.length - 1).x - 160 || compteur3 > pointGraphX.get(valuePointX.length - 1).x - 160) {
                                    scrollActive = true;
                                    switch (valbeacon){
                                        case "5373":
                                            compteur -= 80;
                                            break;
                                        case "CBA5":
                                            compteur2 -= 80;
                                            break;
                                        case "89CE":
                                            compteur3 -= 80;
                                            break;
                                    }
                                    scroll_graph(valbeacon);
                                }
                                    switch (valbeacon){
                                        case "5373":
                                            compteur += 80;
                                            listPoint1.add(new Point(compteur, (float) ((graphHauteur - (distance*80)))));
                                            break;
                                        case "CBA5":
                                            compteur2 += 80;
                                            listPoint2.add(new Point(compteur2, (float) ((graphHauteur - (distance*80)))));
                                            break;
                                        case "89CE":
                                            compteur3 += 80;
                                            listPoint3.add(new Point(compteur3, (float) ((graphHauteur - (distance*80)))));
                                            break;
                                    }
                                    valbeacon = "";
                            }




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    };

    public void initParam() {

        paint = new Paint();

        paint.setColor(Color.BLACK);
        compteur =0;
        compteur2 =0;
        compteur3 =0;
        scrollActive = false;

        paint.setStrokeWidth(3);

        paint.setStyle(Paint.Style.STROKE);

        paintPoint = new Paint();


        paintPoint.setColor(Color.BLACK);

        paintPoint.setStrokeWidth(3);

        paintPoint.setStyle(Paint.Style.STROKE);

        paintDraw = new Paint();
        paintDraw.setColor(Color.RED);
        paintDraw.setStrokeWidth(3);
        paintDraw.setTextSize(30);
        paintDraw.setStyle(Paint.Style.STROKE);

        paintDraw2 = new Paint();
        paintDraw3= new Paint();

        paintDraw2.setColor(Color.GREEN);
        paintDraw2.setStrokeWidth(3);
        paintDraw2.setTextSize(30);
        paintDraw2.setStyle(Paint.Style.STROKE);

        paintDraw3.setColor(Color.BLUE);
        paintDraw3.setStrokeWidth(3);
        paintDraw3.setTextSize(30);
        paintDraw3.setStyle(Paint.Style.STROKE);

        path = new Path();
        listPoint1 = new LinkedList<Point>();
        listPoint2 = new LinkedList<Point>();
        listPoint3 = new LinkedList<Point>();
        pointGraphX = new ArrayList<Point>();
        pointGraphY = new ArrayList<Point>();
        valuePointX = new int[14];

        pathPoint = new Path();
        pathValue1 = new Path();
        pathValue2 = new Path();
        pathValue3 = new Path();

        initPointGraph();

        Log.e("init","init param");
    }

    public void scroll_graph(String beacon) {
        int b = valuePointX.length;

        for (int i = 0; i < b; i++) {
            valuePointX[i] = valuePointX[i] + 1;
        }
        switch (beacon){
            case "5373":

                for ( int i = 0; i < listPoint1.size()-1; i++) {
                    if(listPoint1.size() >= b && listPoint1.get(i).x>80 && i+2 < listPoint1.size()) {
                       listPoint1.set(i, new Point(listPoint1.get(i + 1).x - 80, listPoint1.get(i + 1).y));
                        listPoint1.set(i+1, new Point(listPoint1.get(i + 1).x, listPoint1.get(i + 2).y));
                    }
                }
                break;
            case "CBA5":

                for (int i = 0; i < listPoint2.size()-1; i++) {
                    if(listPoint2.size() >= b && listPoint2.get(i).x>80 && i+2 < listPoint2.size()) {
                        listPoint2.set(i, new Point(listPoint2.get(i + 1).x - 80, listPoint2.get(i + 1).y));
                        listPoint2.set(i+1, new Point(listPoint2.get(i + 1).x, listPoint2.get(i + 2).y));
                    }
                };
                break;
            case "89CE":

                for (int i = 0; i < listPoint3.size()-1; i++) {
                    if(listPoint3.size() >= b && listPoint3.get(i).x>80 && i+2 < listPoint3.size()) {
                        listPoint3.set(i, new Point(listPoint3.get(i + 1).x - 80, listPoint3.get(i + 1).y));
                        listPoint3.set(i+1, new Point(listPoint3.get(i + 1).x, listPoint3.get(i + 2).y));
                    }
                }
                break;
        }





    }

    public void initPointGraph() {
        int x = 80;
        int y = 40;


        while (y < hauteur) {
            pointGraphY.add(new Point(x, y));
            y = y + 80;
        }

        y = (int) pointGraphY.get(pointGraphY.size() - 1).y;
        int g = 0;

        while (x < largeur) {
            pointGraphX.add(new Point(x, y));

            g++;

            x = x + 80;
        }
        valuePointX = new int[g];
        for (int i = 0; i < g; i++) {
            valuePointX[i] = i;
        }

    }


    public void DessinGraphX(Canvas canvas) {

        Paint textPain = new Paint();
        //paint.setTextSize(20);
        textPain.setStrokeWidth(1);
        textPain.setColor(Color.BLACK);

        textPain.setStyle(Paint.Style.FILL);
        path.moveTo(pointGraphX.get(0).x, pointGraphX.get(0).y);

        for (int i = 1; i < pointGraphX.size(); i++) {

            path.lineTo(pointGraphX.get(i).x, pointGraphX.get(i).y);

        }

        canvas.drawPath(path, paint);

    }


    public void dessinPointX(Canvas canvas) {
        Paint textPain = new Paint();
        //paint.setTextSize(20);
        textPain.setStrokeWidth(1);
        textPain.setColor(Color.BLACK);
        textPain.setTextSize(14);
        textPain.setStyle(Paint.Style.FILL);

        for (int i = 1; i < pointGraphX.size(); i++) {
            canvas.drawText(".", pointGraphX.get(i).x, pointGraphX.get(0).y, textPain);

        }
    }

    public void dessinValueX(Canvas canvas) {
        Paint textPain = new Paint();
        //paint.setTextSize(20);
        textPain.setStrokeWidth(1);
        textPain.setColor(Color.BLACK);
        textPain.setTextSize(14);
        textPain.setStyle(Paint.Style.FILL);
        for (int i = 0; i < pointGraphX.size(); i++) {
            Log.e("value", "" + valuePointX[i]);
            canvas.drawText("" + valuePointX[i], pointGraphX.get(i).x, pointGraphX.get(0).y + 20, textPain);

        }

    }


    public void DessinGraphY(Canvas canvas) {

        path.moveTo(pointGraphY.get(0).x, pointGraphY.get(0).y);

        for (int i = 1; i < pointGraphY.size(); i++) {

            path.lineTo(pointGraphY.get(i).x, pointGraphY.get(i).y);

        }
        canvas.drawPath(path, paint);

        for (int i = pointGraphY.size() - 1; i > 0; i--) {
            canvas.drawText("" + (int) (pointGraphY.get(i).x / 80), pointGraphY.get(i).x, pointGraphY.get(i).y, paintPoint);

        }
        paint.setTextSize(20);
        paint.setStrokeWidth(1);
        int j = 0;
        for (int i = pointGraphY.size() - 1; i > 0; i--) {

            canvas.drawText("" + (int) (pointGraphY.get(i).y / 80), 40, pointGraphY.get(j).y, paint);
            j++;
        }
        paint.setStrokeWidth(3);

    for (int i = 1; i < listPoint1.size(); i++) {
        pathValue1.reset();
        pathValue1.moveTo(listPoint1.get(i - 1).x, listPoint1.get(i - 1).y);
        pathValue1.lineTo(listPoint1.get(i).x, listPoint1.get(i).y);
        pathValue1.moveTo(listPoint1.get(i).x, listPoint1.get(i).y);
        pathValue1.close();
        canvas.drawPath(pathValue1, paintDraw);

    }
    for (int i = 1; i < listPoint2.size(); i++) {
            pathValue2.reset();
            pathValue2.moveTo(listPoint2.get(i - 1).x, listPoint2.get(i - 1).y);
            pathValue2.lineTo(listPoint2.get(i).x, listPoint2.get(i).y);
            pathValue2.moveTo(listPoint2.get(i).x, listPoint2.get(i).y);
            pathValue2.close();
            canvas.drawPath(pathValue2, paintDraw2);

        }
        for (int i = 1; i < listPoint3.size(); i++) {
            pathValue3.reset();
            pathValue3.moveTo(listPoint3.get(i - 1).x, listPoint3.get(i - 1).y);
            pathValue3.lineTo(listPoint3.get(i).x, listPoint3.get(i).y);
            pathValue3.moveTo(listPoint3.get(i).x, listPoint3.get(i).y);
            pathValue3.close();
            canvas.drawPath(pathValue3, paintDraw3);

        }
    }

    public class GraphViewPerso extends SurfaceView implements SurfaceHolder.Callback, Runnable {


        Thread thread = null;

        public GraphViewPerso(Context context) {
            super(context);
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            thread = new Thread(this);
            thread.start();
            setFocusable(true);
        }


        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            largeur = preview.getWidth();
            hauteur = preview.getHeight() - 10;
        }


        public GraphViewPerso(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            initParam();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

        @Override
        public void run() {
            Canvas canvas = null;
            while (active) {
                if (mHolder.getSurface().isValid()) {
                    canvas = mHolder.lockCanvas();
                    try {
                        Thread.sleep(sleepValue);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    canvas.drawColor(Color.WHITE);
                    DessinGraphY(canvas);
                    DessinGraphX(canvas);
                    dessinPointX(canvas);
                    dessinValueX(canvas);


                    mHolder.unlockCanvasAndPost(canvas);

                }

            }

        }

        public void onPause() {
            active = false;
            while (true) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
            thread = null;
        }

        public void onResume() {

            active = true;
            thread = new Thread(this);
            thread.start();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        graphPerso.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        graphPerso.onPause();
    }
}
