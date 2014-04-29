package in.tapmobi.athome.server;

import java.io.IOException;
import java.util.ArrayList;

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
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ServerAPI {
	public static ArrayList<UserProfile> mUserProfile;

	private static String getServerUrlForOperation(String operation, Boolean flag) {
		String url = null;
		if (flag) {
			url = "http://sonycrbt.tapmobi.in/ServiceV1/" + operation;
		} else {
			url = "http://athome.elasticbeanstalk.com/api/" + operation;
		}
		return url;
	}

	public static ServerResponse PostDataWithXml(String requestXml, String operation, String requestMethod, Boolean isRegisteration) {
		String strUrl = getServerUrlForOperation(operation, isRegisteration);
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

		ServerResponse serverResponse = PostDataWithXml(null, "Msisdn/" + guidCode, "GET", true);

		return serverResponse.getResponseString();

	}

	// ////////////////////////// MISC /////////////////////////////////
	// private static Document getDomElement(String xml) {
	// Document doc = null;
	// DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	// try {
	// DocumentBuilder db = dbf.newDocumentBuilder();
	// InputSource is = new InputSource();
	// is.setCharacterStream(new StringReader(xml));
	// doc = db.parse(is);
	//
	// } catch (ParserConfigurationException e) {
	// Log.e("Error: ", e.getMessage());
	// return null;
	// } catch (SAXException e) {
	// Log.e("Error: ", e.getMessage());
	// return null;
	// } catch (IOException e) {
	// Log.e("Error: ", e.getMessage());
	// return null;
	// }
	// // return DOM
	// return doc;
	// }

	// private static String getTagValue(String sTag, Element eElement) {
	// String response = null;
	// NodeList nList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	// if (eElement.getNodeType() == Node.ELEMENT_NODE) {
	// if (nList != null && nList.getLength() > 0) {
	// response = nList.item(0).getNodeValue();
	// }
	// }
	// return response;
	// }

	public static UserProfile registerUserSip(String Name, String Email, String verifiedMsisdn) {
		UserProfile user = new UserProfile();
		String registerUriJson = "{\"Msisdn\":" + "\"" + verifiedMsisdn + "\",\"Name\":" + "\"" + Name + "\",\"Email\":" + "\"" + Email + "\"" + "}";

		ServerResponse serverResponse = PostDataWithXml(registerUriJson, "Subscription", "POST", false);

		// responseString = serverresponse.getResponseString();
		try {

			JSONObject jObj = new JSONObject(serverResponse.getResponseString().toString());
			user.Msisdn = jObj.getString("Msisdn");
			user.Name = jObj.getString("Name");
			user.Email = jObj.getString("Email");
			user.SubscribedDate = jObj.getString("SubscribedDate");
			user.ValidityDate = jObj.getString("ValidityDate");
			user.SipPassword = jObj.getString("SipPassword");
			user.SipPort = jObj.getString("SipPort");
			user.SipServer = jObj.getString("SipServer");
			user.SipUsername = jObj.getString("SipUsername");
			// mUserProfile = new ArrayList<UserProfile>();
			// mUserProfile.add(user);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;

	}

	public static Boolean sendSms(String senderNumber, String receiverNumber, String text) {

		String registerUriJson = "{\"From\":" + "\"" + senderNumber + "\",\"To\":" + "\"" + receiverNumber + "\",\"Text\":" + "\"" + text + "\"" + "}";

		ServerResponse serverResponse = PostDataWithXml(registerUriJson, "Message", "POST", null);
		return serverResponse.getSuccess();
	}

}
