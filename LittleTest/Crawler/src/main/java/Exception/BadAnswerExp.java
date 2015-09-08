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
public class BadAnswerExp extends Exception {

    /**
     * Creates a new instance of <code>badAnswerExp</code> without detail
     * message.
     */
    public BadAnswerExp() {
    }

    /**
     * Constructs an instance of <code>badAnswerExp</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public BadAnswerExp(String msg) {
        super(msg);
    }
}
