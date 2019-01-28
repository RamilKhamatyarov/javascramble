package ru.rkhamatyarov.myjavascramble.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "NOTES", schema="SCRAMBLE", catalog = "")
public class ScrambleNote
{

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "DONE")
    private boolean done;

    public ScrambleNote() {
    }

    public ScrambleNote(String message) {
        this.message = message;
        this.date = new Date();
        this.done = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
