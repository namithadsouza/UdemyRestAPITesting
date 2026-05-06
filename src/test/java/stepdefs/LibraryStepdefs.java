package stepdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.context.ScenarioContext;
import org.example.pojo.*;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;

public class LibraryStepdefs {

    @Autowired
    private ScenarioContext scenarioContext;

    private final String baseURL = "http://216.10.245.166/";
    //or use //http://216.10.245.166/ or https://rahulshettyacademy.com/

    @Given("books are added with details {string} {string} {string} {string}")
    public void booksAreAddedWithDetails(String name, String isbn, String aisle, String author) {
        Book libraryBook = new Book();
        libraryBook.setName(name);
        libraryBook.setIsbn(isbn);
        libraryBook.setAisle(aisle);
        libraryBook.setAuthor(author);
        //http://216.10.245.166/Library/Addbook.php
        //method = POST
        RequestSpecBuilder addBookRequestBuilder = new RequestSpecBuilder()
                .setBaseUri(baseURL)
                .setContentType(ContentType.JSON)
                .setBody(libraryBook);
        Response response = given().spec(addBookRequestBuilder.build())
                .when().post("/Library/Addbook.php")
                .then().extract().response();
        String responseBody = response.getBody().asString();
        if (responseBody.trim().isEmpty()) {
            throw new RuntimeException("API returned empty response body");
        }
        AddBookResponse addBookResponse;
        try {
            addBookResponse = response.as(AddBookResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response as AddBookResponse: " + e.getMessage(), e);
        }
        System.out.println(" response is " + addBookResponse.toString());
        scenarioContext.setData("addBookResponse", addBookResponse);
        scenarioContext.setData("addBookResponseCode", 200);
    }

    @Given("books are deleted for {string}")
    public void booksAreDeletedFor(String authorName) {
        //http://216.10.245.166/Library/GetBook.php?AuthorName=Johnfoe
        RequestSpecBuilder addBookRequestBuilder = new RequestSpecBuilder()
                .setBaseUri(baseURL)
                .addQueryParam("AuthorName", authorName);
        Response response = given().spec(addBookRequestBuilder.build())
                .when().get("/Library/GetBook.php")
                .then().extract().response();
        if (response.statusCode() != 200) {
            System.out.println(" status code is " + response.statusCode());
            System.out.println("Response is " + response.getBody().asString());
        } else {
            System.out.println("all books by author " + response.getBody().asString());
            try {
                BookByAuthor[] books = response.as(BookByAuthor[].class);
                for (BookByAuthor book : books) {
                    String bookId = book.getIsbn() + book.getAisle();
                    //http://216.10.245.166/Library/DeleteBook.php
                    BookDeleteById deleteBook = new BookDeleteById();
                    deleteBook.setID(bookId);
                    RequestSpecBuilder deleteBookRequestBuilder = new RequestSpecBuilder()
                            .setBaseUri(baseURL)
                            .setContentType(ContentType.JSON)
                            .setBody(deleteBook);
                    Response deleteBookResponse = given().spec(deleteBookRequestBuilder.build())
                            .when().delete("/Library/DeleteBook.php")
                            .then().extract().response();
                    Assert.assertEquals("delete response ", 200, deleteBookResponse.getStatusCode());
                }
            } catch (Exception e) {
                System.out.println("Failed to parse response as JSON array. Exception: " + e.getClass().getName() + " - " + e.getMessage());
                System.out.println("Response: " + response.getBody().asString());
                // Assume no books to delete
            }
        }
    }

    @Given("validate the books data is accurate by constructing id using {string} and {string}")
    public void validateTheBooksDataIsAccurateByConstructingIdUsingAnd(String isbn, String aisle) {
        String bookId = isbn + aisle;
        //http://216.10.245.166/Library/GetBook.php?ID=renis228
        RequestSpecBuilder getBookRequestBuilder = new RequestSpecBuilder()
                .setBaseUri(baseURL)
                .addQueryParam("ID", bookId);
        Response getBookResponse = given().spec(getBookRequestBuilder.build())
                .when().get("/Library/GetBook.php")
                .then().extract().response();
        String responseBody = getBookResponse.getBody().asString();
        if (responseBody.trim().isEmpty()) {
            throw new RuntimeException("API returned empty response body");
        }
        BookResponse book;
        try {
            BookResponse[] books = getBookResponse.as(BookResponse[].class);
            book = Arrays.stream(books).findFirst().get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response as AddBookResponse: " + e.getMessage(), e);
        }
        scenarioContext.setData("BookById", book);

    }

    @And("validate book name is {string} and author is {string}")
    public void validateBookNameIsAndAuthorIs(String name, String author) {
        BookResponse book = (BookResponse) scenarioContext.getData("BookById");
        Assert.assertEquals(" book name comparison ", name, book.getBook_name());
        Assert.assertEquals(" author name comparison ", author, book.getAuthor());
    }

    @Then("response status should contain a msg {string}")
    public void responseStatusShouldContainAMsg(String message) {
        AddBookResponse addBookResponse = (AddBookResponse) scenarioContext.getData("addBookResponse");
        Assert.assertEquals("message comparison", message, addBookResponse.getMsg());
    }

    @And("response message id should be combination of {string} and {string}")
    public void responseMessageIdShouldBeCombinationOfAnd(String isbn, String aisle) {
        AddBookResponse addBookResponse = (AddBookResponse) scenarioContext.getData("addBookResponse");
        Assert.assertEquals("Id comparison", isbn + aisle, addBookResponse.getID());
    }

    @But("response should not be {int}")
    public void responseShouldNotBe(int statusCode) {
        int addBookResponseCode = (int) scenarioContext.getData("addBookResponseCode");
        Assert.assertNotEquals("response code comparison", statusCode, addBookResponseCode);
    }
}