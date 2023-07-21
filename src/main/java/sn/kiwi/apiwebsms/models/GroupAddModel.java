package sn.kiwi.apiwebsms.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GroupAddModel {

    private int id;

    @NotBlank(message = "user id is required.")
    private int user_id;

    @NotBlank(message = "name is required.")
    private String name;

    @NotBlank(message = "description is required.")
    private String description;

    @NotBlank(message = "Login is required.")
    private String login;

    @NotBlank(message = "Password is required.")
    private String password;

    private long partnerId;
}
