package de.thb.fbi.msr.maus.todolist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.thb.fbi.msr.maus.todolist.remote.LocalDateTimeDeserializer;
import de.thb.fbi.msr.maus.todolist.remote.LocalDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;


public class Todo implements Serializable {

	private long id;

	private static final long serialVersionUID = 1234567L;

	private String name;

	private String description;

	private boolean isFinished;

	private boolean isImportant;

	//@JsonIgnore
	private LocalDateTime dateTime;

	//Setters and getters

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getDescription(){
		return description;
	}

	public void setDescription(String description){
		this.description = description;
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
		this.setId(id);
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

	public Todo updateFrom(Todo todo) {
		this.setName(todo.getName());
		this.setDescription(todo.getDescription());
		this.setIsFinished(todo.getIsFinished());
		this.setIsImportant(todo.getIsImportant());
		this.setDateTime(todo.getDateTime());

		return this;
	}
}



