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
public class EnvoiMessageModel {

    @NotBlank(message = "User is required.")
    private long user_id;

    @NotBlank(message = "Customer id is required.")
    private String customer_id;

    @NotBlank(message = "Code partner is required.")
    private String partnerCode;

    @NotBlank(message = "groups ids is required.")
    private String groups;

    @NotBlank(message = "Signature is required.")
    private String signatureName;

    @NotBlank(message = "Signature is required.")
    private String groupName;

    @NotBlank(message = "Signature is required.")
    private int numberMessage;

    @NotBlank(message = "Signature is required.")
    private int messageLength;


    private String subject;
    @NotBlank(message = "Content is required.")

    private int signature;
    @NotBlank(message = "Content is required.")

    private String recipients;

    @NotBlank(message = "Content is required.")
    private String content;

    private String hour;

    private String minute;

    @NotBlank(message = "Sending type is required.")
    private String sendingType;

    private String sendingDate;

    private String sendingTime;


    @NotBlank(message = "Login is required.")
    private String login;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "Partner is required.")
    private long partner_id;
}
