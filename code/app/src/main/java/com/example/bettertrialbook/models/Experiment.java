/*
The Experiment class contains a list of trials and information contained in an ExperimentInfo
class. This is used to encapsulate information regarding an experiment when retrieving data from
the database.
Subscribers and blacklisting has yet to be implemented.
 */

package com.example.bettertrialbook.models;

import com.example.bettertrialbook.dal.ExperimentDAL;

import java.util.List;

public class Experiment {
    private ExperimentInfo info;
    private List<Trial> trials;
    // private List<User> subscribers;
    // private List<User> blackList;
    // private List<Question> questions;
    // private CollectionReference collectionRef;

    public Experiment(ExperimentInfo info) {
        this.info = info;
    }

    public ExperimentInfo getInfo() {
        return info;
    }

    public List<Trial> getTrials() {
        return trials;
    }

    /*
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
    */
}
