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
public class SignatureAddModel {

    @NotBlank(message = "name is required.")
    private String name;

    @NotBlank(message = "user id is required.")
    private int user_id;

    @NotBlank(message = "Customer id is required.")
    private int customer_id;

    @NotBlank(message = "Login is required.")
    private String login;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "partner id is required.")
    private long partnerId;
}
