package com.cross.inventorycontrol.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditItemForm {
    private Integer id;
    private Integer category;
    @NotBlank
    private String itemName;
    @Min(1)
    private Integer lowerLimit;
    private Boolean isExpiration;
}
