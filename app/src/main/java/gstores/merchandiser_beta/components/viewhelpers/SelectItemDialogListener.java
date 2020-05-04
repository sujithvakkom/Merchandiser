package gstores.merchandiser_beta.components.viewhelpers;


import java.util.Date;

import gstores.merchandiser_beta.components.models.Model;

public interface SelectItemDialogListener {
    void addModel(Model model, Integer quantity, Integer value);
    void addModel(Model model, Integer quantity, Integer value, boolean isReturn, Date date);
}