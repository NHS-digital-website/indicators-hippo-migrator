package uk.nhs.digital.ps.migrator.misc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class ApiHelper {


    public static Map<String, String> loadDocumentLookup(String apiUrl, Map<String, String> mapDocToId, int offset) {

        try {
            URL url = new URL(apiUrl + offset);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            conn.disconnect();


            //create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            //read JSON like DOM Parser
            JsonNode rootNode = objectMapper.readTree(sb.toString());


            Iterator<JsonNode> elements = rootNode.path("items").elements();
            while (elements.hasNext()) {
                JsonNode itemNode = elements.next();
                mapDocToId.put(itemNode.path("name").asText(), itemNode.path("id").asText());
            }

            int resultsOffset = rootNode.path("offset").asInt();
            boolean resultsMore = rootNode.path("more").asBoolean();

            // Max results returned is 100, so need to grab more results?
            if (resultsMore) {
                loadDocumentLookup(apiUrl, mapDocToId,resultsOffset + 100);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return mapDocToId;

    }

}
