package in.tapmobi.athome.server;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public class ServerAPI {

	private static String getServerUrlForOperation(String operation) {
		String url = "http://sonycrbt.tapmobi.in/ServiceV1/" + operation;
		return url;
	}

	public static ServerResponse PostDataWithXml(String requestXml, String operation, String requestMethod) {
		String strUrl = getServerUrlForOperation(operation);
		String finalResult = null;
		ServerResponse sr = new ServerResponse();
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = null;

		StringEntity se = null;
		try {
			if (requestXml != null) {
				se = new StringEntity(requestXml);
				se.setContentType("text/json");
			}
			if (requestMethod.equals("POST")) {
				HttpPost httpPost = new HttpPost(strUrl);
				httpPost.setEntity(se);

				response = client.execute(httpPost);
			} else if (requestMethod.equals("GET")) {
				HttpGet httpGet = new HttpGet(strUrl);
				httpGet.addHeader(new BasicHeader("Accept", "text/json"));

				response = client.execute(httpGet);
			} else if (requestMethod.equals("PUT")) {
				HttpPut httpPut = new HttpPut(strUrl);
				httpPut.setEntity(se);

				response = client.execute(httpPut);
			} else if (requestMethod.equals("DELETE")) {
				HttpDelete httpDelete = new HttpDelete(strUrl);

				response = client.execute(httpDelete);
			}

			sr.setSuccess(false);
			sr.setErrorMessage(null);
			sr.setResponseString(null);

			System.out.println("RequestUrl: " + strUrl);
			System.out.println("RequestXML: " + requestXml);
			Log.d("serverapi", "" + response.getStatusLine().getStatusCode());
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				finalResult = EntityUtils.toString(responseEntity);
				Log.d("serverapi", "Response: " + finalResult);
				if (response.getStatusLine().getStatusCode() == 200) {
					sr.setSuccess(true);
					sr.setResponseString(finalResult);
				} else {
					sr.setErrorMessage(finalResult);
				}
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sr;
	}


	// VERIFICATION
	public static String verifyMSISDN(String guidCode) {

		ServerResponse serverResponse = PostDataWithXml(null, "Msisdn/" + guidCode, "GET");

		return serverResponse.getResponseString();

	}

	// ////////////////////////// MISC /////////////////////////////////
	private static Document getDomElement(String xml) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (SAXException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		}
		// return DOM
		return doc;
	}

	private static String getTagValue(String sTag, Element eElement) {
		String response = null;
		NodeList nList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		if (eElement.getNodeType() == Node.ELEMENT_NODE) {
			if (nList != null && nList.getLength() > 0) {
				response = nList.item(0).getNodeValue();
			}
		}
		return response;
	}

}
