package sn.kiwi.apiwebsms.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.File;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ImportModel {

    @NotBlank(message = "User is required.")
    private long user_id;

    @NotBlank(message = "Group is required.")
    private long group_id;

    @NotBlank(message = "Fields is required.")
    private String fields;  //{"champs_1":"firstName","champs_2":"lastName","champs_3":"cellular"}

    @NotBlank(message = "file is required.")
    private MultipartFile file;

    @NotBlank(message = "aHeader is required.")
    private boolean header;

    @NotBlank(message = "Separator is required.")
    private String separateur;

    @NotBlank(message = "NbLine is required.")
    private String nbrLines;

    @NotBlank(message = "Login is required.")
    private String login;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "partner id is required.")
    private long partnerId;
}
