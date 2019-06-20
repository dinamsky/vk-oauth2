package com.example.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Bot {
    @Id
    private String uuid;
    private String token;
    private String username;

    public Bot(String token, String username) {
        this.uuid = UUID.randomUUID().toString();
        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bot bot = (Bot) o;
        return Objects.equals(uuid, bot.uuid) &&
                Objects.equals(token, bot.token) &&
                Objects.equals(username, bot.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, token, username);
    }
}
