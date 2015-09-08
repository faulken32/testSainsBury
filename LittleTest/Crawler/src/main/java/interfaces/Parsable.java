/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import Exception.BadAnswerExp;
import Exception.CrawlerException;
import java.io.IOException;

/**
 *
 * @author t311372
 */
public interface Parsable {

    String parse(String URL) throws IOException, CrawlerException, BadAnswerExp;
    
}
