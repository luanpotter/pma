package br.com.dextra.pma.main;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import br.com.dextra.pma.models.Project;

public final class Wrapper {

	private Wrapper() {
		throw new RuntimeException("Should not be instanciated.");
	}

	public static List<Project> getProjects() {
		return null;
	}

	private String post(String url) {
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url);

			String token_cache = "todo!";

			// Request parameters and other properties
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("token", token_cache));
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			// Execute and get the response
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				try (InputStream instream = entity.getContent()) {
					// do something useful
				}
			} else {
				throw new IOException("Response expected, none returned...");
			}
		} catch (IOException ex) {
			// todo handle!!
		}
	}

	private String url(String url) {
		StringBuilder response = new StringBuilder();
		try (BufferedReader r = new BufferedReader(new InputStreamReader(
				new URL(url).openStream(), "ISO-8859-1"))) {
			String line;
			while ((line = r.readLine()) != null) {
				response.append(line);
				response.append('\n');
			}
		} catch (EOFException e) {
			// expected
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return response.toString();
	}
}
