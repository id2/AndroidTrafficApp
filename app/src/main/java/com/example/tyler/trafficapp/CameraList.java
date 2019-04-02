package com.example.tyler.trafficapp;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class CameraList extends AppCompatActivity {

    private RecyclerView camList;
    private RecyclerView.Adapter camAdapter;
    private RecyclerView.LayoutManager camLayout;
    private CameraAdapter camAd;
    private EditText searchBar;
    ArrayList<Camera> cameras = new ArrayList<>();
    ArrayList<Camera> cameraArrayList = new ArrayList<>();

    //String[][] camArray = loadArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_list);

        cameraArrayList = loadArray();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerCam);
        camAd = new CameraAdapter(this,cameraArrayList);
        recyclerView.setAdapter(camAd);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        searchBar = findViewById(R.id.editText);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable search) {
                searchCameras(search.toString());
            }
        });

    }

    void searchCameras(String searchText){
        ArrayList<Camera> filteredCams = new ArrayList();

        for(Camera cam: cameraArrayList){
            if(cam.getCameraName().toLowerCase().contains(searchText.toLowerCase())){
                filteredCams.add(cam);
            }
        }

        camAd.filteredList(filteredCams);
    }


    public ArrayList<Camera> loadArray(){

        new Thread( new Runnable() {
            @Override
            public void run() {

                try {

                    String protocol = "http://";
                    //this will continually have to be updated, everytime, to the ip of the computer running the restful server thing
                    String ip = "192.168.1.4";
                    String urlS = ":8080/CameraMSSQL/webresources/com.mycompany.cameramssql.camerasfrench/1/250";
                    String urlString = protocol + ip + urlS;
                    URL url = null;
                    try {
                        url = new URL(urlString);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    URLConnection conn = null;
                    try {
                        conn = url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = null;
                    try {
                        builder = factory.newDocumentBuilder();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    }

                    Document docb = null;
                    try {
                        docb = builder.parse(conn.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }

                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer xform = null;
                    try {
                        xform = transformerFactory.newTransformer();
                    } catch (TransformerConfigurationException e) {
                        e.printStackTrace();
                    }

                    StringWriter writer = new StringWriter();
                    StreamResult result = new StreamResult(writer);

                    String stresult = null;
                    try {
                        xform.transform(new DOMSource(docb), result);
                        stresult = writer.toString();
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    }

                    String[] camerasAll = stresult.split("<camerasFrench>");
                    int len = camerasAll.length;
                    String[][] cameraInfo = new String[len - 1][10];
                    for (int i = 1; i < len; ++i) {
                        cameraInfo[i - 1] = camerasAll[i].split("<\\s*[/a-zA-Z]+\\s*>");
                        if (cameraInfo[i - 1][7].contains("&amp;")) {
                            cameraInfo[i - 1][7] = cameraInfo[i - 1][7].replace("&amp;", "&");
                        }
                    }

                    //id is in 1, latitude 3, longitude 5, name 7
                    for (int i = 0; i < cameraInfo.length; ++i) {
                        String cameraName = cameraInfo[i][7];
                        if (getResources().getConfiguration().locale.getLanguage() == "fr") {

                            cameraName = cameraInfo[i][9];

                        }
                        String cameraLong = cameraInfo[i][5];
                        String cameraLat = cameraInfo[i][3];
                        String cameraId = cameraInfo[i][1];
                        cameras.add(new Camera(cameraName, cameraId, cameraLong, cameraLat));
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (cameras.size() > 0) {
                    Collections.sort(cameras, new Comparator<Camera>() {
                        @Override
                        public int compare(final Camera object1, final Camera object2) {
                            return object1.getCameraName().compareTo(object2.getCameraName());
                        }

                    });
                }


            }

        }).start();
        return cameras;
    }


}
