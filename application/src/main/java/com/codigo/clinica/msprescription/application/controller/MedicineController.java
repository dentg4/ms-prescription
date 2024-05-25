package com.codigo.clinica.msprescription.application.controller;

import com.codigo.clinica.msprescription.domain.aggregates.dto.MedicineDto;
import com.codigo.clinica.msprescription.domain.aggregates.request.MedicineRequest;
import com.codigo.clinica.msprescription.domain.ports.in.MedicineServiceIn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "API Rest de mantenimiento de Medicina.",
        description = "Incluye EndPoints para realizar el mantenimiento de los Medicina."
)
@RestController
@RequestMapping("/api/v1/ms-prescription/medicine")
@AllArgsConstructor
public class MedicineController {

    private final MedicineServiceIn medicineServiceIn;

    @Operation(summary = "Crear un Medicina.",
            description = "Para usar este EndPoint, debes enviar un objeto Medicina que será guardado en base de datos, previa validacion.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medicina creado con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MedicineDto.class))}),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    })
    @PostMapping("/create")
    public ResponseEntity<MedicineDto> create(@RequestBody MedicineRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(medicineServiceIn.createMedicineIn(request));
    }

    @Operation(summary = "Buscar todos los registros de Medicina.",
            description = "EndPoint que lista todos los registros de Medicina de la base de datos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medicina encontradas con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MedicineDto.class))}),
            @ApiResponse(responseCode = "404", description = "Medicina no encontradas.", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/all")
    public ResponseEntity<List<MedicineDto>> getAll(){
        return ResponseEntity.ok(medicineServiceIn.getAllIn());
    }

    @Operation(summary = "Buscar un Medicina por su Id.",
            description = "Para usar este EndPoint, debes enviar el Id del Medicina a través de un PathVariable.",
            parameters = {
                    @Parameter(name = "id", description = "Id de búsqueda.", required = true, example = "1")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medicina encontrado con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MedicineDto.class))}),
            @ApiResponse(responseCode = "404", description = "Medicina no encontrada.", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/find/{id}")
    public ResponseEntity<MedicineDto> getFindById(@PathVariable Long id){
        return ResponseEntity.ok(medicineServiceIn.findByIdIn(id).orElseThrow());
    }

    @Operation(summary = "Actualizar un Medicina.",
            description = "Para usar este EndPoint, debes enviar un objeto Medicina (sus cambios) que será guardado en base de datos, previa validacion.",
            parameters = {
                    @Parameter(name = "id", description = "Id de Medicina.", required = true, example = "1"),
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medicina actualizada con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MedicineDto.class))}),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<MedicineDto> update(@PathVariable Long id,
                                                  @RequestBody MedicineRequest request){
        return ResponseEntity.ok(medicineServiceIn.updateIn(id, request));
    }

    @Operation(summary = "Eliminar un Medicina por su Id.",
            description = "Para usar este EndPoint, enviar el Id del Medicina a través de un PathVariable.",
            parameters = {
                    @Parameter(name = "id", description = "Id para eliminación.", required = true, example = "1")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medicina eliminado con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MedicineDto.class))}),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MedicineDto> delete(@PathVariable Long id){
        return ResponseEntity.ok(medicineServiceIn.deleteIn(id));
    }
}
