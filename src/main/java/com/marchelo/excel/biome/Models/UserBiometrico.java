package com.marchelo.excel.biome.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBiometrico {
    private String personID;
    private String name;
    private List<DtoHorario> date;
}
