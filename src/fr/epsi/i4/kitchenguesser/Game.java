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
    private static final int MAX_QUESTIONS = 20;
    
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

                    //System.out.println(things);

                    float bestPrecision     = ((float) things.get(0).getScore()/(currentGame.size()*3))*100;
                    float secondPrecision   = ((float) things.get(1).getScore()/(currentGame.size()*3))*100;
                    
                    if ((bestPrecision-20 > secondPrecision && currentGame.size() > 5) || currentGame.size() > MAX_QUESTIONS){
                        ArrayList<Thing> bestThings = getThingsWithScore(things.get(0).getScore());
                        
                        if (bestThings.size() > 1){
                            things = bestThings;
                            question = getBestQuestion();
                        } else {
                            if (purposeAnswer(things.get(0), bestPrecision)){
                                addMissingAnswers(things.get(0).getDBObject(em).getId());
                                input = "q";
                            } else {
                                learn(things.get(0));
                                input = "q";
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
        Scanner scanner = new Scanner(System.in);
        String input = "";
        
        System.out.println("Vous pensez à un(e) "+answer+" ("+precision+"%)");
        //System.out.println(things);
        System.out.println("Ai-je raison ? (y/n)");
        
        while (!input.equals("y") && !input.equals("n")){
            input = scanner.nextLine();
            
            if (!input.equals("y") && !input.equals("n")){
                System.out.println("Invalid input");
            }
        }
        
        return input.equals("y");
    }
    
    public void learn(Thing purposedThing) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("À quoi pensiez-vous ?");
        String input = scanner.nextLine();
        
        Thing correctThing = null;
        boolean existingThing = false;
        
        Query query = em.createNamedQuery("Things.search");
        query.setParameter("keyword", "%"+input+"%");
        List<Things> bdThings = query.getResultList();
        if (bdThings.size() >= 1){
            System.out.println("J'ai trouvé des objets similaires :");
            
            int i = 0;
            for (Things bdThing : bdThings) {
                System.out.println(i+") "+bdThing.getName());
                i++;
            }
            System.out.println(i+") Aucune des propositions ci-dessus");
            System.out.println("Sélection :");
            
            int choice = Integer.parseInt(scanner.nextLine());
            
            if (choice < bdThings.size()){
                Things correctDBThing = bdThings.get(choice);
                correctThing = new Thing(correctDBThing.getName());
                existingThing = true;
            }
        } else {
            System.out.println("Aucun objet trouvé, je vais ajouter le votre.");
        }
        
        if (correctThing == null){
            correctThing = new Thing(input);
            addNewThing(input);
            System.out.println("Votre objet a été ajouté à notre base.");
        }
        
        System.out.println(correctThing);
        
        if (existingThing || purposedThing.getScore() == (currentGame.size()*3)){
            addNewQuestion(purposedThing, correctThing);
        }
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
            
            if (maxScore < questionScore || maxScore == questionScore && Math.random() > 0.5f){
                maxScore = questionScore;
                bestQuestion = entry.getValue();
            }
        }
        
        return bestQuestion;
    }
    
    private void addNewQuestion(Thing purposed, Thing right) {
        Scanner scanner = new Scanner(System.in);
        String input = "";

        System.out.println("Quelle question pourrait aider à différencier un(e) "+purposed.getName()+" d'un(e) "+right.getName()+" ?");
        String question = scanner.nextLine();

        System.out.println("Réponse pour "+purposed.getName()+" (y/py/u/pn/n)");
        int purposedAnswer = parseInput(scanner.nextLine());

        System.out.println("Réponse pour "+right.getName()+" (y/py/u/pn/n)");
        int correctAnswer = parseInput(scanner.nextLine());

        Questions bdQuestion = new Questions();
        bdQuestion.setQuestion(question);
        persist(bdQuestion);

        ThingsQuestions tq1 = new ThingsQuestions();
        tq1.setQuestionId(bdQuestion.getId());
        tq1.setThingId(purposed.getDBObject(em).getId());
        tq1.setValue(purposedAnswer);
        persist(tq1);

        ThingsQuestions tq2 = new ThingsQuestions();
        tq2.setQuestionId(bdQuestion.getId());
        tq2.setThingId(right.getDBObject(em).getId());
        tq2.setValue(correctAnswer);
        persist(tq2);
    }
    
    private int addNewThing(String name) {
        Things newThing = new Things();
        newThing.setName(name);
        persist(newThing);

        addMissingAnswers(newThing.getId());
        
        return newThing.getId();
    }
    
    private void addMissingAnswers(int thingID) {
        for (UserAnswer answer : currentGame) {
            ThingsQuestions tq = new ThingsQuestions();
            tq.setQuestionId(answer.getQuestionId());
            tq.setThingId(thingID);
            tq.setValue(answer.getValue());

            persistIgnore(tq);
        }
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
            if (thing.getScore() >= 0 && thing.getAnswer(questionId) > 0){
                answers[thing.getAnswer(questionId)]++;
            }
        }
        
        for (int answer : answers) {
            score *= answer;
        }
        
        return score;
    }
    
    private void persist(Object entity){
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
    }
    
    private void persistIgnore(Object entity){
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Duplicate entry, nothing done");
        }
    }
    
    private ArrayList<Thing> getThingsWithScore(int score) {
        ArrayList<Thing> output = new ArrayList();
        
        for (Thing thing : things) {
            if (thing.getScore() == score){
                output.add(thing);
            }
        }
        
        return output;
    }
}
