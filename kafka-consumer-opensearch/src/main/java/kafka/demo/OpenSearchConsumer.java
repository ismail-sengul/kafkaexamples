package kafka.demo;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

public class OpenSearchConsumer {

    public static RestHighLevelClient createOpenSearchClient() {
        //For your local connection String
        String connectionString = "localhost:9200";

        RestHighLevelClient restHighLevelClient;
        URI connectionUri = URI.create(connectionString);

        //extract login information if it exist
        String userInfo = connectionUri.getUserInfo();

        if (userInfo != null) {
            restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(connectionUri.getHost(), connectionUri.getPort(), connectionUri.getScheme())));
        }else {
            String [] auth = userInfo.split(":");

            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(auth[0], auth[1]));

            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(new HttpHost(connectionUri.getHost(), connectionUri.getPort(), connectionUri.getScheme()))
                            .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                                    .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy()))
            );
        }
        return restHighLevelClient;
    }

    private static final Logger logger = LoggerFactory.getLogger(OpenSearchConsumer.class.getSimpleName());

    public static void main(String[] args) throws IOException {

        //First client the OpenSearch Client
        RestHighLevelClient openSearchClient = createOpenSearchClient();

        //Create the index of OpenSearch if it does not exist already

        try (openSearchClient){

            boolean indexExist = openSearchClient.indices().exists(new GetIndexRequest("wikimedia"), RequestOptions.DEFAULT);

            if(!indexExist){
                CreateIndexRequest createIndexRequest = new CreateIndexRequest("wikimedia");
                openSearchClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
                logger.info("Wikimedia index has been created");
            }
        }

        //Create our Kafka Client

        //main code logic

        //close things
        openSearchClient.close();


    }
}
