package stepdefs;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.context.ScenarioContext;
import org.example.pojo.AddBookResponse;
import org.example.pojo.BookByAuthor;
import org.example.pojo.BookDeleteById;
import org.example.pojo.LibraryBook;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static io.restassured.RestAssured.*;

public class LibraryStepdefs {

    @Autowired
    private ScenarioContext scenarioContext;

    private final String baseURL = "https://rahulshettyacademy.com/";
    //or use //http://216.10.245.166/

    @Given("books are added with details {string} {string} {string} {string}")
    public void booksAreAddedWithDetails(String name, String isbn, String aisle, String author) {
        LibraryBook libraryBook = new LibraryBook();
        libraryBook.setName(name);
        libraryBook.setIsbn(isbn);
        libraryBook.setIsle(aisle);
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
                .when().get("GetBook.php")
                .then().extract().response();
        System.out.println("all books by author " + response.getBody().asString());
        if (response.statusCode() != 200) {
            System.out.println(" status code is " + response.statusCode());
            System.out.println("Response is " + response.getBody());
        } else {
            System.out.println("all books by author " + response.getBody().asString());
            try {
                List<BookByAuthor> books = response.jsonPath().getList("", BookByAuthor.class);
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
                            .when().delete("DeleteBook.php")
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
                .when().get("GetBook.php")
                .then().extract().response();
        LibraryBook book = getBookResponse.jsonPath().getList("", LibraryBook.class).getFirst();
        scenarioContext.setData("BookById", book);

    }

    @And("validate book name is {string} and author is {string}")
    public void validateBookNameIsAndAuthorIs(String name, String author) {
        LibraryBook book = (LibraryBook) scenarioContext.getData("BookById");
        Assert.assertEquals(" book name comparison ", name, book.getName());
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
        int addBookResponseCode = (int) scenarioContext.getData("responseCode");
        Assert.assertNotEquals("response code comparison", statusCode, addBookResponseCode);
    }

    @Given("books are added with details")
    public void booksAreAddedWithDetails() throws IOException {
        String inputPayload = new String(Files.readAllBytes(Path.of("src/test/resources/inputFiles/book.json")));
        RequestSpecBuilder addBookRequestBuilder = new RequestSpecBuilder()
                .setBaseUri(baseURL)
                .setContentType(ContentType.JSON)
                .setBody(inputPayload);
        Response response = given().spec(addBookRequestBuilder.build())
                .when().post("/Library/Addbook.php")
                .then().extract().response();
        String responseBody = response.getBody().asString();
        if (responseBody.trim().isEmpty()) {
            throw new RuntimeException("API returned empty response body");
        }
        else {
           System.out.println("api is "+responseBody);
        }
    }
}