/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.epsi.i4.kitchenguesser;

import fr.epsi.i4.kitchenguesser.entities.Questions;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author paul
 */
public class KitchenGuesser {
    private EntityManager em;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        KitchenGuesser kg = new KitchenGuesser();
        kg.findAll();
    }
    
    public KitchenGuesser() {
        em = Persistence.createEntityManagerFactory("KitchenGuesserPU").createEntityManager();
    }
    
    public void findAll() {
        Query query = em.createNamedQuery("Questions.findAll");
        List<Questions> questions = query.getResultList();
        
        int firstQuestion = (int) (Math.random()*(questions.size()-1));
        
        System.out.println(questions.get(firstQuestion).getQuestion());
        
        
    }
}
