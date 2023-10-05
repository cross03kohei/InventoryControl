package com.cross.inventorycontrol.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemForm {
    private Integer id;
    private Integer category;
    @NotBlank
    private String itemName;
    private Boolean isExpiration;
    @NotNull
    @Min(1)
    private Integer lowerLimit;
}
