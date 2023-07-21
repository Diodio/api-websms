package sn.kiwi.apiwebsms.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ContactAddListModel {

    @NotBlank(message = "User is required.")
    private long user_id;

    private long customer_id;

    @NotBlank(message = "Group is required.")
    private long group_id;

    @NotBlank(message = "Login is required.")
    private String login;

    @NotBlank(message = "Password is required.")
    private String password;
    @NotBlank(message = "partner id is required.")
    private long partnerId;
    public ArrayList<ContactAddElementModel> contacts;
}

