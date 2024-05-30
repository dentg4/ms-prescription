package com.codigo.clinica.msprescription.domain.aggregates.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailMedicinePair {

    @NotBlank(message = "El campo cantidad es necesario.")
    private Integer amount;

    @NotBlank(message = "El campo indicacion es necesario.")
    private String indications;

    @NotBlank(message = "El campo medicina es necesario.")
    private Long medicineId;
}
