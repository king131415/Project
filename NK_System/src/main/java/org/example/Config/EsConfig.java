package org.example.Config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

import java.util.ArrayList;

@Configuration
public class EsConfig {

    @Value("${elasticsearch.url}")
    private String edUrl;

    @Bean
     RestHighLevelClient client(){
        ClientConfiguration clientConfiguration=ClientConfiguration.builder()
                .connectedTo(edUrl)
                .build();
        return RestClients.create(clientConfiguration).rest();
        //后面的rest就是风格


    }
}
