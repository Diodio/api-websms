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
public class SignatureModel {

    @NotBlank(message = "name is required.")
    private String name;

    @NotBlank(message = "user id is required.")
    private int user_id;
}
