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
import java.util.Collections;
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
    private static final int FIRST_QUESTION_TO_CLEAN = 3;
    
    private int next_purpose    = 10;
    private int purpose_number  = 0;
    
    private EntityManager em;
    
    private HashMap<Integer, Question> questions;
    private ArrayList<Thing> things;
    private ArrayList<UserAnswer> currentGame;
    
    public Game() {
        em          = Persistence.createEntityManagerFactory("KitchenGuesserPU").createEntityManager();
        currentGame = new ArrayList<>();
        things      = new ArrayList<>();
        questions   = new HashMap<>();
    }
    
    public void reset() {
        resetQuestions();
        resetThings();
        currentGame.clear();
        next_purpose = 10;
        purpose_number = 0;
    }
    
    public void start() {
        reset();
        
        Scanner scanner = new Scanner(System.in);
        String input = "";
        
        Question question = getRandomQuestion();
        
        while (!input.equals("q")){
            //System.out.println(things.size());
            System.out.println("\nQuestion n°"+currentGame.size()+": "+question+" (y/py/u/pn/n)");
            input = scanner.nextLine();
            
            if (input.equals("reset") || input.equals("q")){
                if (input.equals("reset")) {
                    reset();
                    question = getRandomQuestion();
                }
            } else {
                int answer = parseInput(input);

                if (answer > 0){
                    currentGame.add(new UserAnswer(question.getId(), answer));
                    updateThingsScore(question.getId(), answer);

                    if (currentGame.size() >= FIRST_QUESTION_TO_CLEAN) {
                        cleanThingsList();
                    }

                    questions.remove(question.getId());
                    question = getBestQuestion();

                    System.out.println(things);

                    float bestPrecision     = ((float) things.get(0).getScore()/(currentGame.size()*3))*100;
                    float secondPrecision   = ((float) things.get(0).getScore()/(currentGame.size()*3))*100;

    //                Thing[] bestThings = getBestThings();
    //                float firstPrecision = ((float) bestThings[0].getScore()/(currentGame.size()*3))*100;
    //                float secondPrecision = ((float) bestThings[1].getScore()/(currentGame.size()*3))*100;
    //
                    if ((currentGame.size() > 5 && currentGame.size() < next_purpose && bestPrecision-20 > secondPrecision) || currentGame.size() >= next_purpose){
                        if (purposeAnswer(things.get(0), bestPrecision)){
                            input = "q";
                        } else {
                            things.remove(0);
                            
                            if (questions.size() <= 3 || secondPrecision < 60 || purpose_number > 2){
                                System.out.println("Désolé je n'ai pas trouvé à quoi vous pensez.");
                                input = "q";
                            } else {
                                next_purpose = currentGame.size()+3;
                            }
                        }
                    }
                }
            }
        }
    }
    
    private int parseInput(String input) {
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
            default:
                System.out.println("Invalid input");
        }
        
        return answer;
    }
    
    private boolean purposeAnswer(Thing answer, float precision) {
        purpose_number++;
        
        Scanner scanner = new Scanner(System.in);
        String input = "";
        
        System.out.println("Vous pensez à un(e) "+answer+" ("+precision+"%)");
        System.out.println("Ai-je raison ? (y/n)");
        
        while (!input.equals("y") && !input.equals("n")){
            input = scanner.nextLine();
            
            if (!input.equals("y") && !input.equals("n")){
                System.out.println("Invalid input");
            }
        }
        
        return input.equals("y");
    }
    
    private Question getRandomQuestion(){
        int randIndex = (int) (1+Math.random()*(questions.size()-2));
        return questions.get(randIndex);
    }
    
    private Question getBestQuestion() {
        int maxScore = -1;
        Question bestQuestion = null;
        
        for (Map.Entry<Integer, Question> entry : questions.entrySet()) {
            int questionScore = getScore(entry.getKey());
            
            //System.out.println(entry.getValue().toString()+": "+questionScore);
            
            if (maxScore < questionScore || maxScore == questionScore && Math.random() > 0.5f){
                maxScore = questionScore;
                bestQuestion = entry.getValue();
            }
        }
        
        return bestQuestion;
    }
    
    private Thing[] getBestThings() {
//        int bestScore = -1;
//        Thing bestThing = null;
//        Thing secondBestThing = null;
//        
//        for (Map.Entry<Integer, Thing> entry : things.entrySet()){
//            if (bestScore <= entry.getValue().getScore()){
//                secondBestThing = bestThing;
//                
//                bestScore = entry.getValue().getScore();
//                bestThing = entry.getValue();
//            }
//        }
//        
//        Thing[] result = {bestThing, secondBestThing};
//        
//        return result;
        
        return null;
    }
    
    private void updateThingsScore(int questionId, int answer) {
        for (Thing thing : things) {
            thing.updateScore(questionId, answer);
        }
        
        Collections.sort(things);
    }
    
    private void cleanThingsList() {
        ArrayList<Integer> keysToDelete = new ArrayList<>();
        
        for (int i = things.size()-1; i >= 0; i--) {
            if (things.get(i).getScore() <= MIN_SCORE_TO_KEEP){
                things.remove(i);
            }
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
            
            things.add(newThing);
        });
    }
    
    private int getScore(int questionId){
        int score = 1;
        
        int[] answers = {1,1,1,1,1,1};
        
        for (Thing thing : things) {
            if (thing.getScore() >= 0){
                answers[thing.getAnswer(questionId)]++;
            }
        }
        
        for (int answer : answers) {
            score *= answer;
        }
        
        return score;
    }
}
