package com.example.artsandcrafts;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Request {

        private String USER_AGENT = "Mozilla/5.0";
    private String url = "";
    private String parameters, output;
    private String token = "";

    public Request() {
        this.url = "";
        this.parameters = "";
        this.output = "";
    }

    // Function adds a parameter to the existing string
    public void addParameter(String key, String val) {
        if (this.parameters == "")
            this.parameters += key + "=" + val;
        else
            this.parameters += "&" + key + "=" + val;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void makeGetCall() throws Exception {
        // Init connection objects
        URL obj = new URL(getUrl());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Adding request header (-GET-)
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("X-XAPP-Token", token);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Saving the output
        this.output = response.toString();
    }

    // HTTP POST request
    public void makeCall() throws Exception {

        // Init connection objects
        URL obj = new URL(getUrl());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Adding request header (-POST-)
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Gets the parameters
        String urlParameters = getParameter();

        // Sending post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        // Reading the incoming stream
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Saving the output
        this.output = response.toString();
    }

    // Function returns the parameter string
    public String getParameter() {
        return this.parameters;
    }

    // Function empties the url & parameter string
    public void clean() {
        this.url = "";
        this.parameters = "";
    }

    // Function sets the url
    public void setUrl(String urlString) {
        this.url = urlString;
    }

    // Function gets the url
    public String getUrl() {
        return this.url;
    }

    // Prints out the output
    public String toString() {
        return this.output;
    }
}
