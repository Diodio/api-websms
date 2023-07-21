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
public class CustomerInfoReceivedModel {

    @NotBlank(message = "id user is required.")
    private long user_id;

    @NotBlank(message = "Id customer is required.")
    private long customer_id;

    @NotBlank(message = "Login is required.")
    private String login;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "Partner is required.")
    private long partner_id;

}