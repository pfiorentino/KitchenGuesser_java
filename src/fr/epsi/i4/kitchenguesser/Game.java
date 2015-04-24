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
import java.util.Map;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author paul
 */
public class Game {
    private static final int MIN_SCORE_TO_KEEP = -6;
    private static final int FIRST_QUESTION_TO_CLEAN = 4;
    
    private EntityManager em;
    
    private HashMap<Integer, Question> questions;
    private HashMap<Integer, Thing> things;
    
    private ArrayList<UserAnswer> currentGame;
    
    public Game() {
        em          = Persistence.createEntityManagerFactory("KitchenGuesserPU").createEntityManager();
        currentGame = new ArrayList<>();
        things      = new HashMap<>();
        questions   = new HashMap<>();
    }
    
    public void start() {
        resetQuestions();
        resetThings();
        currentGame.clear();
        
        Scanner scanner = new Scanner(System.in);
        String input = "";
        
        Question question = getRandomQuestion();
        
        while (!input.equals("q")){
            //System.out.println(things.size());
            System.out.println(question+" (y/py/u/pn/n)");
            input = scanner.nextLine();
            
            int answer = 0;
            switch (input){
                case "y":
                    answer = 1;
                    break;
                case "py":
                    answer = 2;
                    break;
                case "u":
                    answer = 3;
                    break;
                case "pn":
                    answer = 4;
                    break;
                case "n":
                    answer = 5;
                    break;
                case "q":
                    break;
                default:
                    System.out.println("Invalid input");
            }
            
            if (answer > 0){
                currentGame.add(new UserAnswer(question.getId(), answer));
                updateThingsScore(question.getId(), answer);
                
                if (currentGame.size() >= FIRST_QUESTION_TO_CLEAN) {
                    cleanThingsList();
                }
                
                questions.remove(question.getId());
                question = getBestQuestion();
            }
            
            Thing bestThing = getBestThing();
            float precision = ((float) bestThing.getScore()/(currentGame.size()*3))*100;
            System.out.println("Question n°"+currentGame.size()+" - Best thing: "+bestThing+" ("+precision+"%)\n");
        }
    }
    
    private Question getRandomQuestion(){
        int randIndex = (int) (Math.random()*(questions.size()-1));
        return questions.get(randIndex);
    }
    
    private Question getBestQuestion() {
        int maxScore = -1;
        Question bestQuestion = null;
        
        for (Map.Entry<Integer, Question> entry : questions.entrySet()) {
            int questionScore = getScore(entry.getKey());
            
            //System.out.println(entry.getValue().toString()+": "+questionScore);
            
            if (maxScore < questionScore){
                maxScore = questionScore;
                bestQuestion = entry.getValue();
            }
        }
        
        return bestQuestion;
    }
    
    private Thing getBestThing() {
        int bestScore = -1;
        Thing bestThing = null;
        
        for (Map.Entry<Integer, Thing> entry : things.entrySet()){
            if (bestScore < entry.getValue().getScore()){
                bestScore = entry.getValue().getScore();
                bestThing = entry.getValue();
            }
        }
        
        return bestThing;
    }
    
    private void updateThingsScore(int questionId, int answer) {
        for (Map.Entry<Integer, Thing> entry : things.entrySet()){
            entry.getValue().updateScore(questionId, answer);
        }
    }
    
    private void cleanThingsList() {
        ArrayList<Integer> keysToDelete = new ArrayList<>();
        
        for (Map.Entry<Integer, Thing> entry : things.entrySet()){
            if (entry.getValue().getScore() <= MIN_SCORE_TO_KEEP){
                keysToDelete.add(entry.getKey());
            }
        }
        
        for (Integer keyToDelete : keysToDelete) {
            things.remove(keyToDelete);
        }
    }
    
    private void resetQuestions() {
        questions.clear();
        
        Query query = em.createNamedQuery("Questions.findAll");
        List<Questions> bdQuestions = query.getResultList();
        bdQuestions.stream().forEach((bdQuestion) -> {
            questions.put(bdQuestion.getId(), new Question(bdQuestion.getId(), bdQuestion.getQuestion()));
        });
    }
    
    private void resetThings() {
        things.clear();
        
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
    
    private int getScore(int questionId){
        int score = 1;
        
        int[] answers = {1,1,1,1,1,1};
        
        for (Map.Entry<Integer, Thing> entry : things.entrySet()) {
            answers[entry.getValue().getAnswer(questionId)]++;
        }
        
        for (int answer : answers) {
            score *= answer;
        }
        
        return score;
    }
}
