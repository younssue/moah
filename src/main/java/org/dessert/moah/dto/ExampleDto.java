package org.dessert.moah.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExampleDto {

    private String example;

    public ExampleDto(String example){
        this.example = example;
    }
}
