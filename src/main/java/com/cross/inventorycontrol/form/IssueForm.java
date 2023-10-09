package com.cross.inventorycontrol.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueForm {
    private Integer ItemId;
    private Integer InventoryId;
    @NotNull
    @Min(1)
    private Integer issueQuantity;
}
