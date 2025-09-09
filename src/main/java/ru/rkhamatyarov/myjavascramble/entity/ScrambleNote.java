package ru.rkhamatyarov.myjavascramble.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Objects;

/**
 * Represents a single note stored in the SCRAMBLE schema.
 *
 * <p>This entity is final and designed to be persisted via JPA.</p>
 */
@Entity
@Table(name = "NOTES", schema = "SCRAMBLE")
public final class ScrambleNote {

    /**
     * Database identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Note message.
     */
    @Column(name = "MESSAGE", nullable = false)
    private String message;

    /**
     * Creation timestamp.
     */
    @Column(name = "DATE", nullable = false)
    private Instant date;

    /**
     * Completion status.
     */
    @Column(name = "DONE", nullable = false)
    private boolean done;

    /**
     * Protected no-arg constructor required by JPA.
     */
    protected ScrambleNote() {
        // for JPA
    }

    /**
     * Create a new ScrambleNote with the provided message.
     * Date is set to now and done is false.
     *
     * @param messageParam the note message; must not be null
     */
    public ScrambleNote(final String messageParam) {
        this.message = Objects.requireNonNull(
                messageParam,
                "message must not be null"
        );
        this.date = Instant.now();
        this.done = false;
    }

    /**
     * @return the database identifier, or null if not persisted yet
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id. Primarily for frameworks/tests; normally the id is generated.
     *
     * @param idParam the identifier to set
     */
    public void setId(final Long idParam) {
        this.id = idParam;
    }

    /**
     * @return the note message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Replace the note message.
     *
     * @param messageParam new message; must not be null
     */
    public void setMessage(final String messageParam) {
        this.message = Objects.requireNonNull(
                messageParam,
                "message must not be null"
        );
    }

    /**
     * @return timestamp when the note was created or last set
     */
    public Instant getDate() {
        return date;
    }

    /**
     * Set the note timestamp.
     *
     * @param dateParam timestamp to set; must not be null
     */
    public void setDate(final Instant dateParam) {
        this.date = Objects.requireNonNull(dateParam, "date must not be null");
    }

    /**
     * @return true when the note is marked done
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Mark or unmark the note as done.
     *
     * @param doneParam new done state
     */
    public void setDone(final boolean doneParam) {
        this.done = doneParam;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScrambleNote)) {
            return false;
        }
        ScrambleNote that = (ScrambleNote) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ScrambleNote{"
                + "id=" + id
                + ", message='" + message + '\''
                + ", date=" + date
                + ", done=" + done
                + '}';
    }
}
