package com.suvasish.Dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String chat_name;
	@ManyToMany
	private Set<User> admins=new HashSet<User>();
	@Override
	public int hashCode() {
		return Objects.hash(admins, chat_image, chat_name, createdBy, id, isGroup, messages, users);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chat other = (Chat) obj;
		return Objects.equals(admins, other.admins) && Objects.equals(chat_image, other.chat_image)
				&& Objects.equals(chat_name, other.chat_name) && Objects.equals(createdBy, other.createdBy)
				&& Objects.equals(id, other.id) && isGroup == other.isGroup && Objects.equals(messages, other.messages)
				&& Objects.equals(users, other.users);
	}
	public Set<User> getAdmins() {
		return admins;
	}
	public void setAdmins(Set<User> admins) {
		this.admins = admins;
	}
	private String chat_image;
	@Column(name="is_group")
	private boolean isGroup;
	//@Column(name="created_by")
	@ManyToOne 
	private User createdBy;
	@ManyToMany
	private Set<User> users=new HashSet<>();
	@OneToMany
	private List<Message> messages=new ArrayList();
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getChat_name() {
		return chat_name;
	}
	public void setChat_name(String chat_name) {
		this.chat_name = chat_name;
	}
	public String getChat_image() {
		return chat_image;
	}
	public void setChat_image(String chat_image) {
		this.chat_image = chat_image;
	}
	public boolean isGroup() {
		return isGroup;
	}
	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}
	public User getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	public List<Message> getMessages() {
		return messages;
	}
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
}
