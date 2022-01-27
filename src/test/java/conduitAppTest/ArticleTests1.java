package conduitAppTest;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import conduitAppPojo.Article;
import conduitAppPojo.ArticlePojo;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ArticleTests1 {

	String token;

	@BeforeMethod
	public void setup() {
		baseURI = "http://localhost";

		token = "Token eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NCwidXNlcm5hbWUiOiJzYXVyYWJodGVzdCIsImV4cCI6MTY0ODQ3MDUwNSwiaWF0IjoxNjQzMjg2NTA1fQ.OL809pVREzOKWGtqW-a5G_KQ-ZJmOir1H4fUMe88Ogg";

		port = 3000;
	}

	@Test
	public void verifyGetArticle() {

		// Request
		given().basePath("/api/articles/saurabh-kvxi9v").header("Authorization", token)
				// Take the action
				.when().get()
				// Add validation
				.then().log().all().statusCode(200).statusLine("HTTP/1.1 200 OK");

	}

	@Test
	public void verifyGetAllArticles() {

		// Request
		given().basePath("/api/articles").header("Authorization", token).queryParam("limit", 2)
				// Take the action
				.when().get()
				// Add validation
				.then().log().all().statusCode(200).statusLine("HTTP/1.1 200 OK");

	}

	@Test
	public void addArticles() {

		String requestPayload = "{\r\n" + "    \"article\": {\r\n" + "        \"title\": \"T1\",\r\n"
				+ "        \"description\": \"T1\",\r\n" + "        \"body\": \"T1\",\r\n"
				+ "        \"tagList\": [\r\n" + "            \"Test\"\r\n" + "        ]\r\n" + "    }\r\n" + "}";

		given().basePath("/api/articles").header("Authorization", token).contentType(ContentType.JSON)
				.body(requestPayload)

				.when().post().then().log().all().statusCode(200);

	}

	@Test
	public void verifyAddArticleWhenPayloadIsMap() {

		Map<String, Object> requestPayload = new HashMap<>();

		Map<String, Object> article = new HashMap<>();

		article.put("title", "Learning API testing");
		article.put("description", "Learning API testing - description");
		article.put("body", "Learning API testing - body");

		List<String> tagList = new LinkedList<>();

		tagList.add("API");
		tagList.add("Rest Assured");

		article.put("tagList", tagList);

		requestPayload.put("article", article);

		given().basePath("/api/articles").header("Authorization", token).contentType(ContentType.JSON)
				.body(requestPayload)

				.when().post().then().log().all().statusCode(200);

	}

	@Test
	public void verifyEditArticleWhenPayloadIsPojo() {

		ArticlePojo requestPayload = new ArticlePojo();

		Article article1 = new Article();

		article1.setTitle("Learning REst assured for API Testing");
		article1.setBody("This is a body");
		article1.setDescription("This is an article description");

		List<String> tags = new LinkedList<>();

		tags.add("API");
		tags.add("Rest");

		article1.setTagList(tags);

		requestPayload.setArticle(article1);

		Response response = given().basePath("/api/articles").header("Authorization", token)
				.contentType(ContentType.JSON).body(requestPayload).when().post();

		String slug = response.then().extract().path("article.slug");

		System.out.println(slug);

		response.then().log().all().statusCode(200);

		// Update this same article

		ArticlePojo updatedRequestPayload = new ArticlePojo();

		Article article2 = new Article();

		article2.setTitle("Learning REst assured for API Testing - Part 2");
		article2.setBody("This is a body - part 2");
		article2.setDescription("This is an article description - Part 2");

		List<String> tags2 = new LinkedList<>();

		tags2.add("API");
		tags2.add("Rest");
		tags2.add("Restfull API");

		article2.setTagList(tags2);

		updatedRequestPayload.setArticle(article2);

		given().basePath("/api/articles/" + slug).header("Authorization", token).contentType(ContentType.JSON)
				.body(updatedRequestPayload).when().put().then().log().all().statusCode(200);

	}
	
	
	@Test
	public void verifyAddEditDeleteArticleWhenPayloadIsPojo() {
		
		//-----------------------------------------Add Article ----------------------------------------

		ArticlePojo requestPayload = new ArticlePojo();

		Article article1 = new Article();

		article1.setTitle("Learning REst assured for API Testing");
		article1.setBody("This is a body");
		article1.setDescription("This is an article description");

		List<String> tags = new LinkedList<>();

		tags.add("API");
		tags.add("Rest");

		article1.setTagList(tags);

		requestPayload.setArticle(article1);

		Response response = given().basePath("/api/articles").header("Authorization", token)
				.contentType(ContentType.JSON).body(requestPayload).when().post();

		String slug = response.then().extract().path("article.slug");

		System.out.println(slug);

		response.then().log().all().statusCode(200);

		// --------------------- Update this same article ------------------------------------------------------

		ArticlePojo updatedRequestPayload = new ArticlePojo();

		Article article2 = new Article();

		article2.setTitle("Learning REst assured for API Testing - Part 2");
		article2.setBody("This is a body - part 2");
		article2.setDescription("This is an article description - Part 2");

		List<String> tags2 = new LinkedList<>();

		tags2.add("API");
		tags2.add("Rest");
		tags2.add("Restfull API");

		article2.setTagList(tags2);

		updatedRequestPayload.setArticle(article2);

		Response updateResponse = given().basePath("/api/articles/" + slug).header("Authorization", token).contentType(ContentType.JSON)
				.body(updatedRequestPayload).when().put();
		
		String updatedTitle = updateResponse.then().extract().path("article.title");
		String updatedDescription = updateResponse.then().extract().path("article.description");;
		
		updateResponse.then().log().all().statusCode(200);
		
		Assert.assertEquals(updatedTitle, "Learning REst assured for API Testing - Part 2");
		Assert.assertEquals(updatedDescription, "This is an article description - Part 2");
		
		// ----------------------------------------- Delete article ----------------------------------------
		
		
		given().basePath("/api/articles/" + slug).header("Authorization", token)
		.when().delete().then().log().all().statusCode(204);
		
		// ------------------------------------------ Get article -------------------------------------------
		
		given().basePath("/api/articles/" + slug).header("Authorization", token)
		.when().get().then().log().all().statusCode(404);

	}

}
