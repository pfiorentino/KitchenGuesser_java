/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.epsi.i4.kitchenguesser;

import java.util.HashMap;

/**
 *
 * @author paul
 */
public class Thing {
    private String name;
    private HashMap<Integer, Integer> answers;
    
    public Thing(String name) {
        this.name = name;
        answers = new HashMap<>();
    }
    
    public void addAnswer(int questionId, int answer) {
        answers.put(questionId, answer);
    }
    
    @Override
    public String toString(){
        return name;
    }
}
