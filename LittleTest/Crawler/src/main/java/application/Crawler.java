/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import interfaces.Parsable;
import Exception.CrawlerException;
import Exception.BadAnswerExp;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ProductLists;
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
 * Main internal Class
 *
 * @author t311372
 */
public class Crawler implements Parsable {

    private static Crawler instance;
    private String URL;
    private static final Logger LOG = LoggerFactory.getLogger(Crawler.class);
    private String link;
    private String title;
    private Long size;

    private Crawler() {

    }

    public static Crawler getInstance() {

        if (Crawler.instance == null) {
            Crawler.instance = new Crawler();
        }

        return Crawler.instance;
    }

    /**
     *
     * @param URL
     * @return
     * @throws IOException
     * @throws CrawlerException
     * @throws BadAnswerExp
     */
    @Override
    public String parse(String URL) throws IOException, CrawlerException, BadAnswerExp {

        if (URL == null || URL.isEmpty()) {
            throw new CrawlerException("URL is null or emphty");
        }

        this.URL = URL;

        String catchedProducts = this.catchProducts(this.callWs());
        ProductLists StringToObject = this.StringToObject(catchedProducts);
        ObjectMapper mapper = new ObjectMapper();
        String writeValueAsString = mapper.writeValueAsString(StringToObject);

        return writeValueAsString;
    }
    
    
    /**
     * call was from url 
     * @return json String
     * @throws CrawlerException 
     */
    private String callWs() throws CrawlerException {
        
        
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
            throw new CrawlerException("no answer from server");

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
     * map object from string 
     * @param res
     * @return the product list 
     * @throws IOException
     */
    private ProductLists StringToObject(String res) throws CrawlerException, BadAnswerExp {

        ObjectMapper mapper = new ObjectMapper();
        ProductLists productLists = new ProductLists();
        // very dirty part bad mapping from jackson because the full object is not set
        try {
            LinkedHashMap readValue = (LinkedHashMap) mapper.readValue(res, Object.class);
            Collection values = readValue.values();
            ArrayList<LinkedHashMap> next = (ArrayList) values.iterator().next();

            ArrayList<Products> localProductList = new ArrayList<>();
            Double totalPrize = 0.0;

            for (LinkedHashMap next1 : next) {

                String values1 = (String) next1.values().iterator().next();
                Double extractUnitPrice = this.extractUnitPrice(values1);
                this.extractLinkAndTitle(values1);
                String crawledDescriptionPage = this.crawlDescriptionPage();
                String extractDescription = this.extractDescription(crawledDescriptionPage);
                // calculate the total price
                totalPrize += extractUnitPrice;
                // instance a product 
                localProductList.add(new Products(extractDescription, title, extractUnitPrice, size));
            }
            
            
            productLists.setProduct(localProductList);
            productLists.setTotalPrice(totalPrize);
            
            
        } catch (IOException e) {
//            LOG.error(e.getMessage());
            throw new BadAnswerExp("bad answer from server");
        }

        return productLists;
    }

    /**
     *
     * @param text
     * @return double
     */
    private double extractUnitPrice(String text) {

        String res = null;

        Pattern p = Pattern.compile("<p.*\"pricePerUnit\">(.*)<abbr title=\"per\">", Pattern.DOTALL);
        Matcher m = p.matcher(text);
        while (m.find()) {
            String group = m.group(1);
            res = group.substring(3);
        }

        return Double.parseDouble(res);
    }

    
    private void extractLinkAndTitle(String text) {

        link = null;
        title = null;
        Pattern p = Pattern.compile("<div class=\"productNameAndPromotions\">.*<a href=\"(.*)\" >(.*)<img src=\".*\">", Pattern.DOTALL);
        Matcher m = p.matcher(text);
        while (m.find()) {
            link = m.group(1).trim();
            title = m.group(2).trim();
        }

    }

    /**
     *
     * call webpages from url link for each product
     * @return @throws IOException
     */
    private String crawlDescriptionPage() throws IOException {

        String htmlRes = null;

        if (this.link != null) {

            HttpGet getRequest = new HttpGet(link);
            HttpClientBuilder create = HttpClientBuilder.create();
            CloseableHttpClient build = create.build();
            CloseableHttpResponse response = build.execute(getRequest);
            InputStream content = response.getEntity().getContent();
            htmlRes = IOUtils.toString(content);
            // render the file siez in kB
            size = (long) htmlRes.length() / 1014;

        }

        return htmlRes;
    }

    /**
     * not a good pratice but quick should have only one regex for perf 
     * @param htmlRes
     * @return
     */
    private String extractDescription(String htmlRes) {

        String res = null;

        Pattern p = Pattern.compile("<div class=\"productText\">(.*)<\\/div>.*<h3 class=\"productDataItemHeader\">Nutrition<\\/h3>", Pattern.DOTALL);
        Matcher m = p.matcher(htmlRes);
        while (m.find()) {
            res = m.group(1);
            // remove html tag

            res = res.replaceAll("\\<[^>]*>", "").trim();

        }
        return res;
    }

}
