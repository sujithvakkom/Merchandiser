package gstores.merchandiser_beta.components.models;

public class ItemModel {

    private Integer inventory_item_id;

    private Integer model_id;

    private String item_code;

        private String description;

    private String model_description;

    private String model;

    private Double price;

    public Integer getInventory_item_id() {
        return inventory_item_id;
    }

    public Integer getModel_id() {
        return model_id;
    }

    public String getItem_code() {
        return item_code;
    }

    public String getDescription() {
        return description;
    }

    public String getModel_description() {
        return model_description;
    }

    public String getModel() {
        return model;
    }

    public Double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return model_description;
    }
}