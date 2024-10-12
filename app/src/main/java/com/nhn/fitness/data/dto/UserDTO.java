package com.nhn.fitness.data.dto;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class UserDTO {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("age")
    @Expose
    private int age;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("avatarUrl")
    @Expose
    private String avatarUrl;
    @SerializedName("roles")
    @Expose
    private List<String> roles;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("birthdate")
    @Expose
    private Date birthdate;
    @SerializedName("email")
    @Expose
    private String email;

    public void setId(int id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public String getEmail() {
        return email;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserDTO{id=" + id + ", age=" + age + ", name='" + name + "', avatarUrl='" + avatarUrl + "', roles=" + roles + ", username='" + username + "', phoneNumber='" + phoneNumber + "', password='" + password + "', birthdate=" + birthdate + ", email='" + email + "'}";
    }

    public UserDTO clone() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setAge(age);
        userDTO.setName(name);
        userDTO.setAvatarUrl(avatarUrl);
        userDTO.setRoles(roles);
        userDTO.setUsername(username);
        userDTO.setPhoneNumber(phoneNumber);
        userDTO.setPassword(password);
        userDTO.setBirthdate(birthdate);
        userDTO.setEmail(email);
        return userDTO;
    }
}
