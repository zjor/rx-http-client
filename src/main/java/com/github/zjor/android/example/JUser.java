package com.github.zjor.android.example;

import com.google.gson.annotations.SerializedName;

public class JUser {

    @SerializedName("id")
    public int id;

    @SerializedName("login")
    public String login;

    @SerializedName("avatar_url")
    public String avatarUrl;

    @SerializedName("url")
    public String url;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JUser{");
        sb.append("id=").append(id);
        sb.append(", login='").append(login).append('\'');
        sb.append(", avatarUrl='").append(avatarUrl).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
