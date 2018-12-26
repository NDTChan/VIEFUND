package mobile.bts.com.viefund.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 3/8/2018.
 */

public class UserModel implements Serializable {
    private static final long serialVersionUID = -7974823823497497357L;

    String id, name, email,status,token,lang;
    public UserModel() {
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public UserModel(String id, String name, String email, String status, String token, String lang) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.status = status;
        this.token = token;
        this.lang = lang;
    }

    public UserModel(String id, String name, String email, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.status = status;
    }
    public UserModel(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
