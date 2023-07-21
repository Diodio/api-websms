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
public class ContactModel {

    @NotBlank(message = "UserId is required.")
    private long userId;

    @NotBlank(message = "userLogin is required.")
    private String userLogin;

    @NotBlank(message = "userContactName is required.")
    private String userContactName;

    @NotBlank(message = "userLanguage is required.")
    private String userLanguage;

    private String userProfil;

    @NotBlank(message = "codeP is required.")
    private long codeP;

    @NotBlank(message = "isPack is required.")
    private long isPack;

    @NotBlank(message = "productName is required.")
    private long productName;

    @NotBlank(message = "abonnement is required.")
    private long abonnement;
}
