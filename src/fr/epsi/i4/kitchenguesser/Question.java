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
public class Question {
    private String question;
    
    public Question(String question) {
        this.question = question;
    }
    
    @Override
    public String toString(){
        return question;
    }
}