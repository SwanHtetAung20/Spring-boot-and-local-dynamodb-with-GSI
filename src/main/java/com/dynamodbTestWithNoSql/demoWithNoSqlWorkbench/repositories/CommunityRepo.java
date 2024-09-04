package com.dynamodbTestWithNoSql.demoWithNoSqlWorkbench.repositories;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.dynamodbTestWithNoSql.demoWithNoSqlWorkbench.enitities.Community;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommunityRepo {

    private final DynamoDBMapper dynamoDBMapper;


    public Community save(Community community) {
        dynamoDBMapper.save(community);
        return community;
    }


    public List<Community> findCommunitiesById(String id) {
        var community = Community.builder()
                .id(id)
                .build();
        try {

            DynamoDBQueryExpression<Community> query = new DynamoDBQueryExpression<Community>()
                    .withHashKeyValues(community)
                    .withLimit(10);
            return dynamoDBMapper.query(Community.class, query);
        } catch (AmazonServiceException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Community> findAllCommunities() {
        return dynamoDBMapper.scan(Community.class, new DynamoDBScanExpression());
    }

    public String deleteCommunity(String id, String date) {
        var community = Community.builder()
                .id(id)
                .created_date(date)
                .build();
        dynamoDBMapper.delete(community);
        return "Successfully Deleted";
    }


    //should provide both hash key and sort key which is good approach for clarity
    public String updateCommunity(String id, String date, Community community) {
        if (date == null) {
            throw new IllegalArgumentException("Date (sort key) must be provided to uniquely identify the item.");
        } else {
            dynamoDBMapper.save(community, dynamoDbSaveExpressionConfig(id, date));
        }
        return "Update Successfully";
    }


    public List<Community> findCommunitiesByName(String name) {
        Map<String, String> attributeNameMap = new HashMap<>();
        attributeNameMap.put("#name", "name");  // when your attribute is reserved word then you need to add alias for this
        Map<String, AttributeValue> attributeValueMap = new HashMap<>();
        attributeValueMap.put(":name", new AttributeValue().withS(name));
        try {
            DynamoDBQueryExpression<Community> queryExpression = new DynamoDBQueryExpression<Community>()
                    .withIndexName("gsi-test")
                    .withConsistentRead(false)
                    .withKeyConditionExpression("#name = :name")
                    .withExpressionAttributeNames(attributeNameMap)
                    .withExpressionAttributeValues(attributeValueMap)
                    .withLimit(10);
            return dynamoDBMapper.query(Community.class, queryExpression);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }


    public List<Community> findCommunitiesByStatus(String status) {
        Map<String, AttributeValue> eav = new HashMap<>();
        Map<String, String> ean = new HashMap<>();
        ean.put("#status", "status");
        eav.put(":status", new AttributeValue().withS(status));
        try {
            DynamoDBQueryExpression<Community> expression = new DynamoDBQueryExpression<Community>()
                    .withIndexName("gsi-index-name")
                    .withConsistentRead(false)
                    .withKeyConditionExpression("#status = :status")
                    .withExpressionAttributeNames(ean)
                    .withExpressionAttributeValues(eav)
                    .withLimit(10);
            return dynamoDBMapper.query(Community.class, expression);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private DynamoDBSaveExpression dynamoDbSaveExpressionConfig(String id, String date) {
        DynamoDBSaveExpression expression = new DynamoDBSaveExpression();
        Map<String, ExpectedAttributeValue> expectedAttributeValueMap = new HashMap<>();
        expectedAttributeValueMap.put("id", new ExpectedAttributeValue(new AttributeValue().withS(id)));
        expectedAttributeValueMap.put("created_date", new ExpectedAttributeValue(new AttributeValue().withS(date)));
        expression.setExpected(expectedAttributeValueMap);
        return expression;
    }

    //this is also fine
//    public String updateCommunity(String id, String date, Community community) {
//        if (date == null) {
//            dynamoDBMapper.save(community, dynamoDbSaveExpressionConfigForPartitionKey(id));
//        } else {
//            dynamoDBMapper.save(community, dynamoDbSaveExpressionConfig(id, date));
//        }
//        return "Update Successfully";
//    }
//
//    private DynamoDBSaveExpression dynamoDbSaveExpressionConfigForPartitionKey(String id) {
//        DynamoDBSaveExpression expression = new DynamoDBSaveExpression();
//        Map<String, ExpectedAttributeValue> expectedAttributeValueMap = new HashMap<>();
//        expectedAttributeValueMap.put("id", new ExpectedAttributeValue(new AttributeValue().withS(id)));
//        expression.setExpected(expectedAttributeValueMap);
//        return expression;
//    }



//    public List<Community> findCommunitiesByNameAndStatus(String name,String status){
//        Map<String,String> ean = new HashMap<>();
//        Map<String,AttributeValue> eav = new HashMap<>();
//        ean.put("#name","name");
//        ean.put("#status","status");
//        eav.put(":name",new AttributeValue().withS(name));
//        eav.put(":status",new AttributeValue().withS(status));
//        try{
//            DynamoDBQueryExpression<Community> queryExpression = new DynamoDBQueryExpression<Community>()
//                    .withIndexName("gsi-test-with-nameAndStatus")
//                    .withConsistentRead(false)
//                    .withKeyConditionExpression("#name = :name and #status = :status")
//                    .withExpressionAttributeNames(ean)
//                    .withExpressionAttributeValues(eav)
//                    .withLimit(10);
//            return dynamoDBMapper.query(Community.class,queryExpression);
//        }catch (Exception e){
//            throw  new RuntimeException(e.getMessage());
//        }
//    }


    // when your attribute is not reserved word then you don't need alias for this
//    public List<Community> findCommunitiesByName(String name) {
//        Map<String, AttributeValue> attributeValueMap = new HashMap<>();
//        attributeValueMap.put(":name", new AttributeValue().withS(name));
//        try {
//            DynamoDBQueryExpression<Community> queryExpression = new DynamoDBQueryExpression<Community>()
//                    .withIndexName("gsi-test")
//                    .withConsistentRead(false)
//                    .withKeyConditionExpression("name = :name")
//                    .withExpressionAttributeValues(attributeValueMap)
//                    .withLimit(10);
//            return dynamoDBMapper.query(Community.class, queryExpression);
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }
//
//    }

    // while querying from gsi the below following methods are not good approach so use above
//    public List<Community> findCommunitiesByName(String name) {
//        try {
//            DynamoDBQueryExpression<Community> queryExpression = new DynamoDBQueryExpression<Community>()
//                    .withHashKeyValues(Community.builder().name(name).build())
//                    .withIndexName("gsi-test")
//                    .withConsistentRead(false)
//                    .withLimit(10);
//            return dynamoDBMapper.query(Community.class, queryExpression);
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }
//
//    }

//    public List<Community> findCommunitiesByStatus(String status) {
//        try {
//            DynamoDBQueryExpression<Community> expression = new DynamoDBQueryExpression<Community>()
//                    .withHashKeyValues(Community.builder().status(status).build())
//                    .withIndexName("gsi-index-name")
//                    .withConsistentRead(false)
//                    .withLimit(10);
//            return dynamoDBMapper.query(Community.class, expression);
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }
//    }


}
