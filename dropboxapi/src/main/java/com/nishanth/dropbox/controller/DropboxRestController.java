package com.nishanth.dropbox.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.nishanth.dropbox.api.AccessTokenClass;
import com.nishanth.dropbox.api.AccountInfo;
import com.nishanth.dropbox.api.Ack;
import com.nishanth.dropbox.api.MetadataInfo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/")
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
	private static final String metaData = "https://api.dropboxapi.com/1/metadata/auto/";
	private static final String updateFileUrl = "https://content.dropboxapi.com/1/files_put/auto/";
	private static final String addFileUrl = "https://content.dropboxapi.com/1/files_put/auto/";
	private static final String signOut = "https://api.dropboxapi.com/1/disable_access_token";

	private String displayName = null;
	
	ObjectMapper objMapper = new ObjectMapper();
	
	@RequestMapping(value = "main", method = RequestMethod.GET)
	public void intialPage(ModelMap model, HttpServletRequest httpReq) 
	{
		model.addAttribute("name", displayName);
		
	}

	@RequestMapping( value="signIn")
	public @ResponseBody String oAuth() throws IOException
	{
		byte[] byteEncodeValue = Base64.encodeBase64(ENCODE_VALUE.getBytes());
		String encodeValue = new String(byteEncodeValue);

		String url = Dropbox_URL+"?client_id="+APP_KEY+"&response_type="+Response_Type+"&redirect_uri="+REDIRECT_URI+"&state="+encodeValue;

		String os = System.getProperty("os.name").toLowerCase();
		if(os.indexOf( "win" ) >= 0)
		{
			Runtime rt = Runtime.getRuntime();
			rt.exec( "rundll32 url.dll,FileProtocolHandler " + url);
		}
		else if(os.indexOf( "mac" ) >= 0)
		{
			Runtime rt = Runtime.getRuntime();
			rt.exec( "open " + url);
		}
		else
		{
			if(Desktop.isDesktopSupported()){
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(new URI(url));
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException();
				}
			}
		}
		return "redirect:/main";
	}

	@RequestMapping( value="/mycallback",method = RequestMethod.GET)
	public String oAuthCallback( @RequestParam("code") String code, @RequestParam("state") String state, HttpServletRequest httpReq)
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
			
			WebResource webResource1 = client.resource("http://localhost:8080/accountInfo.html");
			ClientResponse response1 = webResource1.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				System.out.println("Response = "+response.getStatus());
				System.out.println("Content  = "+response.getEntity(String.class));
				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
			}
			if (response1.getStatus() != 200) {
				System.out.println("Response = "+response.getStatus());
				System.out.println("Content  = "+response.getEntity(String.class));
				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
			}
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException();
		}
		
		return "redirect:/main";
	}

	@RequestMapping( value="signOut",method = RequestMethod.GET)
	public @ResponseBody String signOut(HttpServletRequest httpReq, HttpServletResponse httpResp)
	{
		String signOutInfo = null;
		try {

			Client client = Client.create();

			WebResource webResource = client.resource(signOut);

			ClientResponse response = webResource.header("Authorization", "Bearer "+accessTokenClass.getAccess_token()).get(ClientResponse.class);

			if(response.getStatus() == 200) 
			{
				signOutInfo = response.getEntity(String.class);
				accessTokenClass.setAccess_token(null);
				displayName = null;
				AboutController.NAME = null;
			}
			else if(response.getStatus() == 401)
			{
				httpResp.setStatus(401);
				return response.getEntity(String.class);
			}
			else if(response.getStatus() == 404)
			{
				httpResp.setStatus(404);
				return response.getEntity(String.class);
			}
			else 
			{
				System.out.println("Response = "+response.getStatus());
				System.out.println("Content  = "+response.getEntity(String.class));
				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
			}
			
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException();
		}
		return signOutInfo;
	}

	@RequestMapping( value="accountInfo",method = RequestMethod.GET)
	public @ResponseBody String accountInfo(HttpServletRequest httpReq, HttpServletResponse httpResp)
	{
		String accountInfo = null;
		try {

			Client client = Client.create();

			WebResource webResource = client.resource(accountInfoUrl);

			ClientResponse response = webResource.header("Authorization", "Bearer "+accessTokenClass.getAccess_token()).get(ClientResponse.class);

			AccountInfo account = null;
			if(response.getStatus() == 200) 
			{
				accountInfo = response.getEntity(String.class);
				account = objMapper.readValue(accountInfo, AccountInfo.class);
				displayName = account.getDisplay_name();
				AboutController.NAME = displayName;
				
			}
			else if(response.getStatus() == 401)
			{
				httpResp.setStatus(401);
				return response.getEntity(String.class);
			}
			else if(response.getStatus() == 404)
			{
				httpResp.setStatus(404);
				return response.getEntity(String.class);
			}
			else 
			{
				System.out.println("Response = "+response.getStatus());
				System.out.println("Content  = "+response.getEntity(String.class));
				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
			}
			
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException();
		}
		return accountInfo;
	}
	
	@RequestMapping( value="files",method = RequestMethod.GET)
	public @ResponseBody String files(@RequestParam("fileName") String fileName,HttpServletRequest httpReq, HttpServletResponse httpResp)
	{
		String contentOfFileRequested = null;
		try {
			String input = getFilesURL+fileName;
			Client client = Client.create();
			WebResource webResource = client.resource(input);
			ClientResponse response = null;
			if(accessTokenClass != null && accessTokenClass.getAccess_token() != null)
			{
				response = webResource.header("Authorization", "Bearer "+accessTokenClass.getAccess_token())
						.get(ClientResponse.class);
			}
			else
				return "Not Signed In";

			if(response.getStatus() == 200) 
			{
				contentOfFileRequested = response.getEntity(String.class);
			}
			else if(response.getStatus() == 401)
			{
				httpResp.setStatus(401);
				return response.getEntity(String.class);
			}
			else if(response.getStatus() == 404)
			{
				httpResp.setStatus(404);
				return response.getEntity(String.class);
			}
			else 
			{
				System.out.println("Response = "+response.getStatus());
				System.out.println("Content  = "+response.getEntity(String.class));
				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
			}
			
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException();
		}
		return contentOfFileRequested;
	}
	@RequestMapping( value="metadata",method = RequestMethod.GET, produces ={"application/json"})
	public @ResponseBody Ack metadata(HttpServletRequest httpReq, HttpServletResponse httpResp)
	{
		MetadataInfo metadata = null;
		Ack ack = null;
		try {
			String input = metaData;
			Client client = Client.create();
			WebResource webResource = client.resource(input);
			ClientResponse response = null;
			if(accessTokenClass != null && accessTokenClass.getAccess_token() != null)
			{
				response = webResource.header("Authorization", "Bearer "+accessTokenClass.getAccess_token())
						.accept(MediaType.APPLICATION_JSON)
						.get(ClientResponse.class);
			}
			else
			{
				ack = new Ack();
				Error err = new Error("Not Signed In");
				ack.setError(err);
				return ack;
			}

			if(response.getStatus() == 200) 
			{
				String str = response.getEntity(String.class);
				metadata = objMapper.readValue(str, MetadataInfo.class);
			}
			else if(response.getStatus() == 401)
			{
				httpResp.setStatus(401);
				ack = new Ack();
				Error err = new Error(response.getEntity(String.class));
				ack.setError(err);
				return ack;
			}
			else if(response.getStatus() == 404)
			{
				httpResp.setStatus(404);
				ack = new Ack();
				Error err = new Error(response.getEntity(String.class));
				ack.setError(err);
				return ack;
			}
			else 
			{
				System.out.println("Response = "+response.getStatus());
				System.out.println("Content  = "+response.getEntity(String.class));
				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
			}
			
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException();
		}
		ack = new Ack();
		ack.setMetadata(metadata);;
		return ack;
	}
	/** When updating from Advanced REST clients or SOAP UI */
	@RequestMapping( value="/update",method = RequestMethod.POST)
	public @ResponseBody String updateFile(@RequestParam("fileName") String fileName, @RequestParam("file") MultipartFile file,
			HttpServletRequest httpReq, HttpServletResponse httpResp)
	{
		try {
			String input = updateFileUrl+fileName+"?override=true";

			Client client = Client.create();
			WebResource webResource = client.resource(input);

			File convFile = new File( file.getOriginalFilename());
			file.transferTo(convFile);

			ClientResponse response = null;
			if(accessTokenClass != null && accessTokenClass.getAccess_token() != null)
			{
				response = webResource.header("Content-Length",convFile.length())
						.header("Authorization", "Bearer "+accessTokenClass.getAccess_token())
						.put(ClientResponse.class, convFile);
			}
			else
				return "Not Signed In";
			
			if(response.getStatus() == 200) 
			{
			}
			else if(response.getStatus() == 401)
			{
				httpResp.setStatus(401);
				return response.getEntity(String.class);
			}
			else if(response.getStatus() == 404)
			{
				httpResp.setStatus(404);
				return response.getEntity(String.class);
			}
			else 
			{
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

	@RequestMapping( value="add",method = RequestMethod.POST)
	public @ResponseBody String addFile(@RequestParam("fileName") String fileName, @RequestParam("file") MultipartFile file,
			HttpServletRequest httpReq, HttpServletResponse httpResp)
	{
		try {
			if(fileName.contains("fakepath") && fileName.contains("\\"))
			{
				fileName = fileName.substring(fileName.lastIndexOf("\\")+1, fileName.length());
			}
			String input = addFileUrl+fileName;

			Client client = Client.create();
			WebResource webResource = client.resource(input);

			File convFile = new File( file.getOriginalFilename());
			file.transferTo(convFile);

			ClientResponse response = null;
			if(accessTokenClass != null && accessTokenClass.getAccess_token() != null)
			{
				response = webResource.header("Content-Length",convFile.length())
						.header("Authorization", "Bearer "+accessTokenClass.getAccess_token())
						.post(ClientResponse.class, convFile);
			}
			else
				return "Not Signed In";
			
			if(response.getStatus() == 200) 
			{
			}
			else if(response.getStatus() == 401)
			{
				httpResp.setStatus(401);
				return response.getEntity(String.class);
			}
			else if(response.getStatus() == 404)
			{
				httpResp.setStatus(404);
				return response.getEntity(String.class);
			}
			else 
			{
				System.out.println("Response = "+response.getStatus());
				System.out.println("Content  = "+response.getEntity(String.class));
				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
			}
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException();
		}
		return "Uploaded Successfully";
	}
	
	@RequestMapping( value="updateFile",method = RequestMethod.POST)
	public @ResponseBody String updateFile(@RequestParam("fileContent") String fileContent, @RequestParam("fileName") String fileName, 
			HttpServletRequest httpReq, HttpServletResponse httpResp)
	{
		try {
			String input = updateFileUrl+fileName+"?override=true";

			Client client = Client.create();
			WebResource webResource = client.resource(input);

			ClientResponse response = null;
			if(accessTokenClass != null && accessTokenClass.getAccess_token() != null)
			{
				response = webResource.header("Content-Length",fileContent.length())
						.header("Authorization", "Bearer "+accessTokenClass.getAccess_token())
						.put(ClientResponse.class, fileContent);
			}
			else
				return "Not Signed In";
			
			if(response.getStatus() == 200) 
			{
			}
			else if(response.getStatus() == 401)
			{
				httpResp.setStatus(401);
				return response.getEntity(String.class);
			}
			else if(response.getStatus() == 404)
			{
				httpResp.setStatus(404);
				return response.getEntity(String.class);
			}
			else 
			{	
				System.out.println("Response = "+response.getStatus());
				System.out.println("Content  = "+response.getEntity(String.class));
				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
			}
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException();
		}
		return "Updated Successfully";
	}
}