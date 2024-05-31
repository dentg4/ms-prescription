package com.codigo.clinica.msprescription.domain.aggregates.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDetailRequest {

    @NotNull(message = "El campo cantidad es necesario.")
    private Integer amount;

    @NotBlank(message = "El campo indicaciones es necesario.")
    private String indications;

    @NotNull(message = "El campo prescripcion es necesario.")
    private Long prescriptionId;

    @NotNull(message = "El campo medicina es necesario.")
    private Long medicineId;
}
