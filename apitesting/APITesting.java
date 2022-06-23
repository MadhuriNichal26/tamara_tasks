package com.apitesting;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class APITesting {
	@BeforeMethod
	public void format() {
		System.out.println("-------New Test Case----------");
	}

	@Test(priority = 1)
	public void test1_CheckStatus() {
		Response res = RestAssured.get("https://reqres.in/api/users?page=2");
		Assert.assertEquals(res.statusCode(), 200);
	}

	@Test(priority = 2)
	public void test2_CheckValue() {
		given().get("https://reqres.in/api/users?page=2").then().statusCode(200).body("data.id[0]", equalTo(7)).log()
				.all();
	}

	@Test(priority = 3)
	public void test3_CheckContains() {
		baseURI = "https://reqres.in/api";
		given().get("/users?page=2").then().statusCode(200).body("data.id[1]", equalTo(8)).body("data.first_name",
				hasItems("Tobias", "Byron", "George"));
	}

	@Test(priority = 4)
	public void test4_CheckNewUserAdded() {
		JSONObject req = new JSONObject();
		req.put("name", "madhuri");
		req.put("job", "QA");
		baseURI = "https://reqres.in/api";
		given().header("content-type", "application/json").contentType(ContentType.JSON).accept(ContentType.JSON)
		.body(req.toJSONString()).when().put("/users/2").then().statusCode(200).log().all();
	}

	@Test(priority = 5)
	public void test5() {
		baseURI = "https://reqres.in/api";
		when().delete("/users/2").then().statusCode(204).log().all();
	}

	@Test(priority = 6)
	public void test6() {
		baseURI = "https://reqres.in/api";
		given().
			get("/users?page=2").
		then().assertThat()
			.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema.json")).statusCode(200).log().all();
	}

}
