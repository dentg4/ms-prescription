package com.codigo.clinica.msprescription.domain.aggregates.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionDetailListRequest {

    @NotNull(message = "El campo prescripcion es necesario.")
    private Long prescriptionId;

    @NotNull(message = "El campo de lista es necesario.")
    @NotEmpty(message = "La lista no puede estar vacía.")
    private List<DetailMedicinePair> list;
}
