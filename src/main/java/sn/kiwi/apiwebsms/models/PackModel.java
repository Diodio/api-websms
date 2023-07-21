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
public class PackModel {
    @NotBlank(message = "Pack Id is required.")
    private long pack_id;

    @NotBlank(message = "user id is required.")
    private String customer_id;

    @NotBlank(message = "Purchase type is required.")
    private String purchase_type;

    @NotBlank(message = "Login is required.")
    private String login;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "Partner is required.")
    private long partner_id;
    @NotBlank(message = "id user is required.")
    private long user_id;
    @NotBlank(message = "validationCode user is required.")
    private long validationCode;
}
