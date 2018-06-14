package au.gov.vic.sro.revenueguideapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Path("/")
public class ApiController {
	
private static String dataFile = "resources/guideData.json";
	
	private String getFileWithUtil(String fileName) {
		String result = null;
		ClassLoader classLoader = getClass().getClassLoader();
		try {
		    result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	@GET
	@Path("/getGuideData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLatestRevenueGuideData(@QueryParam("oldVersion") String oldVersion) {
		try {
			int oldVer = 0;
			try {
				oldVer = Integer.parseInt(oldVersion);
			} catch (Exception e) {
				return Response.status(400).build();
			}
			JSONParser parser = new JSONParser(); 
            JSONObject jsonObject = (JSONObject) parser.parse(getFileWithUtil(dataFile));
            int currentVersion = (int) (long) jsonObject.get("version");
            if (currentVersion > oldVer) {
            	return Response.status(200).entity(jsonObject.toString()).build();
            }
			return Response.notModified().build();
		}
		catch(Exception e) {
			return Response.serverError().build();
		}
	}
}
