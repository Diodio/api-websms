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
public class InfoMessageDetailModel {

    @NotBlank(message = "id user is required.")
    private long user_id;

    @NotBlank(message = "Id Partner is required.")
    private long partner_id;

    @NotBlank(message = "Login is required.")
    private String login;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "Message is required.")
    private long message_Id;

}