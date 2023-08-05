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
public class MessagesStatsModel {

    @NotBlank(message = "user_id user is required.")
    private String user_id;

    @NotBlank(message = "partner_id Partner is required.")
    private long partnerId;


    private String customer_id;

    @NotBlank(message = "Login is required.")
    private String login;

    @NotBlank(message = "Password is required.")
    private String password;


    private String period;


    private String startDate;


    private String endDate;

}