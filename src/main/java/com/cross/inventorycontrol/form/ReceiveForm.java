package com.cross.inventorycontrol.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiveForm {
    private Integer itemId;
    private Integer receiveId;
    private Integer inventoryId;
    @NotNull
    @Min(1)
    private Integer receiveQuantity;
    @NotNull
    private Integer price;
}
