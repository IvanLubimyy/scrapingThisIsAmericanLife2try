package com.company;

import java.util.ArrayList;

// Class used for work with list of downloads tasks
public class RunTasksList {

    private ArrayList list = new ArrayList();

    public void add(Thread t) {
        list.add(t);
    }

    public Thread get(int index) {
        return (Thread)list.get(index);
    }

    public boolean remove(Thread t){
        if(t==null){
            return false;
        }
        if (t.isAlive()){
            return false;
        }
        return list.remove(t);
    }

    public int size() {
        clearDead();
        return list.size();
    }
//Method check and delete from list finished task of download
    public void clearDead(){
        for (int i = 0; i < list.size(); i++) {
            Thread checked = (Thread)get(i);
            if(!checked.isAlive()){
                System.out.println("Download " + checked.getName() + " ends. Congrats!");
                remove(checked);
            }
        }
    }

}
