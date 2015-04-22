/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.epsi.i4.kitchenguesser;

import fr.epsi.i4.kitchenguesser.entities.Question;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author paul
 */
public class Engine {
    private EntityManager em;
    
    public Engine() {
        em = Persistence.createEntityManagerFactory("KitchenGuesserPU").createEntityManager();
    }
    
    public void start() {
        System.out.println(getFirstQuestion().getQuestion());
    }
    
    private Question getFirstQuestion(){
        Query query = em.createNamedQuery("Question.findAll");
        List<Question> questions = query.getResultList();
        
        int firstQuestion = (int) (Math.random()*(questions.size()-1));
        
        return questions.get(firstQuestion);
    }
}
