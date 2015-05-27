/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.epsi.i4.kitchenguesser;

import fr.epsi.i4.kitchenguesser.entities.Things;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author paul
 */
public class Thing implements Comparable<Thing> {
    //private final int[][] heuristic = {{3,1,0,-2,-3},{1,3,0,-1,-2},{0,0,0,0,0},{-2,-1,0,3,1},{-3,-2,0,1,3}};
    private final int[][] heuristic = {{3,3,0,-2,-3},{2,3,0,-1,-2},{0,0,0,0,0},{-2,-1,0,3,2},{-3,-2,0,3,3}};
    
    private String name;
    private HashMap<Integer, Integer> answers;
    private int score;
    
    public Thing(String name) {
        this.name       = name;
        this.score      = 0;
        this.answers    = new HashMap<>();
    }
    
    public String getName() {
        return this.name;
    }
    
    public void addAnswer(int questionId, int answer) {
        this.answers.put(questionId, answer);
    }
    
    public int getAnswer(int questionId){
        if (this.answers.containsKey(questionId)){
            return this.answers.get(questionId);
        } else {
            return 0;
        }
    }
    
    public int getScore() {
        return this.score;
    }
    
    public void updateScore(int questionId, int givenAnswer){
        int expectedAnswer = getAnswer(questionId);
        
        if (expectedAnswer > 0)
            score += heuristic[givenAnswer-1][expectedAnswer-1];
    }
    
    public Things getDBObject(EntityManager em) {
        Query query = em.createNamedQuery("Things.findByName");
        query.setParameter("name", this.name);
        return (Things) query.getSingleResult();
    }
    
    @Override
    public String toString(){
        return this.name+"("+this.score+")";
    }

    @Override
    public int compareTo(Thing o) {
        return o.score - this.score;
    }
}
