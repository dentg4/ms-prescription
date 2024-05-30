package com.codigo.clinica.msprescription.domain.aggregates.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDetailRequest {

    @NotBlank(message = "El campo cantidad es necesario.")
    private Integer amount;

    @NotBlank(message = "El campo indicaciones es necesario.")
    private String indications;

    @NotBlank(message = "El campo prescripcion es necesario.")
    private Long prescriptionId;

    @NotBlank(message = "El campo medicina es necesario.")
    private Long medicineId;
}
