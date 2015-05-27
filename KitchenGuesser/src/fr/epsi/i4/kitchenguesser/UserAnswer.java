/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.epsi.i4.kitchenguesser;

/**
 *
 * @author paul
 */
public class UserAnswer {
    private int questionId;
    private int answer;
    
    public UserAnswer(int questionId, int answer){
        this.questionId = questionId;
        this.answer     = answer;
    }
    
    public int getQuestionId() {
        return this.questionId;
    }
    
    public int getValue() {
        return this.answer;
    }
    
    @Override
    public String toString() {
        return "Question "+questionId+": "+answer;
    }
}
