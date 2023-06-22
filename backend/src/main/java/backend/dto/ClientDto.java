package backend.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientDto {

    private Integer id;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private UserDetailDto userDetail;

    ///////////////////////////////////////////////////////////////////////
    ///////////////////////// GETTERS AND SETTERS /////////////////////////
    ///////////////////////////////////////////////////////////////////////


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserDetailDto getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetailDto userDetail) {
        this.userDetail = userDetail;
    }
}
