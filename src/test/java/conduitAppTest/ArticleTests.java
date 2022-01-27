package conduitAppTest;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class ArticleTests {
	
	@Test
	public void verifyGetArticle() {
		
		//1. Create a request
		//2. Send a request
		//3. Receive the response and 
		//4. verify or validate it
		
		//Create a request
		RequestSpecification request = RestAssured.given();
		
		request.baseUri("http://localhost:3000/api/articles/saurabh-kvxi9v");
		
		request.header("Authorization", "Token eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NCwidXNlcm5hbWUiOiJzYXVyYWJodGVzdCIsImV4cCI6MTY0ODQ3MDUwNSwiaWF0IjoxNjQzMjg2NTA1fQ.OL809pVREzOKWGtqW-a5G_KQ-ZJmOir1H4fUMe88Ogg", null);
		
		//Send a request and receiving a response
		
		Response response = request.when().get();
		
		response.prettyPrint();
		
		String responseAsString = response.asString();
		
		System.out.println(responseAsString);
		
		//Validation 
		ValidatableResponse vResponse = response.then();
		
		vResponse.statusCode(200);
		vResponse.statusLine("HTTP/1.1 200 OK");
		
		
	}

}
