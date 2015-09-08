/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exception;

/**
 *
 * @author t311372
 */
public class CrawlerException extends Exception {

    /**
     * Creates a new instance of <code>CrawlerException</code> without detail
     * message.
     */
    public CrawlerException() {
    }

    /**
     * Constructs an instance of <code>CrawlerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CrawlerException(String msg) {
        super(msg);
    }
}
