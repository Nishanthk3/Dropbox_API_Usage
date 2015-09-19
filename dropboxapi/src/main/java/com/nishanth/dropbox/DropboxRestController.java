package com.nishanth.dropbox;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

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

	private AccessTokenClass accessTokenClass = null;

	private static final String accountInfoUrl = "https://api.dropboxapi.com/1/account/info/";
	private static final String getFilesURL = "https://content.dropboxapi.com/1/files/auto/";
	private static final String updateFileUrl = "https://content.dropboxapi.com/1/files_put/auto/";
	private static final String addFileUrl = "https://content.dropboxapi.com/1/files_put/auto/";

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
			accessTokenClass = new ObjectMapper().readValue(str, AccessTokenClass.class);
			System.out.println("Access Token : "+accessTokenClass.getAccess_token());
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException();
		}
		return "Code Retrieved";
	}

	@RequestMapping( value="/accountInfo",method = RequestMethod.GET)
	public @ResponseBody String accountInfo(HttpServletRequest httpReq)
	{
		String accountInfo = null;
		try {

			Client client = Client.create();

			WebResource webResource = client.resource(accountInfoUrl);

			ClientResponse response = webResource.header("Authorization", "Bearer "+accessTokenClass.getAccess_token()).get(ClientResponse.class);

			if (response.getStatus() != 200) {
				System.out.println("Response = "+response.getStatus());
				System.out.println("Content  = "+response.getEntity(String.class));
				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
			}
			accountInfo = response.getEntity(String.class);
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException();
		}
		return accountInfo;
	}

	@RequestMapping( value="/files",method = RequestMethod.GET)
	public @ResponseBody String files(@RequestParam("file") String file,HttpServletRequest httpReq)
	{
		String contentOfFileRequested = null;
		try {
			String input = getFilesURL+file;
			Client client = Client.create();
			WebResource webResource = client.resource(input);

			ClientResponse response = webResource.header("Authorization", "Bearer "+accessTokenClass.getAccess_token())
					.get(ClientResponse.class);

			if (response.getStatus() != 200) {
				System.out.println("Response = "+response.getStatus());
				System.out.println("Content  = "+response.getEntity(String.class));
				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
			}
			contentOfFileRequested = response.getEntity(String.class);
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException();
		}
		return contentOfFileRequested;
	}

	@RequestMapping( value="/update",method = RequestMethod.POST)
	public @ResponseBody String updateFile(@RequestParam("fileName") String fileName, @RequestParam("file") MultipartFile file,
			HttpServletRequest httpReq)
	{
		try {
			String input = updateFileUrl+fileName+"?override=true";

			Client client = Client.create();
			WebResource webResource = client.resource(input);

			File convFile = new File( file.getOriginalFilename());
			file.transferTo(convFile);

			ClientResponse response = webResource.header("Content-Length",convFile.length())
					.header("Authorization", "Bearer "+accessTokenClass.getAccess_token())
					.put(ClientResponse.class, convFile);
			if (response.getStatus() != 200) {
				System.out.println("Response = "+response.getStatus());
				System.out.println("Content  = "+response.getEntity(String.class));
				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
			}
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException();
		}
		return fileName+" Updated Successfully";
	}
	
	@RequestMapping( value="/add",method = RequestMethod.POST)
	public @ResponseBody String addFile(@RequestParam("fileName") String fileName, @RequestParam("file") MultipartFile file,
			HttpServletRequest httpReq)
	{
		try {
			String input = addFileUrl+fileName;

			Client client = Client.create();
			WebResource webResource = client.resource(input);

			File convFile = new File( file.getOriginalFilename());
			file.transferTo(convFile);

			ClientResponse response = webResource.header("Content-Length",convFile.length())
					.header("Authorization", "Bearer "+accessTokenClass.getAccess_token())
					.post(ClientResponse.class, convFile);
			if (response.getStatus() != 200) {
				System.out.println("Response = "+response.getStatus());
				System.out.println("Content  = "+response.getEntity(String.class));
				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
			}
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException();
		}
		return fileName+" Added Successfully";
	}
}