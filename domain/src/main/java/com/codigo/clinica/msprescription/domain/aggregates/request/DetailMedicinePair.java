package com.codigo.clinica.msprescription.domain.aggregates.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailMedicinePair {

    @NotNull(message = "El campo cantidad es necesario.")
    private Integer amount;

    @NotBlank(message = "El campo indicacion es necesario.")
    private String indications;

    @NotNull(message = "El campo medicina es necesario.")
    private Long medicineId;
}
