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
public class CustomerModel {

    @NotBlank(message = "id user is required.")
    private long user_id;

    @NotBlank(message = "Id customer is required.")
    private long customer_id;
}