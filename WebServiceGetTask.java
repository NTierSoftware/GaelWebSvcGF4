package com.ntier.android.REST;

import java.io.*;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.*;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class WebServiceGetTask extends AsyncTask<String, Integer, String> {
	private Context mContext = null;
	private String processMessage = "Processing...";

	private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

	private ProgressDialog pDlg = null;

	@SuppressWarnings("hiding")
	public WebServiceGetTask(Context mContext, String processMessage) {
		this.mContext = mContext;
		this.processMessage = processMessage;
	}

	public void addNameValuePair(String name, String value) {

		params.add(new BasicNameValuePair(name, value));
	}

	private void showProgressDialog() {

		pDlg = new ProgressDialog(mContext);
		pDlg.setMessage(processMessage);
		pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDlg.setCancelable(false);
		pDlg.show();

	}

	@Override
	protected void onPreExecute() {

		//hideKeyboard();
		showProgressDialog();

	}

	protected void trustEveryone(boolean trust) {// FOR TESTING/DEV PURPOSE ONLY!!! HIGHLY INSECURE!!!
		// see: http://stackoverflow.com/questions/1217141/self-signed-ssl-acceptance-android/1607997#1607997
		if (!trust) return;

		try {
			HttpsURLConnection
					.setDefaultHostnameVerifier(new HostnameVerifier() {
						public boolean verify(String hostname,
								SSLSession session) {
							return true;
						}
					});
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null,
					new X509TrustManager[] { new X509TrustManager() {
						public void checkClientTrusted(
								X509Certificate[] chain, String authType)
								throws CertificateException {
						}

						public void checkServerTrusted(
								X509Certificate[] chain, String authType)
								throws CertificateException {
						}

						public X509Certificate[] getAcceptedIssuers() {
							return new X509Certificate[0];
						}
					} }, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(context
					.getSocketFactory());
		} catch (Exception e) { // should never happen
			e.printStackTrace();
		}
	}// trustEveryone


	@Override
	protected String doInBackground(String... urls) {
		String result = "";
		HttpsURLConnection urlConnection = null;
		InputStream in = null;
		try {
			URL url = new URL(urls[0]);
			trustEveryone(true);
			urlConnection = (HttpsURLConnection) url.openConnection();
			//debugging
			InputStream ins = urlConnection.getInputStream();
			in = new BufferedInputStream(ins );
			result = inputStreamToString(in);
			in.close();
		} // TODO Auto-generated catch block
		catch (IOException e) { e.printStackTrace(); } 
		finally {
			if (urlConnection != null) urlConnection.disconnect();

		}
		return result;
	}//doInBackground

	@Override
	protected void onPostExecute(String response) {
		   //send update 
		   Intent intentUpdate = new Intent();
		  // intentUpdate.setAction(ACTION_MyUpdate);
		   intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
		  // intentUpdate.putExtra(EXTRA_KEY_UPDATE, i);
		  // sendBroadcast(intentUpdate);
		   LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentUpdate);
		//handleResponse(response);
		pDlg.dismiss();

	}

	protected String inputStreamToString(InputStream is) {
		StringBuilder total = new StringBuilder();

		try {
			// Wrap a BufferedReader around the InputStream
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(is));
			String line;
			// Read response until the end
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
			rd.close();
		} catch (IOException e) {
			Log.e("TAG", e.getLocalizedMessage(), e);
		}
		// Return full string
		return total.toString();
	}//inputStreamToString
}//class WebServiceGetTask

class WebServicePostTask extends WebServiceGetTask{
	public Location location = new Location();
	
	public WebServicePostTask(Context mContext, String processMessage) {
		super(mContext, processMessage);
		location.provider = "from client: " + processMessage;
		location.id = 44;
	}

	@SuppressWarnings("resource")
	@Override
	protected String doInBackground(String... urls) {
		String result = "";
		HttpsURLConnection urlConnection = null;

		
		   try {
			 URL url = new URL(urls[0]);
			 trustEveryone(true);

			 urlConnection = (HttpsURLConnection) url.openConnection();

		     urlConnection.setDoOutput(true);
			 urlConnection.setRequestMethod("POST");
		     urlConnection.setDoOutput(true);
		     urlConnection.setChunkedStreamingMode(0);
		     urlConnection.setRequestProperty("Content-Type","application/json");   

		     urlConnection.connect();
		     //debug
		     OutputStream OUTPUTSTREAM = urlConnection.getOutputStream();
		     OutputStream out = new BufferedOutputStream(OUTPUTSTREAM);
		     writeJsonStream(out, this.location);
		     out.close();
		     InputStream in = new BufferedInputStream(urlConnection.getInputStream());
		     result = inputStreamToString(in);
			} 
		   catch (IOException e) { 
			   e.printStackTrace(); 
			   @SuppressWarnings("null")
			   InputStream errIn = new BufferedInputStream(urlConnection.getErrorStream());
			   result = inputStreamToString(errIn);
		   } 
		   finally { if (urlConnection != null) urlConnection.disconnect(); }

		   return result;
	}//doInBackground

	
	public void writeJsonStream(OutputStream out, List<Message> messages) throws IOException {
		Gson gson = new Gson();
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out)); //, "UTF-8"));
        writer.setIndent("  ");
        writer.beginArray();
        for (Message message : messages) {
            gson.toJson(message, Message.class, writer);
        }
        writer.endArray();
        writer.close();
    }

	public void writeJsonStream(OutputStream out, Location myloc) throws IOException {
		Gson gson = new Gson();
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out)); //, "UTF-8"));
        writer.setIndent("  ");
        gson.toJson(myloc, Location.class, writer);
        writer.close();
    }

	
	
}//WebServicePostTask
