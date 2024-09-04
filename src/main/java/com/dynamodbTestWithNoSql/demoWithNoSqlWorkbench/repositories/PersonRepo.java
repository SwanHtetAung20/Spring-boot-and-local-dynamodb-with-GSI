package com.dynamodbTestWithNoSql.demoWithNoSqlWorkbench.repositories;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.dynamodbTestWithNoSql.demoWithNoSqlWorkbench.enitities.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PersonRepo {

    private final DynamoDBMapper dynamoDBMapper;


    public Person save(Person person) {
        dynamoDBMapper.save(person);
        return person;
    }


    public Person findById(String id) {
        return dynamoDBMapper.load(Person.class, id);
    }

    public List<Person> findAll() {
        return dynamoDBMapper.scan(Person.class, new DynamoDBScanExpression());
    }

    public String update(String id, Person person) {
        dynamoDBMapper.save(person, dynamodBSaveExpression(id));
        return "Successfully updated";
    }

    public String delete(String id) {
        var person = dynamoDBMapper.load(Person.class, id);
        dynamoDBMapper.delete(person);
        return "Successfully deleted";
    }

    private DynamoDBSaveExpression dynamodBSaveExpression(String id) {
        DynamoDBSaveExpression expression = new DynamoDBSaveExpression();
        Map<String, ExpectedAttributeValue> expectedAttributeValueMap = new HashMap<>();
        expectedAttributeValueMap.put("personId", new ExpectedAttributeValue(new AttributeValue().withS(id)));
        expression.setExpected(expectedAttributeValueMap);
        return expression;
    }


}
