package models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import remote.LocalDateTimeDeserializer;
import remote.LocalDateTimeSerializer;
import room.LocalDateTimeConverter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@TypeConverters(LocalDateTimeConverter.class)
public class Todo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private static final long serialVersionUID = 1234567L;

    private String name;

    private String description;

    private boolean isFinished;

    private boolean isImportant;

    private static final LocalDateTimeSerializer LocalDateTimeSerializerInstance = new LocalDateTimeSerializer();

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateTime;

    //Setters and getters

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
    public boolean getIsFinished(){
        return isFinished;
    }

    public void setId(long id){this.id = id;}

    public void setIsFinished(boolean isFinished){
        this.isFinished = isFinished;
    }

    public boolean getIsImportant(){
        return isImportant;
    }

    public void setIsImportant(boolean isImportant){
        this.isImportant = isImportant;
    }

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime getDateTime(){
        return dateTime;
    }

    public long getId(){ return id;}

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public void setDateTime(LocalDateTime dateTime){
        this.dateTime = dateTime;
    }

    public Todo() {
        this(null, null, false, false, null);
    }

    public Todo(String name, String description, boolean isFinished, boolean isImportant, LocalDateTime date){
        setName(name);
        setDescription(description);
        setIsFinished(isFinished);
        setIsImportant(isImportant);
        setDateTime(date);
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isFinished=" + isFinished +
                ", isImportant=" + isImportant +
                ", dateTime=" + dateTime +
                '}';
    }

    public boolean equals(Object other) {

        if (other == null || !(other instanceof Todo)) {
            return false;
        } else {
            return ((Todo) other).getId() == this.getId();
        }

    }
}
