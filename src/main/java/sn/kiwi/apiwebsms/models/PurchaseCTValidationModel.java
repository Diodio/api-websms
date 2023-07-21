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
public class PurchaseCTValidationModel {

    @NotBlank(message = "user id is required.")
    @Valid
    private int user_id;

    private int customer_id;

    private int pack_id;


    private long partner_id;

    @NotBlank(message = "Login is required.")
    private String login;

    @NotBlank(message = "Password is required.")
    private String password;

    private String codeValidation;


}
