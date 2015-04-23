/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.epsi.i4.kitchenguesser;

import fr.epsi.i4.kitchenguesser.entities.Questions;
import fr.epsi.i4.kitchenguesser.entities.Things;
import fr.epsi.i4.kitchenguesser.entities.ThingsQuestions;
import java.util.ArrayList;
import java.util.HashMap;
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
    
    private HashMap<Integer, Question> questions;
    private HashMap<Integer, Thing> things;
    
    public Engine() {
        em = Persistence.createEntityManagerFactory("KitchenGuesserPU").createEntityManager();
        
        resetQuestions();
        resetThings();
    }
    
    public void start() {
        System.out.println(getFirstQuestion().getQuestion());
        //System.out.println(getDataset().getNom());
        
        System.out.println(things);
    }
    
    private Questions getFirstQuestion(){
        Query query = em.createNamedQuery("Questions.findAll");
        List<Questions> questions = query.getResultList();
        
        int firstQuestion = (int) (Math.random()*(questions.size()-1));
        
        return questions.get(firstQuestion);
    }
    
    private void resetQuestions() {
        questions = new HashMap<>();
        
        Query query = em.createNamedQuery("Questions.findAll");
        List<Questions> bdQuestions = query.getResultList();
        bdQuestions.stream().forEach((bdQuestion) -> {
            questions.put(bdQuestion.getId(), new Question(bdQuestion.getQuestion()));
        });
    }
    
    private void resetThings() {
        things = new HashMap<>();
        
        Query query = em.createNamedQuery("Things.findAll");
        List<Things> bdThings = query.getResultList();
        bdThings.stream().forEach((bdThing) -> {
            Thing newThing = new Thing(bdThing.getName());
            
            Query thingAnswerQuery = em.createNamedQuery("ThingsQuestions.findByThingId");
            thingAnswerQuery.setParameter("thingId", bdThing.getId());
            List<ThingsQuestions> thingAnswers = thingAnswerQuery.getResultList();
            thingAnswers.stream().forEach((thingAnswer) -> {
                newThing.addAnswer(thingAnswer.getQuestionId(), thingAnswer.getValue());
            });
            
            things.put(bdThing.getId(), newThing);
        });
    }
}
