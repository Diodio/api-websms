package sn.kiwi.apiwebsms.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GroupListModel {

    @NotBlank(message = "user id is required.")
    @Valid
    private int user_id;

    @NotBlank(message = "customer id is required.")
    private int customer_id;

    @NotBlank(message = "partner id is required.")
    private long partnerId;

    @NotBlank(message = "Login is required.")
    private String login;

    @NotBlank(message = "Password is required.")
    private String password;

}
