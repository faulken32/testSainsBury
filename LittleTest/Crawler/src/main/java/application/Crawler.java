/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.Products;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author t311372
 */
public class Crawler {

    private static Crawler instance;
    private String URL;
    private static final Logger LOG = LoggerFactory.getLogger(Crawler.class);
    private ArrayList<Products> productsList;

    private Crawler() {

    }

    public static Crawler getInstance() {

        if (Crawler.instance == null) {
            Crawler.instance = new Crawler();
        }

        return Crawler.instance;
    }

    public String parse(String URL) throws IOException {

        this.URL = URL;
        String catchedProducts = this.catchProducts(this.callWs());
        this.StringToObject(catchedProducts);

        return "";
    }

    private String callWs() {
        String jsonResult = null;

        try {

            HttpGet getRequest = new HttpGet(URL);
            getRequest.addHeader("accept", "application/json");
            HttpClientBuilder create = HttpClientBuilder.create();
            CloseableHttpClient build = create.build();
            CloseableHttpResponse response = build.execute(getRequest);
            InputStream content = response.getEntity().getContent();
            jsonResult = IOUtils.toString(content);

        } catch (IOException ex) {
            LOG.error(ex.getMessage());

        }
        return jsonResult;
    }

    /**
     *
     * @param jsonResult
     * @throws IOException
     */
    private String catchProducts(String jsonResult) {

        String res = null;
        if (jsonResult != null) {
            Pattern p = Pattern.compile("\"products\".*\"");
            Matcher m = p.matcher(jsonResult);
            while (m.find()) {
                String group = m.group(0);
                res = group;

            }
            res = "{" + res + "}]}";
        }

        return res;
    }

    /**
     *
     * @param res
     * @return
     * @throws IOException
     */
    private ArrayList<Products> StringToObject(String res) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        LinkedHashMap readValue = (LinkedHashMap) mapper.readValue(res, Object.class);
        Collection values = readValue.values();
        ArrayList<LinkedHashMap> next = (ArrayList) values.iterator().next();
        productsList = new ArrayList<>();
        for (LinkedHashMap next1 : next) {
            String values1 = (String) next1.values().iterator().next();
            this.extractUnitPrice(values1);
            productsList.add(new Products(values1));
        }

        return this.productsList;
    }

    private void extractUnitPrice(String text) {

        String res = null;

        Pattern p = Pattern.compile("<p.*\"pricePerUnit\">(.*)<abbr title=\"per\">");
        Matcher m = p.matcher(text);
        while (m.find()) {
           
        String toro ="";
        }
    }

}
