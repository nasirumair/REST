/**
 * 
 */
package com.REST;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
/**
 * @author tcs_2071202
 *
 */
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class WebServiceTest  {

	private Client client;
	private String REST_SERVICE_URL = "https://localhost:8443/UserManagement/rest/UserService/users/";
	private static final String PASS = "pass";
	private static final String FAIL = "fail";


	static {
		//for localhost testing only
		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
				new javax.net.ssl.HostnameVerifier(){

					public boolean verify(String hostname,
							javax.net.ssl.SSLSession sslSession) {
						if (hostname.equals("localhost")) {
							return true;
						}
						return false;
					}
				});
	}


	public static Client IgnoreSSLClient() throws Exception {
		SSLContext sslcontext = SSLContext.getInstance("TLS");
		sslcontext.init(null, new TrustManager[]{new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

			@Override
			public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }


		}}, new java.security.SecureRandom());
		return ClientBuilder.newBuilder().sslContext(sslcontext).hostnameVerifier((s1, s2) -> true).build();
	}

	private void init(){

		try {
			this.client = IgnoreSSLClient();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		WebServiceTest tester = new WebServiceTest();
		//initialize the tester
		tester.init();
		//test for Post Text Entry
		tester.testPostText("This is the client text String");
		//test for retrieve all Text
		tester.testAllText("UmairNasir");
		//test for reply submission
		tester.testReplyText("UmairNasir", "This is UMAIRNASIR", "This is the reply Sent from the Client");
		
	}
	
	private void testReplyText(String user, String text, String reply) {
		// TODO Auto-generated method stub	
		Form form = new Form();
		form.param("text", text);
		form.param("user", user);
		form.param("reply", reply);
	
		String callResult = client
				.target(REST_SERVICE_URL)
				.path("/setReplyText")
				.request(MediaType.TEXT_PLAIN)
				.post(Entity.entity(form,
						MediaType.APPLICATION_FORM_URLENCODED),
						String.class);
	
		String result = PASS;
		if(reply.equals(callResult)){
			System.out.println("Test case name: testReplyText("+user+","+text+"), Result: " + result );
			System.out.println("--------------- Response: "+callResult );
		}else{
			System.out.println("Test case name: testReplyText, Result: " + result );
		}
		
		

	}

	private void testAllText(String user) {
		// TODO Auto-generated method stub
		GenericType<List<Text>> list = new GenericType<List<Text>>() {};
		List<Text> allText = client
				.target(REST_SERVICE_URL)
				.path("/getAll/"+user)
				.request(MediaType.APPLICATION_XML)
				.get(list);

		if(allText.isEmpty()){
			System.out.println("Test case name: testAllText, Result: FAIL : List returned is EMPTY");
		}else{
			for(Text t: allText){
				if(user.equals(t.getUserName())){
					continue;
				}else{
					System.out.println("Test case name: testAllText, Result: FAIL at index: "+allText.indexOf(t));
				}
			}
			System.out.println("Test case name: testAllText("+user+"), Result: pass");
			System.out.println("--------------- Response: "+allText.get(0).getUserName());
		}

	}

	private void testPostText(String text) {
		Form form = new Form();
		form.param("text", text);
		form.param("user", "UmairNasir");
		form.param("lat", "1");
		form.param("lng", "2");
		form.param("temp", "22");

		String callResult = client
				.target(REST_SERVICE_URL)
				.path("/setText")
				.request(MediaType.TEXT_PLAIN)
				.put(Entity.entity(form,
						MediaType.APPLICATION_FORM_URLENCODED),
						String.class);
		String result = PASS;
		String res = callResult.split(",")[0];
		if(!text.equals(res)){
			result = FAIL;
		}

		System.out.println("Test case name: testPostText("+text+"), Result: " + result );
		System.out.println("--------------- Response: "+res );

	}


}

