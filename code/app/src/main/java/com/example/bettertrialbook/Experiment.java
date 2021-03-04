package com.example.bettertrialbook;

public class Experiment {
    private ExperimentInfo info;
    private List<Trial> trials;
    private List<User> subscribers;
    private List<User> blackList;
    private List<Question> questions;
    private CollectionReference collectionRef;

    public Experiment(ExperimentInfo info, String description, String region, int minTrials, String status, Boolean geoLocationRequired, User owner, String trialType, CollectionReference cr) {

    }

    public ExperimentInfo getInfo() {
        return info;
    }

    public List<Trial> getTrials() {
        return trials;
    }

    public List<User> getSubscribers() {
        return subscribers;
    }

    public List<User> getBlackList() {
        return blackList;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public CollectionReference getCollectionRef() {
        return collectionRef;
    }
}
