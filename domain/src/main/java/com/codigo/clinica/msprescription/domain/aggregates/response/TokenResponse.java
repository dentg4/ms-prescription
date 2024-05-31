package com.codigo.clinica.msprescription.domain.aggregates.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    private Boolean isValid;
    private List<String> roles;
    private String username;
    private Boolean isTokenExpired;
}
