package com.dynamodbTestWithNoSql.demoWithNoSqlWorkbench.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamodbConfig {

    @Value("${aws.dynamodb.endpoint}")
    private String END_POINT;
    @Value("${aws.dynamodb.accessKey}")
    private String ACCESS_KEY;
    @Value("${aws.dynamodb.secretKey}")
    private String SECRET_KEY;


    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDbConfig());
    }

    private AmazonDynamoDB amazonDbConfig() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(END_POINT, "ap-southeast-2"))
                .withCredentials(awsCredentialProvider()).build();
    }

    private AWSCredentialsProvider awsCredentialProvider() {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY));
    }
}
