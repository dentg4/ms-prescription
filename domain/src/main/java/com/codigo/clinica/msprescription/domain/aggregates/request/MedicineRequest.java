package com.codigo.clinica.msprescription.domain.aggregates.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicineRequest {

    @NotBlank(message = "El campo nombre es necesario.")
    private String name;

    @NotBlank(message = "La campo descripción es necesaria.")
    private String description;
}
