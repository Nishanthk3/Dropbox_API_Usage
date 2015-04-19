package com.nishanth.dropbox;

import java.awt.Desktop;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;

@Controller
public class DropboxRestController {

	private static final String Dropbox_URL = "https://www.dropbox.com/1/oauth2/authorize";
	private static final String AccessToken_URL = "https://api.dropbox.com/1/oauth2/token";
	private static final String Response_Type = "code";

	@Value("${dropbox.APP_KEY}")
	private String APP_KEY;

	@Value("${dropbox.APP_SECRET}")
	private String APP_SECRET;

	@Value("${dropbox.REDIRECT_URI}")
	private String REDIRECT_URI;

	@Value("${dropbox.ENCODE_VALUE}")
	private String ENCODE_VALUE;

	@RequestMapping( value="/", method = RequestMethod.GET)
	public @ResponseBody String intialPage()
	{
		return "Welcome to Dropbox API";
	}

	@RequestMapping( value="/dropbox")
	public @ResponseBody String oAuth()
	{
		byte[] byteEncodeValue = Base64.encodeBase64(ENCODE_VALUE.getBytes());
		String encodeValue = new String(byteEncodeValue);

		String url = Dropbox_URL+"?client_id="+APP_KEY+"&response_type="+Response_Type+"&redirect_uri="+REDIRECT_URI+"&state="+encodeValue;

		if(Desktop.isDesktopSupported()){
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.browse(new URI(url));
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}else{
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("xdg-open " + url);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
		return "redirecting to /mycallback";
	}

	@RequestMapping( value="/mycallback",method = RequestMethod.GET)
	public @ResponseBody String oAuthCallback( @RequestParam("code") String code, @RequestParam("state") String state, HttpServletRequest httpReq)
	{
		try {

			Client client = Client.create();

			WebResource webResource = client.resource(AccessToken_URL);

			String input = "code="+code+"&"
					+ "grant_type=authorization_code&"
					+ "client_id="+APP_KEY+"&"
					+ "client_secret="+APP_SECRET+"&"
					+ "redirect_uri="+REDIRECT_URI;

			ClientResponse response = webResource.type("application/x-www-form-urlencoded")
					.post(ClientResponse.class, input);

			if (response.getStatus() != 200) {
				System.out.println("Response = "+response.getStatus());
				System.out.println("Content  = "+response.getEntity(String.class));
				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
			}
			String str = response.getEntity(String.class);
			AccessTokenClass accessTokenClass = new ObjectMapper().readValue(str, AccessTokenClass.class);
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException();
		}
		return "Code Retrieved";
	}

}