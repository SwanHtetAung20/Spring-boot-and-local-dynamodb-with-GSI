package com.dynamodbTestWithNoSql.demoWithNoSqlWorkbench.repositories;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.dynamodbTestWithNoSql.demoWithNoSqlWorkbench.enitities.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookRepo {

    private final DynamoDBMapper dynamoDBMapper;


    public Book save(Book book) {
        dynamoDBMapper.save(book);
        return book;
    }

    public List<Book> findBooksById(String id) {
        Book book = new Book();
        book.setId(id);
        DynamoDBQueryExpression<Book> queryExpression = new DynamoDBQueryExpression<Book>()
                .withHashKeyValues(book)
                .withLimit(10); // optional
        return dynamoDBMapper.query(Book.class, queryExpression);
    }

    public String deleteBook(String id, String date) {;
        var book = Book.builder()
                .id(id)
                .created_date(date)
                .build();
        dynamoDBMapper.delete(book);
        return "Successfully Deleted =>  " + date;
    }

    public String updateBook(String id, String date, Book book) {
        dynamoDBMapper.save(book, dynamoDbSaveConfig(id, date));
        return "Updated Successfully";
    }

    public List<Book> getAllBooks() {
        return dynamoDBMapper.scan(Book.class, new DynamoDBScanExpression());
    }

    private DynamoDBSaveExpression dynamoDbSaveConfig(String id, String date) {
        DynamoDBSaveExpression expression = new DynamoDBSaveExpression();
        Map<String, ExpectedAttributeValue> expectedAttributeValueMap = new HashMap<>();
        expectedAttributeValueMap.put("id", new ExpectedAttributeValue(new AttributeValue().withS(id)));
        expectedAttributeValueMap.put("created_date", new ExpectedAttributeValue(new AttributeValue().withS(date)));
        expression.setExpected(expectedAttributeValueMap);
        return expression;
    }


}
