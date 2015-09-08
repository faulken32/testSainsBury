/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Exception.BadAnswerExp;
import Exception.CrawlerException;
import application.Crawler;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * very tiny test case not realy relevant just for exemple 
 * @author t311372
 */
public class testApp {

    public testApp() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    
    /**
     *
     * @throws IOException
     * @throws CrawlerException
     * @throws Exception.BadAnswerExp
     */
    @Test(expected = CrawlerException.class)
    public void noUrl() throws IOException, CrawlerException, BadAnswerExp {

        String url = "";
        String parse = Crawler.getInstance().parse(url);
    }

    @Test(expected = BadAnswerExp.class)
    public void badUrl() throws CrawlerException, IOException, BadAnswerExp {

        String url = "http://error.com";
        String parse = Crawler.getInstance().parse(url);
    }
    
     @Test
    public void everythingFine()  throws IOException, CrawlerException, BadAnswerExp {

        String url = "http://www.sainsburys.co.uk/webapp/wcs/stores/servlet/AjaxApplyFilterBrowseView?langId=44&storeId=10151&catalogId=10122&categoryId=185749&parent_category_rn=12518&top_category=12518&pageSize=20&orderBy=FAVOURITES_FIRST&searchTerm=&beginIndex=0&facet=887";
        String parse = Crawler.getInstance().parse(url);
         Assert.assertNotNull(parse);
    }
}
