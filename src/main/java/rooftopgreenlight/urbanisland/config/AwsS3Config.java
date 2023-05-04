package rooftopgreenlight.urbanisland.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import rooftopgreenlight.urbanisland.config.resolver.AwsS3Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AwsS3Config {

    private final AwsS3Properties awsS3Properties;

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials credentials =
                new BasicAWSCredentials(awsS3Properties.getAccessKey(), awsS3Properties.getSecretKey());

        return (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withRegion(awsS3Properties.getRegion())
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
